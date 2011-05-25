package org.jodaengine.node.activity.bpmn;

import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.process.token.Token;

/**
 * The interface can be attached to BpmnEventActivities like {@link BpmnIntermediateTimerActivity}. The interface also
 * declares that the eventActivity can also be attached to the {@link BpmnEventBasedGatewayEvent}.
 */
public interface BpmnEventBasedGatewayEvent {

    /**
     * Creates an specific {@link ProcessIntermediateEvent}.
     * 
     * @param token
     *            - the {@link Token} that is used in order to build the {@link ProcessIntermediateEvent}
     * @return a specific {@link ProcessIntermediateEvent}
     */
    ProcessIntermediateEvent createProcessIntermediateEvent(Token token);
}
