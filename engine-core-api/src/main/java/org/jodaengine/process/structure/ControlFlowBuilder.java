package org.jodaengine.process.structure;

import javax.annotation.Nonnull;

/**
 * The Interface {@link ControlFlowBuilder}. The {@link ControlFlowBuilder} is a comfortable way to construct a
 * {@link ControlFlow}.
 */
public interface ControlFlowBuilder {

    /**
     * Defines the source and the destination of the {@link ControlFlow}.
     * 
     * @param source
     *            - the {@link Node sourceNode} which is the start of the {@link ControlFlow}
     * @param destination
     *            - the {@link Node destinationNode} which is the end of the {@link ControlFlow}
     * @return this {@link ControlFlowBuilder} in order to keep on configuring it
     */
    @Nonnull
    ControlFlowBuilder controlFlowGoesFromTo(Node source, Node destination);

    /**
     * 
     * 
     * @param condition
     *            - the {@link Condition} that needs to apply in order to fire the {@link ControlFlow}
     * @return this {@link ControlFlowBuilder} in order to keep on configuring it
     */
    @Nonnull
    ControlFlowBuilder setCondition(Condition condition);

    /**
     * Builds the {@link ControlFlow} and returns it.
     * 
     * @return a configured {@link ControlFlow}
     */
    @Nonnull
    ControlFlow buildControlFlow();
    
    /**
     * Sets the id of the {@link ControlFlow} to build.
     *
     * @param id the id to set
     * @return this {@link ControlFlowBuilder} in order to keep on configuring it
     */
    @Nonnull
    ControlFlowBuilder setId(String id);
}
