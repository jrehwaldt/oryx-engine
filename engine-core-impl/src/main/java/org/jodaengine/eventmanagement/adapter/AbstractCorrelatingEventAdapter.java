package org.jodaengine.eventmanagement.adapter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.EventCorrelator;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.incoming.IncomingStartProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IncomingIntermediateProcessEvent;
import org.jodaengine.eventmanagement.subscription.EventSubscription;
import org.jodaengine.eventmanagement.subscription.EventUnsubscription;
import org.jodaengine.eventmanagement.subscription.IncomingProcessEvent;

/**
 * This abstract eventAdapter implements our approach of a self correlating event Adapter. It means that this
 * eventAdapter already manages his own queues for correlating {@link IncomingProcessEvent}s with {@link AdapterEvent}s.
 * <p>
 * That is why this adapter implements {@link EventSubscription} in order to fill his own queue with
 * {@link IncomingProcessEvent ProcessEvents}. And, it also implements the {@link EventCorrelator} interface in order to
 * correlate {@link IncomingProcessEvent}s with {@link AdapterEvent}s.
 * </p>
 * 
 * @param <Configuration>
 *            - the {@link AdapterConfiguration} of this adapter
 */
public abstract class AbstractCorrelatingEventAdapter
    <Configuration extends AdapterConfiguration> extends AbstractEventAdapter<Configuration>
    implements EventSubscription, EventUnsubscription, EventCorrelator {

    // Both lists are lazyInitialized
    // TODO @EVENTTEAM rename the processEvents - talk to Gerald
    private List<IncomingProcessEvent> processEvents;
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
    public void registerStartEvent(IncomingStartProcessEvent startEvent) {

        // We don't need to check for uncorrelated events since we only want to know about
        // start events from the moment we dployed a process instance
        getProcessEvents().add(startEvent);
    }

    @Override
    public void registerIncomingIntermediateEvent(IncomingIntermediateProcessEvent intermediateEvent) {

        // TODO @EVENTMANAGERTEAM: Checking if it is already in the list of unCorrelatedAdapterEvents
        getProcessEvents().add(intermediateEvent);
    }

    @Override
    public void unsubscribeFromStartEvent(IncomingStartProcessEvent startEvent) {

        getProcessEvents().remove(startEvent);
    }

    @Override
    public void unsubscribeFromIncomingIntermediateEvent(IncomingIntermediateProcessEvent intermediateEvent) {

        getProcessEvents().remove(intermediateEvent);
    }

    @Override
    public void correlate(AdapterEvent e) {

        // Copy the list of processEvents because the array is modified while it is iterated
        List<IncomingProcessEvent> tmpProcessEvents = new ArrayList<IncomingProcessEvent>(getProcessEvents());
        for (IncomingProcessEvent processEvent : tmpProcessEvents) {

            if (processEvent.evaluate(e)) {

                correlateProcessEvent(processEvent);
            }
        }

        // Push it anyway to the queue of uncorrelated adapterEvents because there may be processEvent that is
        // registered afterwards
        getUnCorrelatedAdapterEvents().add(e);
    }

    /**
     * Knows what to do with a processEvent when it was correlated successfully.
     * 
     * @param processEvent
     *            - the {@link IncomingProcessEvent} that was correlated successfully
     */
    private void correlateProcessEvent(IncomingProcessEvent processEvent) {

        processEvent.trigger();

        // If the processEvent is an processIntermediateEvent then it is removed from the list so that it cannot be
        // correlated more
        if (processEvent instanceof IncomingIntermediateProcessEvent) {

            getProcessEvents().remove(processEvent);
        }
    }

    /**
     * Getter for {@link IncomingProcessEvent}s.
     * 
     * Are public for testing issues.
     * 
     * @return a {@link List} of {@link IncomingProcessEvent}s.
     */
    public List<IncomingProcessEvent> getProcessEvents() {

        if (processEvents == null) {
            this.processEvents = new ArrayList<IncomingProcessEvent>();
        }
        return processEvents;
    }

    /**
     * Getter for {@link AdapterEvent}s.
     * 
     * Are public for testing issues.
     * 
     * @return a {@link List} of {@link AdapterEvent}s.
     */
    public List<AdapterEvent> getUnCorrelatedAdapterEvents() {

        if (unCorrelatedAdapterEvents == null) {
            this.unCorrelatedAdapterEvents = new ArrayList<AdapterEvent>();
        }
        return unCorrelatedAdapterEvents;
    }

    /**
     * Correlation method, which calls the underlying {@link EventCorrelator}.
     * 
     * @see EventCorrelator
     * @see EventsubscriptionManager
     * @param adapterEvent
     *            the event that should be correlated
     */
    protected final void correlateAdapterEvent(@Nonnull AdapterEvent adapterEvent) {

        correlate(adapterEvent);
        logger.info("Correlating {} for {}", adapterEvent, getClass().getSimpleName());
    }
}
