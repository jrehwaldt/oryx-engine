package org.jodaengine.eventmanagement;

import java.util.HashMap;
import java.util.Map;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.bootstrap.Service;
import org.jodaengine.eventmanagement.adapter.AbstractCorrelatingEventAdapter;
import org.jodaengine.eventmanagement.adapter.EventAdapter;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.error.ErrorAdapter;
import org.jodaengine.eventmanagement.adapter.error.ErrorAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.incoming.IncomingAdapter;
import org.jodaengine.eventmanagement.adapter.incoming.IncomingPullAdapter;
import org.jodaengine.eventmanagement.subscription.ProcessEvent;
import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.eventmanagement.subscription.ProcessStartEvent;
import org.jodaengine.eventmanagement.timing.QuartzJobManager;
import org.jodaengine.eventmanagement.timing.TimingManager;
import org.jodaengine.exception.AdapterSchedulingException;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A concrete implementation of our engines Event Manager.
 */
public class EventManager implements EventSubscriptionManager, AdapterManagement, Service {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * A map used to keep track of all the Correlation adapters. One Adapter Configuration is assigned its appropriate
     * event adapter.
     */
    private Map<AdapterConfiguration, EventAdapter> eventAdapters;

    private QuartzJobManager timingManager;

    private ErrorAdapter errorAdapter;

    private boolean running = false;
    
    private JodaEngineServices services;

    /**
     * Instantiates a new event manager.
     */
    public EventManager() {

        // TODO @EVENTTEAM: do we really need this? I guess not it could be like every other adapter
        this.errorAdapter = new ErrorAdapter(new ErrorAdapterConfiguration());
        this.timingManager = new QuartzJobManager(errorAdapter);
    }

    @Override
    public void start(JodaEngineServices services) {

        logger.info("Starting the Event Manager.");
        registerAdapter(this.errorAdapter);
        
        this.services = services;

        timingManager.start();

        this.running = true;
    }

    @Override
    public boolean isRunning() {

        return this.running;
    }

    @Override
    public void stop() {

        logger.info("Stopping the Event Manager.");

        timingManager.stop();

        this.running = false;
    }

    // ==== EventSubscription ====

    @Override
    public void registerStartEvent(ProcessStartEvent startEvent) {

        // startEvents need the NavigatorService in order to start a process instance
        startEvent.injectNavigatorService(services.getNavigatorService());
        AbstractCorrelatingEventAdapter<?> correlatingAdapter = getAdapterForProcessEvent(startEvent);
        correlatingAdapter.registerStartEvent(startEvent);
    }

    @Override
    public void registerIntermediateEvent(ProcessIntermediateEvent intermediateEvent) {

        AbstractCorrelatingEventAdapter<?> correlatingAdapter = getAdapterForProcessEvent(intermediateEvent);
        correlatingAdapter.registerIntermediateEvent(intermediateEvent);
    }

    // QUESTION: Just call it unsubscribeStartEvent ?
    @Override
    public void unsubscribeFromStartEvent(ProcessStartEvent startEvent) {

        AbstractCorrelatingEventAdapter<?> correlatingAdapter = getAdapterForProcessEvent(startEvent);
        correlatingAdapter.unsubscribeFromStartEvent(startEvent);
    }

    @Override
    public void unsubscribeFromIntermediateEvent(ProcessIntermediateEvent intermediateEvent) {

        AbstractCorrelatingEventAdapter<?> correlatingAdapter = getAdapterForProcessEvent(intermediateEvent);
        correlatingAdapter.unsubscribeFromIntermediateEvent(intermediateEvent);

        if (correlatingAdapter instanceof IncomingPullAdapter) {
            IncomingPullAdapter inboundPullAdapter = (IncomingPullAdapter) correlatingAdapter;
            unregisterInboundPullAdapterAtJobManager(inboundPullAdapter);
        }
    }

    /**
     * Checks if the Event is already registered and adds the eventAdapter if necessary.
     * 
     * @param eventAdapter
     *            - the {@link EventAdapter eventAdapter} that should be added.
     * @return true if and event adapter was added and false if the adapter already existed and didn't need to be added
     */
    private boolean addToEventAdapters(EventAdapter eventAdapter) {

        if (!isAlreadyRegistered(eventAdapter)) {
            this.getEventAdapters().put(eventAdapter.getConfiguration(), eventAdapter);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Checks if the event is already registered.
     *
     * @param eventAdapter the event adapter
     * @return true, if it is already registered, false if it is not.
     */
    private boolean isAlreadyRegistered(EventAdapter eventAdapter) {
        return (getEventAdapters().containsKey(eventAdapter.getConfiguration()));
    }

    /**
     * Gets the adapter for the specified process event.
     * If there is no adapter, a new adapter is returned.
     * 
     * @param processEvent
     *            the process event
     * @return the adapter for the process event
     */
    private AbstractCorrelatingEventAdapter<?> getAdapterForProcessEvent(ProcessEvent processEvent) {

        AbstractCorrelatingEventAdapter<?> eventAdapter = (AbstractCorrelatingEventAdapter<?>) getEventAdapters().get(
            processEvent.getAdapterConfiguration());
        if (eventAdapter != null) {
            // Then it means that the eventAdapter already exists, so we return it
            return eventAdapter;
        }

        // Otherwise we will register a new one
        // Delegate the work of registering the adapter to the configuration
        eventAdapter = (AbstractCorrelatingEventAdapter<?>) processEvent.getAdapterConfiguration()
        .registerAdapter(this);
        return eventAdapter;
    }

    // ==== AdapterMangement ====
    // TODO @EVENTMANAGERTEAM: registerAdapter and registerInboundAdapter is basically the same + why not register
    // Adapters for adapter configurations?.
    @Override
    public EventAdapter registerAdapter(EventAdapter adapter) {

        addToEventAdapters(adapter);
        return adapter;
    }

    @Override
    public IncomingAdapter registerInboundAdapter(IncomingAdapter inboundAdapter) {

        addToEventAdapters(inboundAdapter);
        return inboundAdapter;
    }

    @Override
    public IncomingPullAdapter registerInboundPullAdapter(IncomingPullAdapter inboundPullAdapter) {

        // if the the Event Adapter has to be added to the list, we also need to register it with the timing manager
        // otherwise a registration at the timing manager should already be present.
        if (addToEventAdapters(inboundPullAdapter)) {
            // Question: Registration maybe in the adapter itself?
            registerInboundPullAdapterAtJobManager(inboundPullAdapter);
        }

        return inboundPullAdapter;
    }

    /**
     * Encapsulates the registration of the {@link IncomingPullAdapter}.
     * 
     * @param inboundPullAdapter
     *            - the {@link IncomingPullAdapter} to register
     */
    private void registerInboundPullAdapterAtJobManager(IncomingPullAdapter inboundPullAdapter) {

        try {

            timingManager.registerJobForInboundPullAdapter(inboundPullAdapter);

        } catch (AdapterSchedulingException adapterSchedulingException) {
            String errorMessage = "An exception occurred while registering a QuartzJob for the adapter '"
                + inboundPullAdapter.getConfiguration().getUniqueName() + "'";
            logger.error(errorMessage, adapterSchedulingException);
            throw new JodaEngineRuntimeException(errorMessage, adapterSchedulingException);
        }
    }

    /**
     * Encapsulates the 'unregistration' of the {@link IncomingPullAdapter}.
     * 
     * @param inboundPullAdapter
     *            - the {@link IncomingPullAdapter} to unregister
     */
    private void unregisterInboundPullAdapterAtJobManager(IncomingPullAdapter inboundPullAdapter) {

        try {

            timingManager.unregisterJobForInboundPullAdapter(inboundPullAdapter);

        } catch (AdapterSchedulingException aSE) {
            String errorMessage = "An exception occurred while registering a QuartzJob for the adapter '"
                + inboundPullAdapter.getConfiguration().getUniqueName() + "'";
            logger.error(errorMessage, aSE);
            throw new JodaEngineRuntimeException(errorMessage, aSE);
        }
    }

    @Override
    public TimingManager getTimer() {

        return timingManager;
    }

    @Override
    public EventCorrelator getEventCorrelator() {

        // TODO @EVENTTEAM: lol empty wtf is this srsly? GEEERAARRDOOO???
        return null;
    }

    // === Getter ===
    /**
     * Retrieves the {@link EventAdapter eventAdapters}.
     * 
     * @return a set containing the currently registered {@link EventAdapter eventAdapters}
     */
    public Map<AdapterConfiguration, EventAdapter> getEventAdapters() {

        if (eventAdapters == null) {
            this.eventAdapters = new HashMap<AdapterConfiguration, EventAdapter>();
        }
        return eventAdapters;
    }
}
