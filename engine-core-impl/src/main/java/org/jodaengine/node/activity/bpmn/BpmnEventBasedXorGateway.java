package org.jodaengine.node.activity.bpmn;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IncomingIntermediateProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.processeventgroup.AbstractIntermediateProcessEventGroup;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.processeventgroup.ExclusiveProcessEventGroup;
import org.jodaengine.node.activity.AbstractCancelableActivity;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.AbstractToken;
import org.jodaengine.process.token.Token;

/**
 * This {@link BpmnEventBasedXorGateway Activity} represents the event-based gateway used in the BPMN.
 */
public class BpmnEventBasedXorGateway extends AbstractCancelableActivity {

    private static final String REGISTERED_PROCESS_EVENT_PREFIX = "PROCESS_EVENTS-";

    @Override
    protected void executeIntern(AbstractToken token) {

        EventSubscriptionManager eventManager = token.getEventManagerService();

        List<IncomingIntermediateProcessEvent> registeredIntermediateEvents = new ArrayList<IncomingIntermediateProcessEvent>();

        AbstractIntermediateProcessEventGroup eventXorGroup = new ExclusiveProcessEventGroup(token);

        for (ControlFlow controlFlow : token.getCurrentNode().getOutgoingControlFlows()) {
            Node node = controlFlow.getDestination();

            // This node needs to trigger create processIntermediateEvents for the node that are attached to him
            // That's why we need to check whether the activity of the next node allows the creation of
            // processIntermediateEvents
            if (node.getActivityBehaviour() instanceof BpmnEventBasedGatewayEvent) {
                BpmnEventBasedGatewayEvent eventBasedGatewayEvent = (BpmnEventBasedGatewayEvent) node
                .getActivityBehaviour();

                // Creating a processIntermediateEvent
                IncomingIntermediateProcessEvent processEvent = eventBasedGatewayEvent
                .createProcessIntermediateEventForEventGroup(token, eventXorGroup);
                // Setting the node that has fired the event; the node is not that one the execution is currently
                // pointing at but rather the node that contained the event
                processEvent.setFireringNode(node);

                eventXorGroup.add(processEvent);

                // Subscribing on the event
                eventManager.registerIncomingIntermediateEvent(processEvent);

                // Putting the intermediate Events in the queue for later reference
                registeredIntermediateEvents.add(processEvent);
            } else {

                String errorMessage = "The node '" + node.toString() + "' with the activity '"
                    + node.getActivityBehaviour().toString()
                    + "' cannot create a ProcessIntermediateEvent. Needs to be of the type BpmnEventBasedGatewayEvent.";
                logAndThrowError(errorMessage);
            }
        }

        // Storing the list of registered intermediateProcessEvents as internal variable for later reference
        // (see canceling)
        token.setInternalVariable(internalVariableId(REGISTERED_PROCESS_EVENT_PREFIX, token),
            registeredIntermediateEvents);

        token.suspend();
    }

    @Override
    public void resume(Token token, Object resumeObject) {

        if (!(resumeObject instanceof IncomingIntermediateProcessEvent)) {
            logger.debug("The resumeObject '{}' is not a ProcessIntermediateEvent.", resumeObject);
        }

        // Each processIntermediateEvent knows which node is waiting for the event to fire
        IncomingIntermediateProcessEvent intermediateProcessEvent = (IncomingIntermediateProcessEvent) resumeObject;
        Node nodeToSkip = intermediateProcessEvent.getFireringNode();

        // Define where the token continue his execution
        // In this case it starts at the outgoingbehavior of the node that represents the intermediateEvent
        token.setCurrentNode(nodeToSkip);

        // The ProcessEventGroup already takes care of unsubscribing from the other event
    }

    @Override
    public void cancelIntern(AbstractToken executingToken) {

        // Extracting the variable
        @SuppressWarnings("unchecked")
        List<IncomingIntermediateProcessEvent> registeredIntermediateEvents = (List<IncomingIntermediateProcessEvent>) executingToken
        .getInternalVariable(internalVariableId(REGISTERED_PROCESS_EVENT_PREFIX, executingToken));

        // Unsubscribing from all processIntermediateEvents
        EventSubscriptionManager eventManager = executingToken.getEventManagerService();
        for (IncomingIntermediateProcessEvent registeredProcessEvent : registeredIntermediateEvents) {
            eventManager.unsubscribeFromIncomingIntermediateEvent(registeredProcessEvent);
        }
    }
}
