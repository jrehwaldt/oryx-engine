package de.hpi.oryxengine.process.structure;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.routing.behaviour.RoutingBehaviour;
import de.hpi.oryxengine.routing.behaviour.impl.EmptyRoutingBehaviour;

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
    private List<Transition> transitions;

    /** The id. */
    private String id;

    /**
     * Instantiates a new abstract node.
     * 
     * @param activity the activity to be executed
     * @param behaviour the behaviour of the node
     */
    public NodeImpl(Activity activity,
                    RoutingBehaviour behaviour) {

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
     * {@inheritDoc}
     */
    @Override
    public Activity getActivity() {
        return activity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setActivity(Activity activity) {

        this.activity = activity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void transitionTo(Node node) {
        Condition c = new ConditionImpl();
        Transition t = new TransitionImpl(this, node, c);
        this.transitions.add(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * @param behaviour the new behaviour
     */
    public void setBehaviour(RoutingBehaviour behaviour) {
        this.behaviour = behaviour;
    }

    /**
     * Sets the transitions.
     * 
     * @param transitions the new transitions
     */
    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transition> getTransitions() {
        return transitions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRoutingBehaviour(RoutingBehaviour behaviour) {
        this.behaviour = behaviour;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoutingBehaviour getRoutingBehaviour() {
        return behaviour;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProcessInstance> execute(ProcessInstance instance) {
        this.activity.execute(instance);
        return this.behaviour.execute(instance);
    }
}
