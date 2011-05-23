package org.jodaengine.process.structure;

import java.util.List;
import java.util.UUID;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.incomingbehaviour.IncomingBehaviour;
import org.jodaengine.node.outgoingbehaviour.OutgoingBehaviour;
import org.jodaengine.process.token.Token;
import org.jodaengine.util.Attributable;
import org.jodaengine.util.Identifiable;

/**
 * The Interface for Nodes. Nodes are hubs in the graph representation of a process.
 *
 * @param <T> the generic type
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public interface Node<T extends Token<?>> extends Identifiable<UUID>, Attributable {

    /**
     * Gets the activity. The activity is the behavior of a node.
     * 
     * @return the {@link Activity} of this node
     */
    @JsonProperty
    Activity getActivityBehaviour();

    /**
     * Gets the incoming behaviour.
     * 
     * @return the incoming behaviour
     */
    @JsonIgnore
    IncomingBehaviour<T> getIncomingBehaviour();

    /**
     * Gets the outgoing behaviour.
     * 
     * @return the outgoing behaviour
     */
    @JsonIgnore
    OutgoingBehaviour getOutgoingBehaviour();

    /**
     * Next.
     * 
     * @return the next Node(s) depending on the node (normal nodes vs. Splits which have multiple next nodes).
     */
    @JsonIgnore
    List<Transition<T>> getOutgoingTransitions();

    /**
     * Gets the incoming transitions.
     * 
     * @return the incoming transitions
     */
    @JsonIgnore
    List<Transition<T>> getIncomingTransitions();

    /**
     * Describes a new outgoing edge to the given node.
     * 
     * @param node
     *            the node to which a new transition shall be established
     * @return the created transition
     */
    Transition<T> transitionTo(Node<T> node);

    /**
     * Transition to with condition.
     * 
     * @param node
     *            the destination
     * @param c
     *            the condition
     * @return the created transition
     */
    Transition<T> transitionToWithCondition(Node<T> node, Condition c);

}
