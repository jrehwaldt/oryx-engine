package org.jodaengine.eventmanagement;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.params.CoreConnectionPNames;
import org.jodaengine.bootstrap.Service;
import org.jodaengine.eventmanagement.adapter.AbstractCorrelatingEventAdapter;
import org.jodaengine.eventmanagement.adapter.CorrelationAdapter;
import org.jodaengine.eventmanagement.adapter.InboundAdapter;
import org.jodaengine.eventmanagement.adapter.InboundPullAdapter;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.error.ErrorAdapter;
import org.jodaengine.eventmanagement.adapter.error.ErrorAdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.EventSubscription;
import org.jodaengine.eventmanagement.subscription.EventUnsubscription;
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
public class EventManager implements EventSubscription, EventUnsubscription, AdapterManagement, Service {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Map<AdapterConfiguration, CorrelationAdapter> eventAdapters;

    private QuartzJobManager timingManager;

    private ErrorAdapter errorAdapter;

    public EventManager() {

        this.errorAdapter = new ErrorAdapter(new ErrorAdapterConfiguration());
        this.timingManager = new QuartzJobManager(errorAdapter);
    }

    /**
     * This method starts the {@link EventManager} and its dependent services.
     */
    public void start() {

        logger.info("Starting the Event Manager.");
        registerAdapter(this.errorAdapter);

        timingManager.start();
    }

    @Override
    public void stop() {

        logger.info("Stopping the Event Manager.");

        timingManager.stop();
    }

    // ==== EventSubscription ====

    @Override
    public void registerStartEvent(ProcessStartEvent startEvent) {

        AbstractCorrelatingEventAdapter<?> correlatingAdapter = getAdapterForProcessEvent(startEvent);
        correlatingAdapter.registerStartEvent(startEvent);
    }

    @Override
    public void registerIntermediateEvent(ProcessIntermediateEvent intermediateEvent) {

        AbstractCorrelatingEventAdapter<?> correlatingAdapter = getAdapterForProcessEvent(intermediateEvent);
        correlatingAdapter.registerIntermediateEvent(intermediateEvent);
    }

    @Override
    public void unsubscribeFromStartEvent(ProcessStartEvent startEvent) {

        AbstractCorrelatingEventAdapter<?> correlatingAdapter = getAdapterForProcessEvent(startEvent);
        correlatingAdapter.unsubscribeFromStartEvent(startEvent);
    }

    @Override
    public void unsubscribeFromIntermediateEvent(ProcessIntermediateEvent intermediateEvent) {

        AbstractCorrelatingEventAdapter<?> correlatingAdapter = getAdapterForProcessEvent(intermediateEvent);
        correlatingAdapter.unsubscribeFromIntermediateEvent(intermediateEvent);

        if (correlatingAdapter instanceof InboundPullAdapter) {
            InboundPullAdapter inboundPullAdapter = (InboundPullAdapter) correlatingAdapter;
            unregisterInboundPullAdapterAtJobManager(inboundPullAdapter);
        }
    }

    /**
     * Checks if the Event is already registers and adds the eventAdapter if necessary.
     * 
     * @param eventAdapter
     *            - the {@link CorrelationAdapter eventAdapter} that should be added.
     */
    private void addToEventAdapter(CorrelationAdapter eventAdapter) {

        if (!getEventAdapters().containsKey(eventAdapter.getConfiguration())) {
            getEventAdapters().put(eventAdapter.getConfiguration(), eventAdapter);
        }
    }

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
    @Override
    public CorrelationAdapter registerAdapter(CorrelationAdapter adapter) {

        addToEventAdapter(adapter);
        return adapter;
    }

    @Override
    public InboundAdapter registerInboundAdapter(InboundAdapter inboundAdapter) {

        addToEventAdapter(inboundAdapter);
        return inboundAdapter;
    }

    @Override
    public InboundPullAdapter registerInboundPullAdapter(InboundPullAdapter inboundPullAdapter) {

        registerInboundPullAdapterAtJobManager(inboundPullAdapter);

        addToEventAdapter(inboundPullAdapter);

        return inboundPullAdapter;
    }

    /**
     * Encapsulates the registration of the {@link InboundPullAdapter}.
     * 
     * @param inboundPullAdapter
     *            - the {@link InboundPullAdapter} to register
     */
    private void registerInboundPullAdapterAtJobManager(InboundPullAdapter inboundPullAdapter) {

        try {

            timingManager.registerJobForInboundPullAdapter(inboundPullAdapter);

        } catch (AdapterSchedulingException aSE) {
            String errorMessage = "An exception occurred while registering a QuartzJob for the adapter '"
                + inboundPullAdapter.getConfiguration().getUniqueName() + "'";
            logger.error(errorMessage, aSE);
            throw new JodaEngineRuntimeException(errorMessage, aSE);
        }
    }

    /**
     * Encapsulates the 'unregistration' of the {@link InboundPullAdapter}.
     * 
     * @param inboundPullAdapter
     *            - the {@link InboundPullAdapter} to unregister
     */
    private void unregisterInboundPullAdapterAtJobManager(InboundPullAdapter inboundPullAdapter) {
    
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

        // TODO Auto-generated method stub
        return null;
    }

    // === Getter ===
    /**
     * Retrieves the {@link CorrelationAdapter eventAdapters}.
     * 
     * @return a set containing the currently registered {@link CorrelationAdapter eventAdapters}
     */
    public Map<AdapterConfiguration, CorrelationAdapter> getEventAdapters() {

        if (eventAdapters == null) {
            this.eventAdapters = new HashMap<AdapterConfiguration, CorrelationAdapter>();
        }
        return eventAdapters;
    }
}
