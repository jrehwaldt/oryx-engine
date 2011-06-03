package org.jodaengine.node.activity.bpmn;

import javax.annotation.Nonnull;

import org.jodaengine.ServiceFactory;
import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.eventmanagement.subscription.ProcessEventGroup;
import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.eventmanagement.subscription.processevent.intermediate.TimerProcessIntermediateEvent;
import org.jodaengine.node.activity.AbstractCancelableActivity;
import org.jodaengine.process.token.Token;

/**
 * The {@link BpmnTimerIntermediateEventActivity IntermediateTimer} is used to wait a specific amount of time before
 * execution is continued.
 */
public class BpmnTimerIntermediateEventActivity extends AbstractCancelableActivity implements
BpmnEventBasedGatewayEvent {

    private long time;

    private static final String PROCESS_EVENT_PREFIX = "PROCESS_EVENT-";

    /**
     * Instantiates a new intermediate timer with a given time.
     * 
     * @param time
     *            - the time (in ms) to wait for
     */
    public BpmnTimerIntermediateEventActivity(long time) {

        this.time = time;
    }

    @Override
    protected void executeIntern(@Nonnull Token token) {

        // TODO @Gerardo muss geändert werden keine ServiceFactory mehr; vielleicht alle coreservices ins token
        EventSubscriptionManager eventManager = ServiceFactory.getCorrelationService();

        ProcessIntermediateEvent processEvent = createProcessIntermediateEvent(token);

        eventManager.registerIntermediateEvent(processEvent);

        // the name should be unique, as the token can only work on one activity at a time.
        token.setInternalVariable(internalVariableId(PROCESS_EVENT_PREFIX, token), processEvent);

        token.suspend();
    }

    @Override
    public void cancel(Token executingToken) {

        ProcessIntermediateEvent intermediateEvent = (ProcessIntermediateEvent) executingToken
        .getInternalVariable(internalVariableId(PROCESS_EVENT_PREFIX, executingToken));

        EventSubscriptionManager eventManager = ServiceFactory.getCorrelationService();
        eventManager.unsubscribeFromIntermediateEvent(intermediateEvent);
    }

    //
    // ==== Interface that represents that this event can also be used by other nodes like event-based Gateway ====
    //
    @Override
    public ProcessIntermediateEvent createProcessIntermediateEvent(Token token) {

        return new TimerProcessIntermediateEvent(time, token);
    }

    @Override
    public ProcessIntermediateEvent createProcessIntermediateEventInEventGroup(Token token, ProcessEventGroup eventGroup) {

        return new TimerProcessIntermediateEvent(time, token, eventGroup);
    }
}
