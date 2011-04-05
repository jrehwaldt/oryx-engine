package de.hpi.oryxengine.correlation.adapter;

import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.correlation.EventConfiguration;


/**
 * Configuration package for our adapter.
 */
public interface AdapterConfiguration extends EventConfiguration {
     
        
    /**
     * Creates the adapter for this configuration.
     *
     * @param c the Correlation Manager 
     * @return the correlation adapter for this config
     */
    CorrelationAdapter createAdapter(CorrelationManager c);
}
