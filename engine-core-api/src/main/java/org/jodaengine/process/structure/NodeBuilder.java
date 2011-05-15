package org.jodaengine.process.structure;

import javax.annotation.Nonnull;

import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.incomingbehaviour.IncomingBehaviour;
import org.jodaengine.node.outgoingbehaviour.OutgoingBehaviour;

/**
 * The Interface {@link NodeBuilder}. The {@link NodeBuilder} is a comfortable way to construct a {@link Node}.
 */
public interface NodeBuilder {

    /**
     * Sets the activity class only and uses the empty default constructor upon instantiation. This is a convenience
     * method.
     * 
     * @param activityBehavior
     *            - the {@link Activity activityBehavior} of the {@link Node} that should be built
     * @return this {@link NodeBuilder} in order to keep on configuring it
     */
    @Nonnull
    NodeBuilder setActivityBehavior(Activity activityBehavior);

    /**
     * Sets the {@link IncomingBehaviour}.
     * 
     * @param behaviour
     *            - the new {@link IncomingBehaviour}
     * @return this {@link NodeBuilder} in order to keep on configuring it
     */
    @Nonnull
    NodeBuilder setIncomingBehaviour(IncomingBehaviour behaviour);

    /**
     * Sets the {@link OutgoingBehaviour}.
     * 
     * @param behaviour
     *            - the new {@link OutgoingBehaviour}
     * @return this {@link NodeBuilder} in order to keep on configuring it
     */
    @Nonnull
    NodeBuilder setOutgoingBehaviour(OutgoingBehaviour behaviour);

    /**
     * Builds the {@link Node} and returns it.
     * 
     * @return a configured {@link Node}
     * @thorws {@link JodaEngineRuntimeException} in case some contraints are not fulfilled.
     */
    @Nonnull
    Node buildNode();
}
