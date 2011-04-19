package de.hpi.oryxengine.process.definition;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;

/**
 * Helps building a {@link NodeParameter}.
 */
public interface NodeParameterBuilder {

    /**
     * Sets the activity class only and uses the empty default constructor upon instantiation. This is a convenience
     * method.
     * 
     * @param clazz
     *            - the new {@link Activity ActivityClass}
     * @return this {@link NodeParameter} in order to keep on configuring it
     */
    NodeParameterBuilder setActivityBlueprintFor(Class<? extends Activity> clazz);

    /**
     * This class added a parameter for the instantiation of the {@link Activity ActivityClass}.
     * 
     * Calling this method for the second time will add the second constructor parameter .
     * 
     * @param parameterClazz
     *            - {@link Class} of the parameter that is given to
     * @param parameterInstance
     *            - the concrete parameter instance, e.g. "Hallo"
     * @return this {@link NodeParameter} in order to keep on configuring it
     */
    NodeParameterBuilder addConstructorParameter(Class<?> parameterClazz, Object parameterInstance);

    /**
     * Sets the {@link IncomingBehaviour}.
     * 
     * @param behaviour
     *            - the new {@link IncomingBehaviour}
     * @return this {@link NodeParameter} in order to keep on configuring it
     */
    NodeParameterBuilder setIncomingBehaviour(IncomingBehaviour behaviour);

    /**
     * Sets the {@link OutgoingBehaviour}.
     * 
     * @param behaviour
     *            - the new {@link OutgoingBehaviour}
     * @return this {@link NodeParameter} in order to keep on configuring it
     */
    NodeParameterBuilder setOutgoingBehaviour(OutgoingBehaviour behaviour);

    /**
     * Finishes configuring and retrieves the {@link NodeParameter}, but does not clear the {@link NodeParameterBuilder
     * Builderinstance}. All instance variables will be untouched.
     * 
     * @return this {@link NodeParameter} in order to keep on configuring it
     */
    NodeParameter buildNodeParameter();

    /**
     * Finishes configuring and retrieves the {@link NodeParameter}. Clears the {@link ActivityBlueprint}. That's why we
     * have to set the {@link Activity ActivityClass} and the ConstructorParameter again.
     * 
     * @return this {@link NodeParameter} in order to keep on configuring it
     */
    NodeParameter buildNodeParameterAndClear();

}
