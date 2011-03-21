package de.hpi.oryxengine.correlation.adapter;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.exception.OryxEngineException;

/**
 * This interface should be implemented by {@link EventManager} adapters,
 * which allow incoming pull communication. This includes,
 * e.g. pop3 email receiving adapter.
 */
public interface InboundPullAdapter
extends InboundAdapter {
    
    /**
     * This method is invoked whenever pulling is requested.
     * 
     * @throws OryxEngineException thrown if pulling fails
     */
    void pull()
    throws OryxEngineException;
    
    @Override
    @Nonnull PullAdapterConfiguration getConfiguration();
}
