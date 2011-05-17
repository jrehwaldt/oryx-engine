package org.jodaengine.process.structure.condition;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.structure.Condition;
import org.jodaengine.process.token.Token;


/**
 * This {@link Condition} only receives the id of a process variable. During runtime the process variable with the
 * specific id will be checked whether it is true or false.
 * 
 * This is not suppose to be a real Condition but rather a demo condition.
 */
public class CheckVariableTrueCondition implements Condition {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private String processVariableID;

    /**
     * Is supposed to be the default constructor for this {@link Condition}.
     * 
     * @param processVariableID
     *            - the ID of the process variable
     */
    public CheckVariableTrueCondition(@Nonnull String processVariableID) {

        this.processVariableID = processVariableID;
    }

    @Override
    public boolean evaluate(Token instance) {

        ProcessInstanceContext processInstanceContext = instance.getInstance().getContext();
        Object processVariable = processInstanceContext.getVariable(processVariableID);

        if (processVariable == null) {
            return false;
        }

        boolean returnBooleanValue = false;
        try {
            returnBooleanValue = (Boolean) processVariable;

        } catch (ClassCastException classCastException) {
            String errorMessage = "The processVariable corresponding to the ID '" + processVariableID
                + "' cannnot be casted to a Boolean Type.";
            logger.error(errorMessage, classCastException);
            new JodaEngineRuntimeException(errorMessage, classCastException);
        }

        return returnBooleanValue;
    }

}
