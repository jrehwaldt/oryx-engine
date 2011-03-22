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
}
