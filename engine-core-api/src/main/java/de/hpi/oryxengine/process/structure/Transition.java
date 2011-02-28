package de.hpi.oryxengine.process.structure;

/**
 * The Interface for transitions. Transitions are the edges between nodes.
 */
public interface Transition {

    /**
     * Gets the condition that is connected to the transition.
     *
     * @return the condition
     */
    Condition getCondition();

    /**
     * Gets the destination of the transition.
     *
     * @return the destination node of the edge.
     */
    Node getDestination();

    /**
     * Gets the source of the transition.
     *
     * @return the source node of the edge.
     */
    Node getSource();
}
