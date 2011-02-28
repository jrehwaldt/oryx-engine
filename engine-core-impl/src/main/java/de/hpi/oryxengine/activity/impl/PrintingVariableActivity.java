package de.hpi.oryxengine.activity.impl;

import de.hpi.oryxengine.activity.AbstractActivityImpl;
import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * The Class PrintingVariableActivity.
 * Prints out a variable value which the activity gets in its constructor.
 */
public class PrintingVariableActivity
extends AbstractActivityImpl {

    /** The variable name. */
    private String variableName;

    /**
     * Instantiates a new printing variable activity.
     *
     * @param variableToBePrinted the variable to be printed
     */
    public PrintingVariableActivity(String variableToBePrinted) {
        super();
        variableName = variableToBePrinted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeIntern(ProcessInstance instance) {

        String variableValue = (String) instance.getVariable(variableName).toString();
        System.out.println("In der Variable " + variableName + " steht " + variableValue + " .");
    }

}
