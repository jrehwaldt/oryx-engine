package de.hpi.oryxengine.process.structure;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.node.activity.Activity;
import de.hpi.oryxengine.node.incomingbehaviour.IncomingBehaviour;
import de.hpi.oryxengine.node.outgoingbehaviour.OutgoingBehaviour;

/**
 * The Interface {@link NodeBuilder}. The {@link NodeBuilder} is a comfortable way to construct a {@link Node}.
 */
public interface NodeBuilder {

    /**
     * Sets the activity class only and uses the empty default constructor upon instantiation. This is a convenience
     * method.
     * 
     * @param clazz
     *            - the new {@link Activity ActivityClass}
     * @return this {@link NodeBuilder} in order to keep on configuring it
     */
    @Nonnull
    NodeBuilder setActivityBlueprintFor(Class<? extends Activity> clazz);

    /**
     * This class added a parameter for the instantiation of the {@link Activity ActivityClass}.
     * 
     * Calling this method for the second time will add the second constructor parameter .
     * 
     * @param parameterClazz
     *            - {@link Class} of the parameter that is given to
     * @param parameterInstance
     *            - the concrete parameter instance, e.g. "Hallo"
     * @return this {@link NodeBuilder} in order to keep on configuring it
     */
    @Nonnull
    NodeBuilder addConstructorParameter(Class<?> parameterClazz, Object parameterInstance);

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
     * @thorws {@link DalmatinaRuntimeException} in case some contraints are not fulfilled.
     */
    @Nonnull
    Node buildNode();
}
