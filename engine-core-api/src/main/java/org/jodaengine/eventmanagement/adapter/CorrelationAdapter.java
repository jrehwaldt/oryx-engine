package org.jodaengine.eventmanagement.adapter;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.EventManager;

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
