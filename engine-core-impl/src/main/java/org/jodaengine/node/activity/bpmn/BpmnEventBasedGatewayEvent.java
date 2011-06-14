package org.jodaengine.node.activity.bpmn;

import org.jodaengine.eventmanagement.processevent.incoming.TriggeringBehaviour;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IncomingIntermediateProcessEvent;
import org.jodaengine.eventmanagement.subscription.IncomingProcessEvent;
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
    IncomingIntermediateProcessEvent createProcessIntermediateEvent(Token token);

    /**
     * Creates an specific {@link IncomingIntermediateProcessEvent}.
     * 
     * @param token
     *            - the {@link Token} that is used in order to build the {@link IncomingIntermediateProcessEvent}
     * @param eventGroup
     *            - if the {@link IncomingIntermediateProcessEvent} is connected to another {@link IncomingProcessEvent} than a
     *            {@link AbstractProcessIntermediateEventGroup} can be used to specify that connection
     * @return a specific {@link IncomingIntermediateProcessEvent}
     */
    IncomingIntermediateProcessEvent createProcessIntermediateEventForEventGroup(Token token, TriggeringBehaviour eventGroup);
}
