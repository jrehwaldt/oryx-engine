package de.hpi.oryxengine.process.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The Class AbstractNode. Which is used for the graph representation of a Process
 */
public class NodeImpl implements Node {

    /**
     * The activity. This is the behaviour of the node e.g. what gets executed.
     * */
    private ActivityBlueprint blueprint;

    private OutgoingBehaviour outgoingBehaviour;
    private IncomingBehaviour incomingBehaviour;

    private List<Transition> outgoingTransitions, incomingTransitions;

    private UUID id;

    /**
     * Instantiates a new abstract node.
     * 
     * @param blueprint
     *            the blueprint of the activity that is to instantiate when the node is reached by a token
     * @param incomingBehaviour
     *            the incoming behaviour
     * @param outgoingBehaviour
     *            the outgoing behaviour
     */
    public NodeImpl(ActivityBlueprint blueprint,
                    IncomingBehaviour incomingBehaviour,
                    OutgoingBehaviour outgoingBehaviour) {

        this.blueprint = blueprint;
        this.incomingBehaviour = incomingBehaviour;
        this.outgoingBehaviour = outgoingBehaviour;
        this.outgoingTransitions = new ArrayList<Transition>();
        this.incomingTransitions = new ArrayList<Transition>();

        // TODO is it okay, to just create a random one?
        this.id = UUID.randomUUID();
    }

    @Override
    public OutgoingBehaviour getOutgoingBehaviour() {

        return outgoingBehaviour;
    }

    @Override
    public void setOutgoingBehaviour(OutgoingBehaviour outgoingBehaviour) {

        this.outgoingBehaviour = outgoingBehaviour;
    }

    @Override
    public IncomingBehaviour getIncomingBehaviour() {

        return incomingBehaviour;
    }

    @Override
    public void setIncomingBehaviour(IncomingBehaviour incomingBehaviour) {

        this.incomingBehaviour = incomingBehaviour;
    }

    /**
     * This is a convenience constructor, if you only need the standard join- and split-behaviour.
     * 
     * @param blueprint
     *            the blueprint
     */
    public NodeImpl(ActivityBlueprint blueprint) {

        this(blueprint, new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
    }

    /**
     * This is a convenience constructor if you need standard join- and split-behaviour and the default constructor for
     * the given activity class.
     * 
     * @param clazz
     *            the clazz
     */
    public NodeImpl(Class<? extends Activity> clazz) {

        this(null, new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
        Class<?>[] conSig = {};
        Object[] conArgs = {};
        this.blueprint = new ActivityBlueprintImpl(clazz, conSig, conArgs);
    }

    @Override
    public void transitionTo(Node node) {

        Condition c = new ConditionImpl();
        createTransitionWithCondition(node, c);
    }

    /**
     * Creates the transition with condition.
     * 
     * @param node
     *            the destination
     * @param c
     *            the condition
     */
    private void createTransitionWithCondition(Node node, Condition c) {

        Transition t = new TransitionImpl(this, node, c);
        this.outgoingTransitions.add(t);
        List<Transition> nextIncoming = node.getIncomingTransitions();
        nextIncoming.add(t);
    }

    @Override
    public UUID getID() {

        return this.id;
    }

    /**
     * Sets the transitions.
     * 
     * @param transitions
     *            the new transitions
     */
    public void setTransitions(List<Transition> transitions) {

        this.outgoingTransitions = transitions;
    }

    @Override
    public List<Transition> getOutgoingTransitions() {

        return outgoingTransitions;
    }

    @Override
    public List<Transition> getIncomingTransitions() {

        return incomingTransitions;
    }

    @Override
    public void transitionToWithCondition(Node node, Condition c) {

        createTransitionWithCondition(node, c);

    }

    @Override
    public ActivityBlueprint getActivityBlueprint() {

        return blueprint;
    }

    @Override
    public void setActivityBlueprint(ActivityBlueprint blueprint) {

        this.blueprint = blueprint;

    }

}
