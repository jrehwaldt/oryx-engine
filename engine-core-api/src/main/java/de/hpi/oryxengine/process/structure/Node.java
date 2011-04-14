package de.hpi.oryxengine.process.structure;

import java.util.List;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;
import de.hpi.oryxengine.util.Identifiable;

/**
 * The Interface for Nodes. Nodes are hubs in the graph representation of a process.
 */
public interface Node extends Identifiable {

    /**
     * Gets the activity. The activity is the behavior of a node.
     * 
     * @return the activity blueprint that is used to instantiate the activity.
     */
    ActivityBlueprint getActivityBlueprint();

    /**
     * Sets the blueprint of the activity that is instantiated when a token reaches the node. The activity is the
     * behavior of a node.
     * 
     * @param blueprint
     *            the blueprint of the activity to use.
     */
    void setActivityBlueprint(ActivityBlueprint blueprint);

    /**
     * Sets the outgoing behaviour.
     * 
     * @param outgoingBehaviour
     *            the new outgoing behaviour
     */
    void setOutgoingBehaviour(OutgoingBehaviour outgoingBehaviour);

    /**
     * Sets the incoming behaviour.
     * 
     * @param incomingBehaviour
     *            the new incoming behaviour
     */
    void setIncomingBehaviour(IncomingBehaviour incomingBehaviour);

    /**
     * Gets the incoming behaviour.
     * 
     * @return the incoming behaviour
     */
    IncomingBehaviour getIncomingBehaviour();

    /**
     * Gets the outgoing behaviour.
     * 
     * @return the outgoing behaviour
     */
    OutgoingBehaviour getOutgoingBehaviour();

    /**
     * Next.
     * 
     * @return the next Node(s) depending on the node (normal nodes vs. Splits which have multiple next nodes).
     */
    List<Transition> getOutgoingTransitions();

    /**
     * Gets the incoming transitions.
     * 
     * @return the incoming transitions
     */
    List<Transition> getIncomingTransitions();

    /**
     * Describes a new outgoing edge to the given node.
     * 
     * @param node
     *            the node to which a new transition shall be established
     */
    void transitionTo(Node node);

    /**
     * Transition to with condition.
     * 
     * @param node
     *            the destination
     * @param c
     *            the condition
     */
    void transitionToWithCondition(Node node, Condition c);

}
