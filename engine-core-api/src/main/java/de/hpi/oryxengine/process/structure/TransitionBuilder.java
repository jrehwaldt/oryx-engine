package de.hpi.oryxengine.process.structure;

import javax.annotation.Nonnull;

/**
 * The Interface {@link TransitionBuilder}. The {@link TransitionBuilder} is a comfortable way to construct a
 * {@link Transition}.
 */
public interface TransitionBuilder {

    /**
     * Defines the source and the destination of the {@link Transition}.
     * 
     * @param source
     *            - the {@link Node sourceNode} which is the start of the {@link Transition}
     * @param destination
     *            - the {@link Node destinationNode} which is the end of the {@link Transition}
     * @return this {@link TransitionBuilder} in order to keep on configuring it
     */
    @Nonnull
    TransitionBuilder transitionGoesFromTo(Node source, Node destination);

    /**
     * 
     * 
     * @param condition - the {@link Condition} that needs to apply in order to fire the {@link Transition}
     * @return this {@link TransitionBuilder} in order to keep on configuring it
     */
    @Nonnull
    TransitionBuilder setCondition(Condition condition);

    /**
     * Builds the {@link Transition} and returns it.
     * 
     * @return a configured {@link Transition}
     * @thorws {@link JodaEngineRuntimeException} in case some constraints are not fulfilled.
     */
    @Nonnull
    Transition buildTransition();
}
