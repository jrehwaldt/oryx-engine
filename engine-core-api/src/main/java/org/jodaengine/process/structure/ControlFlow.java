package org.jodaengine.process.structure;

import javax.annotation.Nonnull;

/**
 * The Interface for control flows. Control flows are the edges between nodes.
 */
public interface ControlFlow {

    /**
     * Gets the condition that is connected to the control flow.
     * 
     * @return the condition
     */
    @Nonnull
    Condition getCondition();

    /**
     * Gets the destination of the control flow.
     * 
     * @return the destination node of the edge.
     */
    @Nonnull
    Node getDestination();

    /**
     * Gets the source of the control flow.
     * 
     * @return the source node of the edge.
     */
    @Nonnull
    Node getSource();
}
