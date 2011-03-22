package de.hpi.oryxengine.correlation.registration;

import java.util.List;

import de.hpi.oryxengine.correlation.adapter.AdapterType;
import de.hpi.oryxengine.correlation.adapter.PullAdapterConfiguration;

/**
 * The Interface ProcessEvent.
 */
public interface ProcessEvent {

    /**
     * Gets the adapter type.
     *
     * @return the adapter type
     */
    AdapterType getAdapterType();

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
