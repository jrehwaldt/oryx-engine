package org.jodaengine.correlation.adapter;

import org.jodaengine.correlation.AdapterRegistrar;
import org.jodaengine.correlation.CorrelationManager;
import org.jodaengine.correlation.EventConfiguration;
import org.jodaengine.exception.AdapterSchedulingException;

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
     * @exception AdapterSchedulingException
     *                thrown if scheduling the adapter failed
     */
    CorrelationAdapter registerAdapter(AdapterRegistrar adapterRegistrar)
    throws AdapterSchedulingException;
}
