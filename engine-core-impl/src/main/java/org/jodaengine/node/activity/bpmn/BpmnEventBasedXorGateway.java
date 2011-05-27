package org.jodaengine.node.activity.bpmn;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.ServiceFactory;
import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.node.activity.AbstractCancelableActivity;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.process.token.Token;

/**
 * This {@link Activity} represents the event-based gateway used in the BPMN.
 */
public class BpmnEventBasedXorGateway extends AbstractCancelableActivity {

    private static final String REGISTERED_PROCESS_EVENT_PREFIX = "PROCESS_EVENTS-";

    @Override
    protected void executeIntern(Token token) {

        // TODO @Gerardo muss geändert werden keine ServiceFactory mehr - vielleicht im Token
        EventSubscriptionManager eventManager = ServiceFactory.getCorrelationService();

        List<ProcessIntermediateEvent> registeredIntermediateEvents = new ArrayList<ProcessIntermediateEvent>();

        for (Transition transition : token.getCurrentNode().getOutgoingTransitions()) {
            Node node = transition.getDestination();
            if (node.getActivityBehaviour() instanceof BpmnEventBasedGatewayEvent) {
                BpmnEventBasedGatewayEvent eventBasedGatewayEvent = (BpmnEventBasedGatewayEvent) node
                .getActivityBehaviour();

                ProcessIntermediateEvent processEvent = eventBasedGatewayEvent.createProcessIntermediateEvent(token);
                // Setting the node that has fired the event; the node is not that one the execution is currently
                // pointing at but rather the node that contained the event
                processEvent.setFireringNode(node);

                // Subscribing on the event
                eventManager.registerIntermediateEvent(processEvent);

                // Putting the intermediate Events in the queue for later reference
                registeredIntermediateEvents.add(processEvent);
            }
        }

        // Storing the list of intermediateProcessEvents as internal varibale for later reference
        token.setInternalVariable(internalVariableId(REGISTERED_PROCESS_EVENT_PREFIX, token),
            registeredIntermediateEvents);

        token.suspend();
    }

    @Override
    public synchronized void resume(Token token, Object resumeObject) {

        if (!(resumeObject instanceof ProcessIntermediateEvent)) {
            logger.debug("The resumeObject '{}' is not a ProcessIntermediateEvent.", resumeObject);
        }

        ProcessIntermediateEvent intermediateProcessEvent = (ProcessIntermediateEvent) resumeObject;

        // Maybe checking if it is in the list of registered events

        @SuppressWarnings("unchecked")
        List<ProcessIntermediateEvent> registeredIntermediateEvents = (List<ProcessIntermediateEvent>) token
        .getInternalVariable(internalVariableId(REGISTERED_PROCESS_EVENT_PREFIX, token));

        if (!registeredIntermediateEvents.remove(intermediateProcessEvent)) {
            // Then it means the element that should be deleted was not in the list of registered Events
            String errorMessage = "The event-based Xor Gateway was resumed by an event that it has not registered.";
            logger.error(errorMessage);
            throw new JodaEngineRuntimeException(errorMessage);
        }

        unsubscribingFrom(registeredIntermediateEvents);
        
        Node nodeToSkip = intermediateProcessEvent.getFireringNode();
        // Define the new starting point of the token; the new starting point is one of the nodes that comes right after that one
        token.setCurrentNode(nodeToSkip);
    }

    private void unsubscribingFrom(List<ProcessIntermediateEvent> registeredIntermediateEvents) {

        // Unsubscribing the other registered events; doing it as early as possible

        // TODO @Gerardo muss geändert werden keine ServiceFactory mehr - vielleicht im Token
        EventSubscriptionManager eventManager = ServiceFactory.getCorrelationService();
        for (ProcessIntermediateEvent registeredProcessEvent : registeredIntermediateEvents) {
            eventManager.unsubscribeFromIntermediateEvent(registeredProcessEvent);
        }
    }
}
