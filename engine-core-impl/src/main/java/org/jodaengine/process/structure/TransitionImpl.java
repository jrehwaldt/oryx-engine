package org.jodaengine.process.structure;

import org.jodaengine.process.token.Token;

/**
 * The Class TransitionImpl. The implementation of a transition that is. A transition is the edge between to nodes.
 *
 * @param <T> the generic type
 */
public class TransitionImpl<T extends Token<?>> implements Transition<T> {

    /** The destination. E.g. where does the arrow point to. */
    private Node<T> destination;

    /** The start. E.g. Where does the arrow/edge originate. */
    private Node<T> source;

    /** The condition. The transition can only be done with a true condition. */
    private Condition condition;

    /**
     * Instantiates a new transition impl.
     * 
     * @param start
     *            the startnode
     * @param destination
     *            the destinationnode
     * @param c
     *            the condition
     */
    public TransitionImpl(Node<T> start, Node<T> destination, Condition c) {

        this.source = start;
        this.destination = destination;
        this.condition = c;

    }

    /**
     * Gets the condition.
     *
     * @return the condition
     * @see org.jodaengine.process.structure.Transition#getCondition()
     */
    @Override
    public Condition getCondition() {
        return this.condition;
    }

    /**
     * Gets the destination.
     *
     * @return the destination
     * @see org.jodaengine.process.structure.Transition#getDestination()
     */
    @Override
    public Node<T> getDestination() {

        return this.destination;
    }

    /**
     * Gets the start.
     * 
     * @return the start
     */
    @Override
    public Node<T> getSource() {

        return source;
    }

    // TODO the following three methods diverge from the interface. Do we want to expose them in the interface?
    /**
     * Sets the start.
     * 
     * @param start
     *            the new start
     */
    public void setSource(Node<T> start) {

        this.source = start;
    }

    /**
     * Sets the destination.
     * 
     * @param destination
     *            the new destination
     */
    public void setDestination(Node<T> destination) {

        this.destination = destination;
    }

    /**
     * Sets the condition.
     * 
     * @param condition
     *            the new condition
     */
    public void setCondition(Condition condition) {

        this.condition = condition;
    }

}
