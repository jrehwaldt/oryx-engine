package de.hpi.oryxengine.factory;

import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;

/**
 * A factory for creating AddNumberAndStoreNode objects.
 * It just sets the activity to the AddNumberAndStoreActivity, which is instructed to sum up 1 and 1.
 */
public class AddNumbersAndStoreNodeFactory extends AbstractNodeFactory {
    
    private int summand1;
    private int summand2;
    private String variablename;
    private static final int DEFAULT_SUMMAND = 1;
    private static final String DEFAULT_VARIABLENAME = "result";
    
    /**
     * Instantiates a new adds the numbers and store node factory with default values.
     */
    AddNumbersAndStoreNodeFactory() {
        this(DEFAULT_SUMMAND, DEFAULT_SUMMAND);
    }
    
    /**
     * Instantiates a new adds the numbers and store node factory with the given 2 values and a default variablename.
     *
     * @param sum1 the first summand
     * @param sum2 the second summand
     */
    AddNumbersAndStoreNodeFactory(int sum1, int sum2) {
        this(DEFAULT_VARIABLENAME, sum1, sum2);
    }
    
    /**
     * Instantiates a new adds the numbers and store node factory with all values defined by the user.
     *
     * @param variablename the variablename
     * @param sum1 the sum1
     * @param sum2 the sum2
     */
    AddNumbersAndStoreNodeFactory(String variablename, int sum1, int sum2) {
        this.summand1 = sum1;
        this.summand2 = sum2;
        this.variablename = variablename;        
    }
    
    /**
     * Sets the activity, overwriting the process in the AbstractNodeFactory.
     */
    @Override
    public void setActivity() {
        activity = new AddNumbersAndStoreActivity(variablename, summand1, summand2);
    }

}
