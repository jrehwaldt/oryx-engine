package de.hpi.oryxengine.correlation;

import java.util.Date;

import javax.annotation.Nonnull;

/**
 * A basic event, which is returned to the {@link EventManager} by adapter implementations.
 */
public interface AdapterEvent {
    
    /**
     * Gets the event type.
     * 
     * @return the event type
     */
    @Nonnull EventType getEventType();
    
    /**
     * Returns the event's creation time.
     * 
     * @return a timestamp
     */
    @Nonnull Date getTimestamp();
}
