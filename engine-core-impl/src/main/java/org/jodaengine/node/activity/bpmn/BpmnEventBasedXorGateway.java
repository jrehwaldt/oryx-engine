package org.jodaengine.node.activity.bpmn;

import org.jodaengine.ServiceFactory;
import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.node.activity.AbstractCancelableActivity;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.process.token.Token;

/**
 * This {@link Activity} represents the event-based gateway used in the BPMN.
 */
public class BpmnEventBasedXorGateway extends AbstractCancelableActivity {

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
                processEvent.setFireringNode(node);
                eventManager.registerIntermediateEvent(processEvent);
            }
        }

        // eine Map bestehend aus dem internalVariableId(PROCESS_EVENT_PREFIX, token) + processEvent.hashcode()
        // final String itemContextVariableId = internalVariableId(PROCESS_EVENT_PREFIX, token, "");
        // context.setInternalVariable(itemContextVariableId, processEvent);

        token.suspend();
    }

    @Override
    public synchronized void resume(Token token, Object resumeObject) {

        if (!(resumeObject instanceof ProcessIntermediateEvent)) {
            logger.debug("The resumeObject '{}' is not a ProcessIntermediateEvent.", resumeObject);
        }

        ProcessIntermediateEvent intermediateProcessEvent = (ProcessIntermediateEvent) resumeObject; 
        Node nodeToSkip = intermediateProcessEvent.getFireringNode();
        
        token.setCurrentNode(nodeToSkip);
    }
}
