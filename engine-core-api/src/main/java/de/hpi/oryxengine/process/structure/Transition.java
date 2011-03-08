package de.hpi.oryxengine.process.structure;

import javax.annotation.Nonnull;

/**
 * The Interface for transitions. Transitions are the edges between nodes.
 */
public interface Transition {

    /**
     * Gets the condition that is connected to the transition.
     *
     * @return the condition
     */
    @Nonnull Condition getCondition();

    /**
     * Gets the destination of the transition.
     *
     * @return the destination node of the edge.
     */
    @Nonnull Node getDestination();

    /**
     * Gets the source of the transition.
     *
     * @return the source node of the edge.
     */
    @Nonnull Node getSource();
}
