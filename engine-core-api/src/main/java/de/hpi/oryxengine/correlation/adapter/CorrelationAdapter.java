package de.hpi.oryxengine.correlation.adapter;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.EventManager;

/**
 * General {@link EventManager} adapter.
 */
public interface CorrelationAdapter {
    /**
     * Returns the adapter's type.
     * 
     * @return the adapter's type
     */
    @Nonnull
    EventType getAdapterType();

    /**
     * Provides access to the adapter's configuration.
     * 
     * @return the configuration
     */
    @Nonnull
    AdapterConfiguration getConfiguration();
}
