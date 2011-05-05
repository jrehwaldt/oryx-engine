package de.hpi.oryxengine.correlation.adapter;

import de.hpi.oryxengine.correlation.AdapterRegistrar;
import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.correlation.EventConfiguration;
import de.hpi.oryxengine.exception.AdapterSchedulingException;

/**
 * Configuration package for our adapter.
 */
public interface AdapterConfiguration extends EventConfiguration {

    // /**
    // * Creates the adapter for this configuration.
    // *
    // * @param c
    // * the Correlation Manager
    // * @return the correlation adapter for this config
    // */
    // CorrelationAdapter createAdapter(CorrelationManager c);

    /**
     * Registers the adapter for this configuration.
     * 
     * @param adapterRegistrar
     *            - the {@link CorrelationManager}
     */
    CorrelationAdapter registerAdapter(AdapterRegistrar adapterRegistrar)
    throws AdapterSchedulingException;
}
