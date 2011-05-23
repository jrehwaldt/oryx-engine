package org.jodaengine.process.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.incomingbehaviour.IncomingBehaviour;
import org.jodaengine.node.outgoingbehaviour.OutgoingBehaviour;
import org.jodaengine.process.structure.condition.HashMapCondition;
import org.jodaengine.process.token.Token;

/**
 * The Class AbstractNode. Which is used for the graph representation of a Process
 *
 * @param <T> the generic type
 */
public class NodeImpl<T extends Token<?>> implements Node<T> {
    
    /**
     * The {@link Activity}. This is the behavior of the node e.g. what gets executed.
     */
    private Activity activityBehaviour;

    /** The outgoing behaviour. */
    private OutgoingBehaviour outgoingBehaviour;
    
    /** The incoming behaviour. */
    private IncomingBehaviour<T> incomingBehaviour;

    /** The incoming transitions. */
    private List<Transition<T>> outgoingTransitions, incomingTransitions;

    /** The id. */
    private UUID id;
    
    /** The attributes. */
    private Map<String, Object> attributes;

    /**
     * Hidden constructor.
     */
    protected NodeImpl() {

    }

    /**
     * Instantiates a new abstract node.
     *
     * @param activityBehavior the activity behavior
     * @param incomingBehaviour the incoming behavior
     * @param outgoingBehaviour the outgoing behavior
     */
    public NodeImpl(Activity activityBehavior,
                    IncomingBehaviour<T> incomingBehaviour,
                    OutgoingBehaviour outgoingBehaviour) {

        this.activityBehaviour = activityBehavior;
        this.incomingBehaviour = incomingBehaviour;
        this.outgoingBehaviour = outgoingBehaviour;
        this.outgoingTransitions = new ArrayList<Transition<T>>();
        this.incomingTransitions = new ArrayList<Transition<T>>();

        this.id = UUID.randomUUID();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutgoingBehaviour getOutgoingBehaviour() {

        return outgoingBehaviour;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IncomingBehaviour<T> getIncomingBehaviour() {

        return incomingBehaviour;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transition<T> transitionTo(Node<T> node) {

        Condition condition = new HashMapCondition();
        return createTransitionWithCondition(node, condition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transition<T> transitionToWithCondition(Node<T> node, Condition c) {

        return createTransitionWithCondition(node, c);
    }

    /**
     * Creates the transition with condition.
     *
     * @param node the destination
     * @param c the condition
     * @return the transition
     */
    private Transition<T> createTransitionWithCondition(Node<T> node, Condition c) {

        Transition<T> transition = new TransitionImpl<T>(this, node, c);
        this.outgoingTransitions.add(transition);
        List<Transition<T>> nextIncoming = node.getIncomingTransitions();
        nextIncoming.add(transition);

        return transition;
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
     * @param transitions
     *            the new transitions
     */
    public void setTransitions(List<Transition<T>> transitions) {

        this.outgoingTransitions = transitions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transition<T>> getOutgoingTransitions() {

        return outgoingTransitions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transition<T>> getIncomingTransitions() {

        return incomingTransitions;
    }

    /**
     * {@inheritDoc}
     */
    @JsonProperty
    @Override
    public Map<String, Object> getAttributes() {

        if (attributes == null) {
            attributes = new HashMap<String, Object>();
        }
        return attributes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getAttribute(String attributeKey) {

        return getAttributes().get(attributeKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAttribute(String attributeKey, Object attributeValue) {

        getAttributes().put(attributeKey, attributeValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Activity getActivityBehaviour() {

        return this.activityBehaviour;
    }
}
