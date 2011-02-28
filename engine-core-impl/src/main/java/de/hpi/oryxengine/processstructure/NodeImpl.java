package de.hpi.oryxengine.processstructure;

import java.util.ArrayList;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;
import de.hpi.oryxengine.routingBehaviour.impl.EmptyRoutingBehaviour;

/**
 * The Class AbstractNode. Which is used for the graph representation of a Process
 */
public class NodeImpl implements Node {

    /**
     * The activity. This is the behaviour of the node e.g. what gets executed.
     * */
    private Activity activity;

    /** The routing behaviour. E.g. incoming and outgoing transitions. */
    private RoutingBehaviour behaviour;

    /** The next node. */
    private ArrayList<Transition> transitions;

    /** The id. */
    private String id;

    /**
     * Instantiates a new abstract node.
     * 
     * @param activity
     *            the activity to be executed
     * @param behaviour
     *            the behaviour of the node
     */
    public NodeImpl(Activity activity, RoutingBehaviour behaviour) {

        this.activity = activity;
        this.behaviour = behaviour;
        this.transitions = new ArrayList<Transition>();
    }
    

    /**
     * Instantiates a new node impl.
     * 
     * @param activity
     *            the activity
     */
    public NodeImpl(Activity activity) {

        this(activity, new EmptyRoutingBehaviour());
    }

    /**
     * Gets the activity.
     * 
     * @return the activity to be executed
     */
    public Activity getActivity() {

        return activity;
    }

    /**
     * Sets the activity.
     * 
     * @param activity
     *            the new activity
     */
    public void setActivity(Activity activity) {

        this.activity = activity;
    }

    /**
     * Sets the next node.
     * 
     * @param node
     *            the next node where the transition points to
     */

    public void transitionTo(Node node) {

        Condition c = new ConditionImpl();
        Transition t = new TransitionImpl(this, node, c);
        this.transitions.add(t);
    }

    /**
     * Returns the id.
     * 
     * @return the id
     * @see de.hpi.oryxengine.processstructure.Node#getId()
     */
    public String getId() {

        return this.id;
    }

    /**
     * Sets the id.
     * 
     * @param id
     *            the new id
     * @see de.hpi.oryxengine.processstructure.Node#setId(java.lang.String)
     */
    public void setId(String id) {

        this.id = id;
    }

    /**
     * Gets the behaviour.
     * 
     * @return the behaviour
     */
    public RoutingBehaviour getBehaviour() {

        return behaviour;
    }

    /**
     * Sets the behaviour.
     * 
     * @param behaviour
     *            the new behaviour
     */
    public void setBehaviour(RoutingBehaviour behaviour) {

        this.behaviour = behaviour;
    }

    /**
     * Sets the transitions.
     * 
     * @param transitions
     *            the new transitions
     */
    public void setTransitions(ArrayList<Transition> transitions) {

        this.transitions = transitions;
    }

    /**
     * get the transitions of the node.
     * 
     * @return the transitions
     * @see de.hpi.oryxengine.processstructure.Node#getTransitions()
     */
    public ArrayList<Transition> getTransitions() {

        return transitions;
    }

    /**
     * Set the routing behaviour of the node.
     * 
     * @param behaviour
     *            the new routing behaviour
     * @see 
     * de.hpi.oryxengine.processstructure.Node#setRoutingBehaviour(de.hpi.oryxengine.routingBehaviour.RoutingBehaviour)
     */
    public void setRoutingBehaviour(RoutingBehaviour behaviour) {

        this.behaviour = behaviour;
    }

    /**
     * Get the routing behaviour of the node.
     * 
     * @return the routing behaviour
     * @see de.hpi.oryxengine.processstructure.Node#getRoutingBehaviour()
     */
    public RoutingBehaviour getRoutingBehaviour() {

        return behaviour;
    }
}
