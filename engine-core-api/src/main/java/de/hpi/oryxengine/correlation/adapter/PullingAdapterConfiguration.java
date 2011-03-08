package de.hpi.oryxengine.correlation.adapter;

/**
 * Configuration for {@link PullingInboundAdapter}.
 */
public interface PullingAdapterConfiguration
extends AdapterConfiguration {
    
    /**
     * Returns the adapter's pull interval in ms.
     * 
     * @return the pull interval in ms
     */
    long getInterval();
}
