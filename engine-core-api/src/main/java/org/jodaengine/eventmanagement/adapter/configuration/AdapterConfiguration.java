package org.jodaengine.eventmanagement.adapter.configuration;

import org.jodaengine.eventmanagement.AdapterManagement;
import org.jodaengine.eventmanagement.EventConfiguration;
import org.jodaengine.eventmanagement.adapter.CorrelationAdapter;

/**
 * Configuration package for our adapter.
 */
public interface AdapterConfiguration extends EventConfiguration {

    
    // TODO not everyone is a TimingManager @ throws  
    /**
     * Registers the adapter for this configuration.
     *
     * @param adapterRegistrar - the place where you can register your adapters
     * @return the schedule {@link CorrelationAdapter}
     */
    CorrelationAdapter registerAdapter(AdapterManagement adapterRegistrar);
}
