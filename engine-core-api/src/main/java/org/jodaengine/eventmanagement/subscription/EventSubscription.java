package org.jodaengine.eventmanagement.subscription;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.EventCorrelator;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IncomingIntermediateProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.ProcessStartEvent;


/**
 * This interface provides methods for registering events to the {@link EventManager}.
 */
public interface EventSubscription {

    /**
     * Entry point for registering a start event with the {@link EventCorrelator}.
     * 
     * @param startEvent
     *            the start event
     */
    void registerStartEvent(@Nonnull ProcessStartEvent startEvent);

    /**
     * Entry point for registering an event with the {@link EventCorrelator}.
     * 
     * @param intermediateEvent
     *            the intermediate event
     */
    void registerIntermediateEvent(@Nonnull IncomingIntermediateProcessEvent intermediateEvent);
}
