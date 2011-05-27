package org.jodaengine.eventmanagement;


import org.jodaengine.eventmanagement.adapter.EventType;

/**
 * The Interface Configuration. The most generic Configuration of an Event.
 */
public interface EventConfiguration {

    /**
     * Gets the adapter type.
     * 
     * @return the adapter type
     */
    EventType getEventType();
}
