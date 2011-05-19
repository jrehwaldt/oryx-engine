package org.jodaengine.eventmanagement;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.registration.ProcessIntermediateEvent;
import org.jodaengine.eventmanagement.registration.ProcessStartEvent;

/**
 * This interface provides methods for registering events to the {@link EventManager}.
 */
public interface EventRegistrar {

    /**
     * Entry point for registering an event with the {@link CorrelationManager}.
     * 
     * @param startEvent
     *            the start event
     */
    void registerStartEvent(@Nonnull ProcessStartEvent startEvent);

    /**
     * Entry point for registering an event with the {@link CorrelationManager}.
     * 
     * @param intermediateEvent
     *            the intermediate event
     */
    void registerIntermediateEvent(@Nonnull ProcessIntermediateEvent intermediateEvent);
}
