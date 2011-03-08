package de.hpi.oryxengine.correlation;

/**
 * This interface provides methods for registering
 * events to the {@link EventManager}.
 */
public interface EventRegistrar {
    
    /**
     * Entry point for registering an event with the {@link CorrelationManager}.
     */
    void registerCorrelationEvent();
}
