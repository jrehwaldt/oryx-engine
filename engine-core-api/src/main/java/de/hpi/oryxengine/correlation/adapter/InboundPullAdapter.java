package de.hpi.oryxengine.correlation.adapter;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.EventManager;
import de.hpi.oryxengine.exception.DalmatinaException;

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
     * @throws DalmatinaException thrown if pulling fails
     */
    void pull()
    throws DalmatinaException;
    
    @Override
    @Nonnull PullAdapterConfiguration getConfiguration();
}
