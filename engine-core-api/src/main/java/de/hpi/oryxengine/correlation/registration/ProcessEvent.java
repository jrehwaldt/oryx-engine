package de.hpi.oryxengine.correlation.registration;

import java.util.List;

import de.hpi.oryxengine.correlation.EventType;
import de.hpi.oryxengine.correlation.adapter.PullAdapterConfiguration;

/**
 * The Interface ProcessEvent.
 */
public interface ProcessEvent {

    /**
     * Gets the event type.
     *
     * @return the event type
     */
    EventType getEventType();

    /**
     * Gets the adapter configuration.
     *
     * @return the adapter configuration
     */
    PullAdapterConfiguration getAdapterConfiguration();
    
    /**
     * Gets the conditions.
     *
     * @return the conditions
     */
    List<EventCondition> getConditions();

}
