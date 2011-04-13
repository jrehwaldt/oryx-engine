package de.hpi.oryxengine.factory.node;

import de.hpi.oryxengine.activity.impl.HashComputationActivity;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.structure.Node;

/**
 * A factory for creating HashComputationNode objects. Those nodes are used to compute a specified hash of a given
 * String. This however can be created without any parameters with all default values.
 */
public class HashComputationNodeFactory extends AbstractNodeFactory {

    /** The Constant DEFAULT_VARIABLENAME. */
    private static final String DEFAULT_VARIABLENAME = "hash";

    /** The Constant DEFAULT_TO_BE_HASHED. */
    private static final String DEFAULT_TO_BE_HASHED = "hasheMePlease!!!111einseins";

    /** The variable name. */
    private String variableName;

    /** The to be hashed. */
    private String toBeHashed;

    /** The hash algorithm. */
    private String hashAlgorithm;

    /**
     * Instantiates a new hash computation node factory.
     * 
     * @param variableName
     *            the variable name which is then set in the process token
     * @param toBeHashed
     *            the string to be hashed
     * @param hashAlgorithm
     *            the hash algorithm
     */
    public HashComputationNodeFactory(String variableName, String toBeHashed, String hashAlgorithm) {

        this.variableName = variableName;
        this.toBeHashed = toBeHashed;
        this.hashAlgorithm = hashAlgorithm;
    }

    /**
     * Instantiates a new hash computation node factory.
     * 
     * @param variableName
     *            the variable name which is then set in the process token
     * @param toBeHashed
     *            the string to be hashed
     */
    public HashComputationNodeFactory(String variableName, String toBeHashed) {

        this(variableName, toBeHashed, null);
    }

    /**
     * Instantiates a new hash computation node factory.
     * 
     * @param toBeHashed
     *            the string to be hashed
     */
    public HashComputationNodeFactory(String toBeHashed) {

        this(DEFAULT_VARIABLENAME, toBeHashed);
    }

    /**
     * Instantiates a new hash computation node factory with all defautl values.
     */
    public HashComputationNodeFactory() {

        this(DEFAULT_TO_BE_HASHED);
    }

    /**
     * Sets the activity.
     * 
     * {@inheritDoc}
     */
    @Override
    public void setActivity() {

        activityClazz = HashComputationActivity.class;
    }

    @Override
    public void registerActivityParameters(ProcessInstance instance, Node node) {

        if (hashAlgorithm == null) {
            Class<?>[] constructorSig = {String.class, String.class};
            Object[] params = {variableName, toBeHashed};

            instance.getContext().setActivityConstructorClasses(node.getID(), constructorSig);
            instance.getContext().setActivityParameters(node.getID(), params);
        } else {
            Class<?>[] constructorSig = {String.class, String.class, String.class};
            Object[] params = {variableName, toBeHashed, hashAlgorithm};

            instance.getContext().setActivityConstructorClasses(node.getID(), constructorSig);
            instance.getContext().setActivityParameters(node.getID(), params);
        }
        

    }

}
