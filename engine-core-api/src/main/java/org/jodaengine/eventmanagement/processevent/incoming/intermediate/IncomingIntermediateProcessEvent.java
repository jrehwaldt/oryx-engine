package org.jodaengine.eventmanagement.processevent.incoming.intermediate;

import org.jodaengine.eventmanagement.subscription.IncomingProcessEvent;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;

/**
 * The Interface IntermediateEvent. An intermediate event, in contrast to the startEvent will not trigger the
 * instantiation of a new process, but a notification to a registered process instance will be sent.
 * 
 */
public interface IncomingIntermediateProcessEvent extends IncomingProcessEvent {

    /**
     * Gets the assigned token that has be notified if timer is complete.
     * 
     * @return the definition id
     */
    Token getToken();

    /**
     * Receives the {@link Node} that fired the event.
     * @return the {@link Node} that fired the event
     */
    Node getFireringNode();

    /**
     * Set the {@link Node} that fires the event.
     *
     * @param node the new firering node
     */
    void setFireringNode(Node node);
}
