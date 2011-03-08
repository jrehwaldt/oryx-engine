package de.hpi.oryxengine.correlation;

/**
 * A basic event, which is returned to the {@link EventManager} by adapter implementations.
 */
public interface AdapterEvent {
    
    /**
     * Gets the event type.
     * @return the event type
     */
    EventType getEventType();
    
    
}
