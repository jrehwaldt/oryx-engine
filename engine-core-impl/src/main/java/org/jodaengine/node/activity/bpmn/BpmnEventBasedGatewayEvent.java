package org.jodaengine.node.activity.bpmn;

import org.jodaengine.eventmanagement.processevent.incoming.intermediate.AbstractIncomingIntermediateProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IncomingIntermediateProcessEvent;
import org.jodaengine.process.token.Token;

/**
 * The interface can be attached to BpmnEventActivities like {@link BpmnTimerIntermediateEventActivity}. The interface
 * also
 * declares that the eventActivity can also be attached to the {@link BpmnEventBasedGatewayEvent}.
 */
public interface BpmnEventBasedGatewayEvent {

    /**
     * Creates an specific {@link IncomingIntermediateProcessEvent}.
     * 
     * @param token
     *            - the {@link Token} that is used in order to build the {@link IncomingIntermediateProcessEvent}
     * @return a specific {@link IncomingIntermediateProcessEvent}
     */
    AbstractIncomingIntermediateProcessEvent createProcessIntermediateEvent(Token token);
}
