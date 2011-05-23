package org.jodaengine.process.structure;

import javax.annotation.Nonnull;

import org.jodaengine.process.token.Token;

/**
 * The Interface for transitions. Transitions are the edges between nodes.
 *
 * @param <T> the generic type
 */
public interface Transition<T extends Token<?>> {

    /**
     * Gets the condition that is connected to the transition.
     * 
     * @return the condition
     */
    @Nonnull
    Condition getCondition();

    /**
     * Gets the destination of the transition.
     * 
     * @return the destination node of the edge.
     */
    @Nonnull
    Node<T> getDestination();

    /**
     * Gets the source of the transition.
     * 
     * @return the source node of the edge.
     */
    @Nonnull
    Node<T> getSource();
}
