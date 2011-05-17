package org.jodaengine.node.activity.bpmn;

import javax.annotation.Nonnull;

import org.jodaengine.ServiceFactory;
import org.jodaengine.eventmanagement.EventManager;
import org.jodaengine.eventmanagement.adapter.TimedConfiguration;
import org.jodaengine.eventmanagement.adapter.TimerConfigurationImpl;
import org.jodaengine.eventmanagement.registration.TimerEventImpl;
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

        EventManager correlationService = ServiceFactory.getCorrelationService();
        TimedConfiguration conf = new TimerConfigurationImpl(this.time);
        this.jobCompleteName = correlationService.registerIntermediateEvent(new TimerEventImpl(conf, token));
        token.suspend();
    }

    @Override
    public void cancel(Token executingToken) {

        TimingManager timer = ServiceFactory.getCorrelationService().getTimer();
        timer.unregisterJob(this.jobCompleteName);
    }

}
