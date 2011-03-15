package de.hpi.oryxengine.activity.impl;

import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Class PrintingVariableActivity.
 * Prints out a variable value which the activity gets in its constructor.
 */
public class PrintingVariableActivity
extends AbstractActivity {

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
     * Execute intern.
     *
     * @param instance the instance
     * {@inheritDoc}
     */
    @Override
    public void executeIntern(Token instance) {

        String variableValue = (String) instance.getContext().getVariable(variableName).toString();
        System.out.println("In der Variable " + variableName + " steht " + variableValue + " .");
    }

}
