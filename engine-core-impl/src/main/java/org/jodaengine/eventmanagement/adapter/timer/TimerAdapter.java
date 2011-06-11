package org.jodaengine.eventmanagement.adapter.timer;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.adapter.AbstractCorrelatingEventAdapter;
import org.jodaengine.eventmanagement.adapter.incoming.IncomingPullAdapter;
import org.jodaengine.exception.JodaEngineException;

/**
 * Representing an adapter for generating TimeEvent.
 */
public class TimerAdapter extends AbstractCorrelatingEventAdapter<TimerAdapterConfiguration> implements
IncomingPullAdapter {

    /**
     * Default constructor.
     * 
     * @param timerConfiguration
     *            the {@link TimerAdapterConfiguration configuration} for this {@link TimerAdapter}
     */
    public TimerAdapter(@Nonnull TimerAdapterConfiguration timerConfiguration) {

        super(timerConfiguration);
    }

    @Override
    public void pull()
    throws JodaEngineException {

        AdapterEvent event = new TimerAdapterEvent(configuration);
        correlate(event);
    }
}
