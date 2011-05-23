package org.jodaengine.node.activity.custom;

import org.jodaengine.node.activity.AbstractBpmnActivity;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.token.BPMNToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class PrintingVariableActivity.
 * Prints out a variable value which the activity gets in its constructor.
 */
public class PrintingVariableActivity
extends AbstractBpmnActivity {

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
    public void executeIntern(BPMNToken token) {

        ProcessInstanceContext context = token.getInstance().getContext();
        logger.info("Variable {}={}", variableName, context.getVariable(variableName));
    }
}
