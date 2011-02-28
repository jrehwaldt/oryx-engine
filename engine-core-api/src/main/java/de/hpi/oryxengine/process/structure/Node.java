package de.hpi.oryxengine.process.structure;

import java.util.List;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.routing.behaviour.RoutingBehaviour;

/**
 * The Interface for Nodes. Nodes are hubs in the graph representation of a process.
 */
public interface Node {

    /**
     * Gets the activity. The activity is the behavior of a node.
     * 
     * @return the activity
     */
    Activity getActivity();

    /**
     * Sets the activity. The activity is the behavior of a node.
     * 
     * @param activity
     *            the new activity
     */
    void setActivity(Activity activity);

    /**
     * Gets the routing behavior.
     * 
     * @return the routing behavior
     */
    RoutingBehaviour getRoutingBehaviour();

    /**
     * Sets the routing behavior.
     * 
     * @param behaviour
     *            the new routing behavior
     */
    void setRoutingBehaviour(RoutingBehaviour behaviour);

    /**
     * Next.
     * 
     * @return the next Node(s) depending on the node (normal nodes vs. Splits which have multiple next nodes).
     */
    List<Transition> getTransitions();

    /**
     * Describes a new outgoing edge to the given node.
     * 
     * @param node
     *            the node to which a new transition shall be established
     */
    void transitionTo(Node node);

    /**
     * Gets the id of the node.
     * 
     * @return the id
     */
    String getId();

    /**
     * Sets the id of the node.
     * 
     * @param id
     *            the new id
     */
    void setId(String id);

    /**
     * Execute some sort of behaviour.
     *
     * @param instance The process instance to execute
     * @return the list
     */
    List<ProcessInstance> execute(ProcessInstance instance);

}
