package org.jodaengine.node.activity.bpmn;

import javax.annotation.Nonnull;

import org.jodaengine.ServiceFactory;
import org.jodaengine.correlation.CorrelationManager;
import org.jodaengine.correlation.adapter.TimedConfiguration;
import org.jodaengine.correlation.adapter.TimerConfigurationImpl;
import org.jodaengine.correlation.registration.TimerEventImpl;
import org.jodaengine.correlation.timing.TimingManager;
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

        CorrelationManager correlationService = ServiceFactory.getCorrelationService();
        TimedConfiguration conf = new TimerConfigurationImpl(this.time);
        this.jobCompleteName = correlationService.registerIntermediateEvent(new TimerEventImpl(conf, token));
        token.suspend();
    }

    @Override
    public void cancel() {

        TimingManager timer = ServiceFactory.getCorrelationService().getTimer();
        timer.unregisterJob(this.jobCompleteName);
    }

}
