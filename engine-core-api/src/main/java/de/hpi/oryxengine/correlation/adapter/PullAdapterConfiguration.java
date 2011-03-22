package de.hpi.oryxengine.correlation.adapter;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

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
    @Nonnegative long getPullInterval();
    
    /**
     * Provides a unique adapter name. Must not be human readable,
     * but cool if done so. Construct through configuration parameters.
     * 
     * @return an unique adapter name
     */
    @Nonnull String getUniqueName();
}
