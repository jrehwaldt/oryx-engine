package org.jodaengine.eventmanagement.processevent;

import org.jodaengine.eventmanagement.adapter.EventType;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;

/**
 * The Interface for a general ProcessEvent.
 * All process events have an assigned adapter with a given configuration that may produce
 * events.
 */
public interface ProcessEvent {
    /**
     * Gets the adapter configuration.
     * 
     * @return the adapter configuration
     */

    AdapterConfiguration getAdapterConfiguration();
    
    /**
     * Gets the type of the event.
     *
     * @return the event type
     */
    EventType getEventType();
}
