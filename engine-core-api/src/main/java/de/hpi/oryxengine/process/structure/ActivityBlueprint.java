package de.hpi.oryxengine.process.structure;

import java.lang.reflect.InvocationTargetException;

import de.hpi.oryxengine.node.activity.Activity;

/**
 * The ActivityBlueprint is used, when a token encounters a node. Then it can read the blueprint in order to instantiate
 * the activity.
 */
public interface ActivityBlueprint {

    /**
     * Gets the activity class to instantiate.
     * 
     * @return the activity class
     */
    Class<? extends Activity> getActivityClass();

    /**
     * Gets the constructor signature. The order of is important!
     * 
     * @return the constructor signature to use when the activity class is instantiated
     */
    Class<?>[] getConstructorSignature();

    /**
     * Gets the parameters. These have to be in the order according to the signature.
     * 
     * @return the parameters
     */
    Object[] getParameters();

    /**
     * Creates an activity instance from this blueprint.
     * 
     * @return the created activity
     * @throws NoSuchMethodException
     *             thrown if the specified constructor does not exist.
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             thrown if the constructor is not accessible
     * @throws InvocationTargetException
     *             the invocation target exception
     */
    Activity instantiate() 
        throws NoSuchMethodException, 
        InstantiationException, 
        IllegalAccessException, 
        InvocationTargetException;
}
