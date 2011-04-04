package de.hpi.oryxengine.correlation;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.registration.StartEvent;

/**
 * This interface provides methods for registering events to the {@link EventManager}.
 */
public interface EventRegistrar {

    /**
     * Entry point for registering an event with the {@link CorrelationManager}.
     * 
     * @param event the start event
     */
    void registerStartEvent(@Nonnull StartEvent event);


}
