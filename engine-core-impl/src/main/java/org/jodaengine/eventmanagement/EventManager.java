package org.jodaengine.eventmanagement;

import java.util.HashSet;
import java.util.Set;

import org.jodaengine.bootstrap.Service;
import org.jodaengine.eventmanagement.adapter.CorrelationAdapter;
import org.jodaengine.eventmanagement.adapter.InboundAdapter;
import org.jodaengine.eventmanagement.adapter.InboundPullAdapter;
import org.jodaengine.eventmanagement.adapter.error.ErrorAdapter;
import org.jodaengine.eventmanagement.adapter.error.ErrorAdapterConfiguration;
import org.jodaengine.eventmanagement.registration.ProcessIntermediateEvent;
import org.jodaengine.eventmanagement.registration.ProcessStartEvent;
import org.jodaengine.eventmanagement.timing.TimingManager;
import org.jodaengine.eventmanagement.timing.TimingManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A concrete implementation of our engines Event Manager.
 */
public class EventManager implements EventRegistrar, AdapterRegistrar, Service {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Set<CorrelationAdapter> eventAdapters;

    private TimingManagerImpl timingManager;

    private ErrorAdapter errorAdapter;

    public EventManager() {

        this.errorAdapter = new ErrorAdapter(new ErrorAdapterConfiguration());
        this.timingManager = new TimingManagerImpl(errorAdapter);
    }

    /**
     * This method starts the {@link EventManager} and its dependent services.
     */
    public void start() {

        logger.info("Starting the correlation manager");
        registerAdapter(this.errorAdapter);
    }

    @Override
    public void stop() {

        logger.info("Stopping the correlation manager");
    }

    // ==== EventSubscription ====

    @Override
    public void registerStartEvent(ProcessStartEvent startEvent) {

        // Delegate the work of registering the adapter to the configuration
        startEvent.getAdapterConfiguration().registerAdapter(this);
    }

    /**
     * Checks if the Event is already registers and adds the eventAdapter if necessary.
     * 
     * @param eventAdapter
     *            - the {@link CorrelationAdapter eventAdapter} that should be added.
     */
    private void addEventAdapterToEvent(CorrelationAdapter eventAdapter) {

        if (!getEventAdapters().contains(eventAdapter)) {
            getEventAdapters().add(eventAdapter);
        }
    }

    @Override
    public void registerIntermediateEvent(ProcessIntermediateEvent event) {

        // TODO Auto-generated method stub
    }

    // ==== AdapterMangement ====
    @Override
    public CorrelationAdapter registerAdapter(CorrelationAdapter adapter) {

        addEventAdapterToEvent(adapter);
        return adapter;
    }

    @Override
    public InboundAdapter registerInboundAdapter(InboundAdapter inboundAdapter) {

        addEventAdapterToEvent(inboundAdapter);

        return inboundAdapter;
    }

    @Override
    public InboundPullAdapter registerInboundPullAdapter(InboundPullAdapter inboundPullAdapter) {

        timingManager.registerJobForInboundPullAdapter(inboundPullAdapter);
        addEventAdapterToEvent(inboundPullAdapter);

        return inboundPullAdapter;
    }

    @Override
    public TimingManager getTimer() {

        return timingManager;
    }

    @Override
    public CorrelationManager getEventCorrelation() {

        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Retrieves the {@link CorrelationAdapter eventAdapters}.
     * 
     * @return a set containing the currently registered {@link CorrelationAdapter eventAdapters}
     */
    public Set<CorrelationAdapter> getEventAdapters() {

        if (eventAdapters == null) {
            this.eventAdapters = new HashSet<CorrelationAdapter>();
        }
        return eventAdapters;
    }
}
