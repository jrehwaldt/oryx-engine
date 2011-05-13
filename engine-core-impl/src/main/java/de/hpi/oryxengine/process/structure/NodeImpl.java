package de.hpi.oryxengine.process.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.annotate.JsonProperty;

import de.hpi.oryxengine.node.activity.Activity;
import de.hpi.oryxengine.node.incomingbehaviour.IncomingBehaviour;
import de.hpi.oryxengine.node.incomingbehaviour.SimpleJoinBehaviour;
import de.hpi.oryxengine.node.outgoingbehaviour.OutgoingBehaviour;
import de.hpi.oryxengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import de.hpi.oryxengine.process.structure.condition.HashMapCondition;

/**
 * The Class AbstractNode. Which is used for the graph representation of a Process
 */
public class NodeImpl implements Node {

    /**
     * The activity. This is the behaviour of the node e.g. what gets executed.
     * */
    // TODO @Gerardo Comment wegmachen

//    private ActivityBlueprint blueprint;

    private Activity activityBehavior;
    
    
    private OutgoingBehaviour outgoingBehaviour;
    private IncomingBehaviour incomingBehaviour;

    private List<Transition> outgoingTransitions, incomingTransitions;

    private UUID id;
    private Map<String, Object> attributes;

    /**
     * Hidden constructor.
     */
    protected NodeImpl() {

    }

    /**
     * Instantiates a new abstract node.
     * 
     * @param blueprint
     *            the blueprint of the activity that is to instantiate when the node is reached by a token
     * @param incomingBehaviour
     *            the incoming behavior
     * @param outgoingBehaviour
     *            the outgoing behavior
     */
    public NodeImpl(Activity activityBehavior,
                    IncomingBehaviour incomingBehaviour,
                    OutgoingBehaviour outgoingBehaviour) {

        this.activityBehavior = activityBehavior;
        this.incomingBehaviour = incomingBehaviour;
        this.outgoingBehaviour = outgoingBehaviour;
        this.outgoingTransitions = new ArrayList<Transition>();
        this.incomingTransitions = new ArrayList<Transition>();

        this.id = UUID.randomUUID();
    }

    @Override
    public OutgoingBehaviour getOutgoingBehaviour() {
    
        return outgoingBehaviour;
    }

    @Override
    public IncomingBehaviour getIncomingBehaviour() {
    
        return incomingBehaviour;
    }

    @Override
    public Transition transitionTo(Node node) {

        Condition condition = new HashMapCondition();
        return createTransitionWithCondition(node, condition);
    }

    @Override
    public Transition transitionToWithCondition(Node node, Condition c) {

        return createTransitionWithCondition(node, c);
    }

    /**
     * Creates the transition with condition.
     * 
     * @param node
     *            the destination
     * @param c
     *            the condition
     */
    private Transition createTransitionWithCondition(Node node, Condition c) {

        Transition transition = new TransitionImpl(this, node, c);
        this.outgoingTransitions.add(transition);
        List<Transition> nextIncoming = node.getIncomingTransitions();
        nextIncoming.add(transition);

        return transition;
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

    @JsonProperty
    @Override
    public Map<String, Object> getAttributes() {

        if (attributes == null) {
            attributes = new HashMap<String, Object>();
        }
        return attributes;
    }

    @Override
    public Object getAttribute(String attributeKey) {

        return getAttributes().get(attributeKey);
    }

    @Override
    public void setAttribute(String attributeKey, Object attributeValue) {

        getAttributes().put(attributeKey, attributeValue);
    }

    @Override
    public Activity getActivityBehaviour() {

        return this.activityBehavior;
    }
}
