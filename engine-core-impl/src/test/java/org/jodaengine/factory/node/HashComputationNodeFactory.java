package org.jodaengine.factory.node;

import org.jodaengine.node.activity.custom.HashComputationActivity;

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
    public void setActivityBlueprint() {

        if (hashAlgorithm == null) {
            
            activityBehavior = new HashComputationActivity(variableName, toBeHashed);
        } else {
            
            activityBehavior = new HashComputationActivity(variableName, toBeHashed, hashAlgorithm);
        }
    }
}
