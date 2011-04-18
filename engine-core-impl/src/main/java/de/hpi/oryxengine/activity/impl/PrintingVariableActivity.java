package de.hpi.oryxengine.activity.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.process.instance.ProcessInstanceContext;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Class PrintingVariableActivity.
 * Prints out a variable value which the activity gets in its constructor.
 */
public class PrintingVariableActivity
extends AbstractActivity {

    private String variableName;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Instantiates a new printing variable activity.
     *
     * @param variableToBePrinted the variable to be printed
     */
    public PrintingVariableActivity(String variableToBePrinted) {
        super();
        variableName = variableToBePrinted;
    }

    @Override
    public void executeIntern(Token token) {

        ProcessInstanceContext context = token.getInstance().getContext();
        String variableValue = (String) context.getVariable(variableName).toString();
        logger.info(variableValue);
    }
}
