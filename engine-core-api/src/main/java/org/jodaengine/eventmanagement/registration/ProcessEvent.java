package org.jodaengine.eventmanagement.registration;

import java.util.List;

import org.jodaengine.eventmanagement.adapter.AdapterConfiguration;

/**
 * The Interface ProcessEvent. All process events have an assigned adapter with a given configuration that may produce
 * incoming events that correlate against the process event.
 */
public interface ProcessEvent {

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
