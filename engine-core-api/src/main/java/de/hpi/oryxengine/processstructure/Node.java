package de.hpi.oryxengine.processstructure;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;

/**
 * The Interface for Nodes. Nodes are hubs in the graph representation of a process.
 */
public interface Node {

    // TODO The node needs a join- and a split-behaviour, as you can imagine
    // nodes that have 3 incoming transitions and
    // 5 outgoing transitions in some crazy modelling language

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
    ArrayList<Transition> getTransitions();

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
     * @return the list
     */
    List<ProcessInstance> execute(ProcessInstance instance);

}
