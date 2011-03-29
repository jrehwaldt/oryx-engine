package de.hpi.oryxengine.activity.impl;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.allocation.TaskDistribution;
import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.correlation.registration.TimerEventImpl;
import de.hpi.oryxengine.process.token.Token;

/**
 * The actvity IntermediateTimer is used to wait a specific amount of time before execution is continued.
 */
public class IntermediateTimer extends AbstractActivity {
    private long time;

    public IntermediateTimer(int time) {
        this.time = time;
    }
    
    @Override
    protected void executeIntern(@Nonnull Token token) {
        
        CorrelationManager correlationService = ServiceFactory.getCorrelationService(token.getNavigator());
        
        correlationService.registerIntermediateEvent(new TimerEventImpl(config, tokenId));
        
        token.suspend();
    }
    
    @Override
    public void signal(Token token) {

        // BlaBla
    }


}
