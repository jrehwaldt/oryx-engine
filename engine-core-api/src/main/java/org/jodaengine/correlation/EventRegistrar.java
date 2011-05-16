package org.jodaengine.correlation;

import javax.annotation.Nonnull;

import org.jodaengine.correlation.registration.IntermediateEvent;
import org.jodaengine.correlation.registration.StartEvent;

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
    void registerStartEvent(@Nonnull StartEvent event);

    /**
     * Entry point for registering an event with the {@link CorrelationManager}.
     * 
     * @param event
     *            the intermediate event
     * @return the name of the job for the event
     */
    String registerIntermediateEvent(@Nonnull IntermediateEvent event);
}
