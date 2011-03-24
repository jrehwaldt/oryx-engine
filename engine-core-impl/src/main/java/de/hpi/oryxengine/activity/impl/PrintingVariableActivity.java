package de.hpi.oryxengine.activity.impl;

import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.process.instance.ProcessInstanceContext;
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
     * @param token the instance
     * {@inheritDoc}
     */
    @Override
    public void executeIntern(Token token) {

        ProcessInstanceContext context = token.getInstance().getContext();
        String variableValue = (String) context.getVariable(variableName).toString();
        System.out.println("In der Variable " + variableName + " steht " + variableValue + " .");
    }

}
