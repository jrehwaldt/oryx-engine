package de.hpi.oryxengine.activity.impl;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * The Class AddNumbersAndStoreActivity.
 * As the name inidcates, an activity that adds two numbers and stores the result.
 */
public class AddNumbersAndStoreActivity implements Activity {

    /** The number a. */
    private int numberA;

    /** The number b. */
    private int numberB;

    /** The name the resulting variable should have. */
    private String resultVaribaleName;

    /**
     * Instantiates a "new adds the numbers and store" activity.
     *
     * @param a the first summand
     * @param b the second summand
     * @param varibaleName the varibale name
     */
    public AddNumbersAndStoreActivity(int a, int b, String varibaleName) {

        numberA = a;
        numberB = b;
        resultVaribaleName = varibaleName;
    }

    /**
     * sums up the two summands and sets the corresponding resultvariable.
     *
     * @param instance the processinstance since we need the variables and need to set the result.
     * @see de.hpi.oryxengine.activity.Activity#execute(de.hpi.oryxengine.process.instance.ProcessInstance)
     */
    public void execute(ProcessInstance instance) {

        int result = numberA + numberB;
        instance.setVariable(resultVaribaleName, "" + result);
    }

}
