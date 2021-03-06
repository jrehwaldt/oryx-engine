package org.jodaengine.eventmanagement.adapter.configuration;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.adapter.incoming.IncomingPullAdapter;


// TODO pulling once here and the time itnerval somewhere else? this is bullshit...
/**
 * Configuration for {@link IncomingPullAdapter}.
 */
public interface PullAdapterConfiguration extends AdapterConfiguration {

    /**
     * Says whether the adapter needs to be pulled only one or continuously.
     * 
     * @return true, if the adapter should be pulled only once; false, if the adapter should be pulled continuously
     */
    boolean pullingOnce();

    /**
     * Provides a unique event name. Can be human readable, but cool if done so. Construct through configuration
     * parameters.
     * 
     * @return an unique adapter name
     */
    @Nonnull
    String getUniqueName();
}
