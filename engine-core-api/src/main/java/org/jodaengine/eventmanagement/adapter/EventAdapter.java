package org.jodaengine.eventmanagement.adapter;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;

/**
 * General {@link EventManagerService} adapter.
 */
public interface EventAdapter {
    /**
     * Returns the adapter's type.
     * 
     * @return the adapter's type
     */
    // TODO @EVENTTEAM is this really an adapter type? its more the event type - further more should it contain Incoming/Outgoing?
    @Nonnull
    EventType getAdapterType();

    /**
     * Provides access to the adapter's configuration.
     * 
     * @return the configuration
     */
    @Nonnull
    AdapterConfiguration getConfiguration();
    
    @Override
    boolean equals(Object eventAdapter);
    
    @Override
    int hashCode();
}
