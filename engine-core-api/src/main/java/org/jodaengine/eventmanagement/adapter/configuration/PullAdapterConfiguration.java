package org.jodaengine.eventmanagement.adapter.configuration;

import org.jodaengine.eventmanagement.adapter.InboundPullAdapter;

/**
 * Configuration for {@link InboundPullAdapter}.
 */
public interface PullAdapterConfiguration extends AdapterConfiguration {

    /**
     * Says whether the adapter needs to be pulled only one or continuously.
     * 
     * @return true, if the adapter should be pulled only once; false, if the adapter should be pulled continuously
     */
    boolean pullingOnce();
}
