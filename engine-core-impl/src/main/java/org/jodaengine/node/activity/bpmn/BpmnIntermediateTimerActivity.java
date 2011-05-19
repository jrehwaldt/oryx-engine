package org.jodaengine.node.activity.bpmn;

import javax.annotation.Nonnull;

import org.jodaengine.ServiceFactory;
import org.jodaengine.eventmanagement.EventManager;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.timer.TimerAdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.TimerEventImpl;
import org.jodaengine.eventmanagement.timing.TimingManager;
import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.token.Token;


/**
 * The actvity IntermediateTimer is used to wait a specific amount of time before execution is continued.
 */
public class BpmnIntermediateTimerActivity extends AbstractActivity {
    private long time;
    private String jobCompleteName;

    /**
     * Instantiates a new intermediate timer with a given time.
     * 
     * @param time
     *            - the time (in ms) to wait for
     */
    public BpmnIntermediateTimerActivity(long time) {

        this.time = time;
    }

    @Override
    protected void executeIntern(@Nonnull Token token) {

        // TODO @Gerardo muss geändert werden keine ServiceFactory mehr
        EventManager eventManager = ServiceFactory.getCorrelationService();
        AdapterConfiguration conf = new TimerAdapterConfiguration(this.time);
        
        eventManager.registerIntermediateEvent(new TimerEventImpl(conf, token));
        token.suspend();
    }

    @Override
    public void cancel(Token executingToken) {

        TimingManager timer = ServiceFactory.getCorrelationService().getTimer();
        timer.unregisterJob(this.jobCompleteName);
    }

}
