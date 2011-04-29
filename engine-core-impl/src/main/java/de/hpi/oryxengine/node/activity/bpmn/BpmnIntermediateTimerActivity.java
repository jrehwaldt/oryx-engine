package de.hpi.oryxengine.node.activity.bpmn;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.correlation.adapter.TimedConfiguration;
import de.hpi.oryxengine.correlation.adapter.TimerConfigurationImpl;
import de.hpi.oryxengine.correlation.registration.TimerEventImpl;
import de.hpi.oryxengine.correlation.timing.TimingManager;
import de.hpi.oryxengine.node.activity.AbstractActivity;
import de.hpi.oryxengine.process.token.Token;

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
