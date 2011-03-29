package de.hpi.oryxengine.correlation.adapter;

import javax.annotation.Nonnull;


/**
 * Configuration package for our adapter.
 */
public interface AdapterConfiguration {
    /**
     * Provides the firing adapter's configuration.
     * 
     * @return the adapter's configuration
     */
    @Nonnull AdapterType getAdapterType();
    
    /**
     * Provides a unique adapter name. Should be human readable,
     * but cool if done so. Construct through configuration parameters.
     * 
     * @return an unique adapter name
     */
    @Nonnull String getUniqueName();
}
