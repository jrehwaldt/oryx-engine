package org.jodaengine.eventmanagement.adapter;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.EventManager;
import org.jodaengine.eventmanagement.adapter.configuration.PullAdapterConfiguration;
import org.jodaengine.exception.JodaEngineException;

/**
 * This interface should be implemented by {@link EventManager} adapters, which allow incoming pull communication. This
 * includes, e.g. pop3 email receiving adapter.
 */
public interface InboundPullAdapter extends InboundAdapter {

    /**
     * This method is invoked whenever pulling is requested.
     * 
     * @throws JodaEngineException
     *             thrown if pulling fails
     */
    void pull()
    throws JodaEngineException;

    @Override
    @Nonnull
    PullAdapterConfiguration getConfiguration();
}
