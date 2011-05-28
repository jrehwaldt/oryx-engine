package org.jodaengine.node.activity.bpmn;

import javax.annotation.Nonnull;

import org.jodaengine.ServiceFactory;
import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.eventmanagement.subscription.processevent.intermediate.TimerProcessIntermediateEvent;
import org.jodaengine.node.activity.AbstractCancelableActivity;
import org.jodaengine.process.token.Token;

/**
 * The {@link BpmnIntermediateTimerActivity IntermediateTimer} is used to wait a specific amount of time before
 * execution is continued.
 */
// TODO @Gerardo: Name ändern nach BpmnTimerIntermediateActivity
public class BpmnIntermediateTimerActivity extends AbstractCancelableActivity implements BpmnEventBasedGatewayEvent {

    private long time;

    private static final String PROCESS_EVENT_PREFIX = "PROCESS_EVENT-";

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

        // TODO @Gerardo muss geändert werden keine ServiceFactory mehr; vielleicht alle coreservices ins token
        EventSubscriptionManager eventManager = ServiceFactory.getCorrelationService();

        ProcessIntermediateEvent processEvent = createProcessIntermediateEvent(token);

        eventManager.registerIntermediateEvent(processEvent);

        // the name should be unique, as the token can only work on one activity at a time.
        final String itemContextVariableId = internalVariableId(PROCESS_EVENT_PREFIX, token);
        token.setInternalVariable(itemContextVariableId, processEvent);

        token.suspend();
    }

    @Override
    public ProcessIntermediateEvent createProcessIntermediateEvent(Token token) {

        return new TimerProcessIntermediateEvent(time, token);
    }

    @Override
    public void cancel(Token executingToken) {

        final String itemContextVariableId = internalVariableId(PROCESS_EVENT_PREFIX, executingToken);

        ProcessIntermediateEvent intermediateEvent = (ProcessIntermediateEvent) executingToken
        .getInternalVariable(itemContextVariableId);

        EventSubscriptionManager eventManager = ServiceFactory.getCorrelationService();
        eventManager.unsubscribeFromIntermediateEvent(intermediateEvent);
    }
}
