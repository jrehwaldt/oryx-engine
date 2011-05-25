package org.jodaengine.node.activity.bpmn;

import org.jodaengine.ServiceFactory;
import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.process.token.Token;

/**
 * This {@link Activity} represents the event-based gateway used in the BPMN.
 */
public class BpmnEventBasedXorGateway extends AbstractActivity {

    @Override
    protected void executeIntern(Token token) {

        // TODO @Gerardo muss geändert werden keine ServiceFactory mehr
        EventSubscriptionManager eventManager = ServiceFactory.getCorrelationService();

        for (Transition transition : token.getCurrentNode().getOutgoingTransitions()) {
            Node node = transition.getDestination();
            if (node.getActivityBehaviour() instanceof BpmnEventBasedGatewayEvent) {
                BpmnEventBasedGatewayEvent eventBasedGatewayEvent = (BpmnEventBasedGatewayEvent) node
                .getActivityBehaviour();

                ProcessIntermediateEvent processEvent = eventBasedGatewayEvent.createProcessIntermediateEvent(token);
                eventManager.registerIntermediateEvent(processEvent);
            }
        }

        // ProcessInstanceContext context = token.getInstance().getContext();
        //
        // // the name should be unique, as the token can only work on one activity at a time.
        // final String itemContextVariableId = internalVariableId(PROCESS_EVENT_PREFIX, token);
        // context.setInternalVariable(itemContextVariableId, processEvent);

        token.suspend();
    }

}
