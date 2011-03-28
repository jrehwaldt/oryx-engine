package de.hpi.oryxengine.correlation.registration;

import java.util.List;

import de.hpi.oryxengine.correlation.adapter.AdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.AdapterType;
import de.hpi.oryxengine.correlation.adapter.PullAdapterConfiguration;

/**
 * The Interface ProcessEvent. All process events have an assigned adapter with a given configuration that may produce
 * incoming events that correlate against the process event.
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
 
    AdapterConfiguration getAdapterConfiguration();

    /**
     * Gets the conditions.
     * 
     * @return the conditions
     */
    List<EventCondition> getConditions();

}
