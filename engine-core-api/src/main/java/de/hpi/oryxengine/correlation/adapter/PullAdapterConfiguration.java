package de.hpi.oryxengine.correlation.adapter;

import de.hpi.oryxengine.correlation.EventType;

/**
 * Configuration for {@link InboundPullAdapter}.
 */
public interface PullAdapterConfiguration
extends AdapterConfiguration {
    
    /**
     * Returns the adapter's pull interval in ms.
     * 
     * @return the pull interval in ms
     */
    long getPullInterval();

    /**
     * Provides a unique adapter name. Must not be human readable,
     * but cool if done so. Construct through configuration parameters.
     * 
     * @return an unique adapter name
     */
    String getUniqueName();
    
    /**
     * Returns the event's type.
     * 
     * @return event type
     */
    EventType getEventType();
    
    /**
     * Provides the adapter's implementation class.
     * 
     * @return the adapter's implementation class
     */
    Class<? extends InboundPullAdapter> getAdapterClass();
}
