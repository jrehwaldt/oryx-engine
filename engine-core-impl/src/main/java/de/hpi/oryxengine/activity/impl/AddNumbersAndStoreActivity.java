package de.hpi.oryxengine.activity.impl;

import de.hpi.oryxengine.activity.AbstractActivityImpl;
import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * The Class AddNumbersAndStoreActivity.
 * As the name indicates, an activity that adds any number of summands and stores the result.
 */
public class AddNumbersAndStoreActivity
extends AbstractActivityImpl {

    /** Summands. */
    private int[] summands;

    /** The name the resulting variable should have. */
    private String resultVariableName;

    /**
     * Instantiates a "new adds the numbers and store" activity.
     *
     * @param variableName the variable name
     * @param summands summands
     */
    public AddNumbersAndStoreActivity(String variableName,
                                      int... summands) {
        super();
        this.summands = summands;
        resultVariableName = variableName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeIntern(ProcessInstance instance) {
        
        int result = 0;
        for (int value: this.summands) {
            result += value;
        }
        instance.setVariable(resultVariableName, String.valueOf(result));
    }
}
