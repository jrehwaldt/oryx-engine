package org.jodaengine.eventmanagement.adapter.configuration;

import org.jodaengine.eventmanagement.AdapterManagement;
import org.jodaengine.eventmanagement.EventConfiguration;
import org.jodaengine.eventmanagement.adapter.EventAdapter;

/**
 * Configuration package for our adapter.
 */
public interface AdapterConfiguration extends EventConfiguration {

    
    /**
     * Registers the adapter for this configuration.
     *
     * @param adapterRegistrar - the place where you can register your adapters
     * @return the schedule {@link EventAdapter}
     */
    EventAdapter registerAdapter(AdapterManagement adapterRegistrar);
    
    @Override
    boolean equals(Object eventAdapter);
    
    @Override
    int hashCode();
}
