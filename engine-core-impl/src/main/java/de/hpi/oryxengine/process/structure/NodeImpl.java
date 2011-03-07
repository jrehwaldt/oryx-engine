package de.hpi.oryxengine.process.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The Class AbstractNode. Which is used for the graph representation of a Process
 */
public class NodeImpl
implements Node {

    /**
     * The activity. This is the behaviour of the node e.g. what gets executed.
     * */
    private Activity activity;

    /** The routing behaviour. E.g. incoming and outgoing transitions. */
    private OutgoingBehaviour outgoingBehaviour;
    private IncomingBehaviour incomingBehaviour;

    /** The next node. */
    private List<Transition> transitions;

    /** The id. */
    private UUID id;

    /**
     * Instantiates a new abstract node.
     * 
     * @param activity the activity to be executed
     * @param behaviour the behaviour of the node
     */
    public NodeImpl(Activity activity,
                    IncomingBehaviour incomingBehaviour,
                    OutgoingBehaviour outgoingBehaviour) {

        this.activity = activity;
        this.incomingBehaviour = incomingBehaviour;
        this.outgoingBehaviour = outgoingBehaviour;
        this.transitions = new ArrayList<Transition>();
    }
    

    public OutgoingBehaviour getOutgoingBehaviour() {
    
        return outgoingBehaviour;
    }


    public void setOutgoingBehaviour(OutgoingBehaviour outgoingBehaviour) {
    
        this.outgoingBehaviour = outgoingBehaviour;
    }


    public IncomingBehaviour getIncomingBehaviour() {
    
        return incomingBehaviour;
    }


    public void setIncomingBehaviour(IncomingBehaviour incomingBehaviour) {
    
        this.incomingBehaviour = incomingBehaviour;
    }


    /**
     * Instantiates a new node impl.
     * 
     * @param activity
     *            the activity
     */
    public NodeImpl(Activity activity) {
        this(activity, new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
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
     * Transition to the next node.
     *
     * @param node the node
     * {@inheritDoc}
     */
    @Override
    public void transitionTo(Node node) {
        Condition c = new ConditionImpl();
        createTransitionWithCondition(node, c);
    }
    
    /**
     * Creates the transition with condition.
     *
     * @param node the destination
     * @param c the condition
     */
    private void createTransitionWithCondition(Node node, Condition c) {
        Transition t = new TransitionImpl(this, node, c);
        this.transitions.add(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getID() {
        return this.id;
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
    public List<Token> execute(Token instance) {
        List<Token> instances = this.incomingBehaviour.join(instance);
        this.activity.execute(instance);
        return this.outgoingBehaviour.split(instances);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void transitionToWithCondition(Node node, Condition c) {
        createTransitionWithCondition(node, c);
        
    }
}
