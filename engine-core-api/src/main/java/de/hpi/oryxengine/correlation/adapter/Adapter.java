package de.hpi.oryxengine.correlation.adapter;

import de.hpi.oryxengine.correlation.EventType;

/**
 * General {@link EventManager} adapter.
 */
public interface Adapter {
    /**
     * Returns the adapter's type.
     * 
     * @return the adapter's type
     */
    EventType getEventType();
}
