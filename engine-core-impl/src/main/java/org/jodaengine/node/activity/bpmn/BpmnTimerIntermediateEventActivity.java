package org.jodaengine.node.activity.bpmn;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.eventmanagement.processevent.incoming.TriggeringBehaviour;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IncomingIntermediateProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.TimerProcessIntermediateEvent;
import org.jodaengine.node.activity.AbstractCancelableActivity;
import org.jodaengine.process.token.AbstractToken;
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
    protected void executeIntern(@Nonnull AbstractToken token) {

        EventSubscriptionManager eventManager = token.getCorrelationService();

        IncomingIntermediateProcessEvent processEvent = createProcessIntermediateEvent(token);

        eventManager.registerIntermediateEvent(processEvent);

        // the name should be unique, as the token can only work on one activity at a time.
        token.setInternalVariable(internalVariableId(PROCESS_EVENT_PREFIX, token), processEvent);

        token.suspend();
    }

    @Override
    public void resume(Token token, Object resumeObject) {

        super.resume(token, resumeObject);

        // Doing nothing because the fact the this method already means that the TimerEvent occurred
    }

    @Override
    public void cancelIntern(AbstractToken executingToken) {

        IncomingIntermediateProcessEvent intermediateEvent = (IncomingIntermediateProcessEvent) executingToken
        .getInternalVariable(internalVariableId(PROCESS_EVENT_PREFIX, executingToken));

        EventSubscriptionManager eventManager = executingToken.getCorrelationService();
        eventManager.unsubscribeFromIntermediateEvent(intermediateEvent);
    }

    //
    // ==== Interface that represents that this event can also be used by other nodes like event-based Gateway ====
    //
    @Override
    public IncomingIntermediateProcessEvent createProcessIntermediateEvent(Token token) {

        return new TimerProcessIntermediateEvent(time, token);
    }

    @Override
    public IncomingIntermediateProcessEvent createProcessIntermediateEventForEventGroup(Token token,
                                                                                TriggeringBehaviour eventGroup) {

        return new TimerProcessIntermediateEvent(time, token, eventGroup);
    }
}
