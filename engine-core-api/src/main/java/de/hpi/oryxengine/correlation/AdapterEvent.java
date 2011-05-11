package de.hpi.oryxengine.correlation;

import java.util.Date;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.adapter.AdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.EventType;

/**
 * A basic event, which is returned to the {@link EventManager} by adapter implementations.
 */
public interface AdapterEvent {

    /**
     * Gets the {@link AdapterConfiguration}.
     * 
     * @return the adapter's configuration
     */
    @Nonnull
    AdapterConfiguration getAdapterConfiguration();

    /**
     * Returns the event's creation time.
     * 
     * @return a timestamp
     */
    @Nonnull
    Date getTimestamp();

    /**
     * Return's the {@link EventType} .
     * 
     * @return the adapter's type
     */
    @Nonnull
    EventType getAdapterType();
}
