package de.hpi.oryxengine.correlation.adapter;

// TODO: Auto-generated Javadoc
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
