package de.hpi.oryxengine.correlation.adapter;

import de.hpi.oryxengine.correlation.AdapterRegistrar;
import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.correlation.EventConfiguration;
import de.hpi.oryxengine.exception.AdapterSchedulingException;

/**
 * Configuration package for our adapter.
 */
public interface AdapterConfiguration extends EventConfiguration {

    /**
     * Registers the adapter for this configuration.
     * 
     * @param adapterRegistrar
     *            - the {@link CorrelationManager}
     * @return the schedule {@link CorrelationAdapter}
     * @exception AdapterSchedulingException thrown if scheduling the adapter failed
     */
    CorrelationAdapter registerAdapter(AdapterRegistrar adapterRegistrar)
    throws AdapterSchedulingException;
}
