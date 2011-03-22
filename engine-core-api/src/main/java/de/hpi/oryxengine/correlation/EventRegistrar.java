package de.hpi.oryxengine.correlation;

import de.hpi.oryxengine.correlation.registration.IntermediateEvent;
import de.hpi.oryxengine.correlation.registration.StartEvent;

/**
 * This interface provides methods for registering events to the {@link EventManager}.
 */
public interface EventRegistrar {

    /**
     * Entry point for registering an event with the {@link CorrelationManager}.
     */
    void registerStartEvent(StartEvent event);

    /**
     * Entry point for registering an event with the {@link CorrelationManager}.
     */
    void registerIntermediateEvent(IntermediateEvent event);
}
