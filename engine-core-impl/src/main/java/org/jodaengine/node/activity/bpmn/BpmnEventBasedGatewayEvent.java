package org.jodaengine.node.activity.bpmn;

import org.jodaengine.eventmanagement.subscription.ProcessEvent;
import org.jodaengine.eventmanagement.subscription.ProcessEventGroup;
import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.process.token.Token;

/**
 * The interface can be attached to BpmnEventActivities like {@link BpmnTimerIntermediateEventActivity}. The interface
 * also
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

    /**
     * Creates an specific {@link ProcessIntermediateEvent}.
     * 
     * @param token
     *            - the {@link Token} that is used in order to build the {@link ProcessIntermediateEvent}
     * @param eventGroup
     *            - if the {@link ProcessIntermediateEvent} is connected to another {@link ProcessEvent} than a
     *            {@link ProcessEventGroup} can be used to specify that connection
     * @return a specific {@link ProcessIntermediateEvent}
     */
    ProcessIntermediateEvent createProcessIntermediateEventInEventGroup(Token token, ProcessEventGroup eventGroup);
}
