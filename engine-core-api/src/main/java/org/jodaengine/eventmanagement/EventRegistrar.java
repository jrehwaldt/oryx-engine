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
     * @param event
     *            the start event
     */
    void registerStartEvent(@Nonnull ProcessStartEvent event);

    /**
     * Entry point for registering an event with the {@link CorrelationManager}.
     * 
     * @param event
     *            the intermediate event
     * @return the name of the job for the event
     */
    String registerIntermediateEvent(@Nonnull ProcessIntermediateEvent event);
}
