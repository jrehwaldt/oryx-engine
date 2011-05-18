package org.jodaengine.eventmanagement.adapter.timer;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.CorrelationManager;
import org.jodaengine.eventmanagement.adapter.AbstractEventAdapter;
import org.jodaengine.eventmanagement.adapter.InboundPullAdapter;
import org.jodaengine.exception.JodaEngineException;

public class TimerAdapter extends AbstractEventAdapter<TimerAdapterConfiguration> implements InboundPullAdapter {

    /**
     * Default constructor.
     * 
     * @param correlationManager - 
     *            the {@link CorrelationManager} manager in order to correlate
     * @param timerConfiguration
     *            the {@link TimerAdapterConfiguration configuration} for this {@link TimerAdapter}
     */
    public TimerAdapter(@Nonnull CorrelationManager correlationManager, @Nonnull TimerAdapterConfiguration timerConfiguration) {

        super(correlationManager, timerConfiguration);
    }

    @Override
    public void pull()
    throws JodaEngineException {

        AdapterEvent event = new TimerAdapterEvent(configuration);
        correlate(event);
    }
}
