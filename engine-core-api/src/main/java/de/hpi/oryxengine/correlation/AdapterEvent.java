package de.hpi.oryxengine.correlation;

import java.util.Date;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.adapter.AdapterConfiguration;

/**
 * A basic event, which is returned to the {@link EventManager} by adapter implementations.
 */
public interface AdapterEvent {
    
    /**
     * Gets the adapter's configuration.
     * 
     * @return the adapter's configuration
     */
    @Nonnull AdapterConfiguration getAdapterConfiguration();
    
    /**
     * Returns the event's creation time.
     * 
     * @return a timestamp
     */
    @Nonnull Date getTimestamp();
}
