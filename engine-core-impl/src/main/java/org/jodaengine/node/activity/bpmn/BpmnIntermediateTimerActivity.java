package org.jodaengine.node.activity.bpmn;

import javax.annotation.Nonnull;

import org.jodaengine.ServiceFactory;
import org.jodaengine.eventmanagement.EventManager;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.timer.TimerAdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.eventmanagement.subscription.TimerEventImpl;
import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.token.Token;

/**
 * The actvity IntermediateTimer is used to wait a specific amount of time before execution is continued.
 */
public class BpmnIntermediateTimerActivity extends AbstractActivity {

    private long time;

    private static final String PROCESS_EVENT_PREFIX = "PROCESS_EVENT";

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
        ProcessIntermediateEvent processEvent = new TimerEventImpl(conf, token);

        eventManager.registerIntermediateEvent(processEvent);

        ProcessInstanceContext context = token.getInstance().getContext();

        // the name should be unique, as the token can only work on one activity at a time.
        final String itemContextVariableId = internalVariableId(PROCESS_EVENT_PREFIX, token);
        context.setInternalVariable(itemContextVariableId, processEvent);

        token.suspend();
    }

    private static String internalVariableId(String prefix, Token token) {

        return PROCESS_EVENT_PREFIX + "-" + token.getID() + "-" + token.getCurrentNode().getID();
    }

    @Override
    public void cancel(Token executingToken) {

        ProcessInstanceContext context = executingToken.getInstance().getContext();
        final String itemContextVariableId = internalVariableId(PROCESS_EVENT_PREFIX, executingToken);

        ProcessIntermediateEvent intermediateEvent = (ProcessIntermediateEvent) context
        .getInternalVariable(itemContextVariableId);

        EventManager eventManager = ServiceFactory.getCorrelationService();
        eventManager.unsubscribeFromIntermediateEvent(intermediateEvent);
        // TimingManager timer = ServiceFactory.getCorrelationService().getTimer();
        // timer.unregisterJob(this.jobCompleteName);
    }

}
