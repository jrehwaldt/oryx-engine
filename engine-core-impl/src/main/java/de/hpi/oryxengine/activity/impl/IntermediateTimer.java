package de.hpi.oryxengine.activity.impl;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.AbstractDeferredActivity;
import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.correlation.adapter.TimedAdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.TimerConfigurationImpl;
import de.hpi.oryxengine.correlation.registration.TimerEventImpl;
import de.hpi.oryxengine.process.token.Token;

/**
 * The actvity IntermediateTimer is used to wait a specific amount of time before execution is continued.
 */
public class IntermediateTimer extends AbstractDeferredActivity {
    private long time;

    /**
     * Instantiates a new intermediate timer with a given time.
     *
     * @param time the time in ms
     */
    public IntermediateTimer(long time) {
        this.time = time;
    }
    
    @Override
    protected void executeIntern(@Nonnull Token token) {
        
        CorrelationManager correlationService = ServiceFactory.getCorrelationService();
        TimedAdapterConfiguration conf = new TimerConfigurationImpl(null, this.time);
        correlationService.registerIntermediateEvent(new TimerEventImpl(conf, token));
        token.suspend();
    }


}
