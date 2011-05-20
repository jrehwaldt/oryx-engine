package org.jodaengine.eventmanagement.adapter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.EventCorrelator;
import org.jodaengine.eventmanagement.EventManager;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.EventSubscription;
import org.jodaengine.eventmanagement.subscription.EventUnsubscription;
import org.jodaengine.eventmanagement.subscription.ProcessEvent;
import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.eventmanagement.subscription.ProcessStartEvent;

/**
 * This abstract eventAdapter implements our approach of a self correlating event Adapter. It means that this
 * eventAdapter already manages his own queues for correlating {@link ProcessEvent}s with {@link AdapterEvent}s.
 * <p>
 * That is why this adapter implements {@link EventSubscription} in order to fill his own queue with
 * {@link ProcessEvent ProcessEvents}. And, it also implements the {@link EventCorrelator} interface in order to
 * correlate {@link ProcessEvent}s with {@link AdapterEvent}s.
 * </p>
 * 
 * @param <Configuration>
 *            - the {@link AdapterConfiguration} of this adapter
 */
public abstract class AbstractCorrelatingEventAdapter<Configuration extends AdapterConfiguration> extends AbstractEventAdapter<Configuration>
implements EventSubscription, EventUnsubscription, EventCorrelator {

    // Both lists are lazyInitialized
    private List<ProcessEvent> processEvents;
    private List<AdapterEvent> unCorrelatedAdapterEvents;

    /**
     * Default instantiation for this type of EventAdapter. It is forwarded to {@link AbstractEventAdapter}
     * 
     * @param configuration
     *            - the {@link AdapterConfiguration} of this adapter
     */
    public AbstractCorrelatingEventAdapter(Configuration configuration) {

        super(configuration);
    }

    @Override
    public void registerStartEvent(ProcessStartEvent startEvent) {

        // TODO @Gerardo&TobiP Checking if it is already in the list of unCorrelatedAdapterEvents
        getProcessEvents().add(startEvent);
    }

    @Override
    public void registerIntermediateEvent(ProcessIntermediateEvent intermediateEvent) {

        // TODO @Gerardo&TobiP Checking if it is already in the list of unCorrelatedAdapterEvents
        getProcessEvents().add(intermediateEvent);
    }

    @Override
    public void unsubscribeFromStartEvent(ProcessStartEvent startEvent) {

        getProcessEvents().remove(startEvent);
    }

    @Override
    public void unsubscribeFromIntermediateEvent(ProcessIntermediateEvent intermediateEvent) {

        getProcessEvents().remove(intermediateEvent);
    }

    @Override
    public void correlate(AdapterEvent e) {

        for (ProcessEvent processEvent : getProcessEvents()) {

            if (processEvent.evaluate(e)) {
                processEvent.trigger();
            }
        }

        // Push it anyway to the queue of uncorrelated adapterEvents because there may be processEvent that is
        // registered afterwards
        getUnCorrelatedAdapterEvents().add(e);
    }

    /**
     * Private Getter for {@link ProcessEvent}s.
     * 
     * @return a {@link List} of {@link ProcessEvent}s.
     */
    private List<ProcessEvent> getProcessEvents() {

        if (processEvents == null) {
            this.processEvents = new ArrayList<ProcessEvent>();
        }
        return processEvents;
    }

    /**
     * Private Getter for {@link AdapterEvent}s.
     * 
     * @return a {@link List} of {@link AdapterEvent}s.
     */
    private List<AdapterEvent> getUnCorrelatedAdapterEvents() {

        if (unCorrelatedAdapterEvents == null) {
            this.unCorrelatedAdapterEvents = new ArrayList<AdapterEvent>();
        }
        return unCorrelatedAdapterEvents;
    }

    /**
     * Correlation method, which calls the underlying {@link EventCorrelator}.
     * 
     * @see EventCorrelator
     * @see EventManager
     * @param adapterEvent
     *            the event that should be correlated
     */
    protected final void correlateAdapterEvent(@Nonnull AdapterEvent adapterEvent) {

        correlate(adapterEvent);
        logger.info("Correlating {} for {}", adapterEvent, getClass().getSimpleName());
    }
}
