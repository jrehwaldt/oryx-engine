package org.jodaengine.node.activity.bpmn;

import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.token.Token;

/**
 * This class provides some helper methods for activities that has a more extended canceling mechanism like {@link BpmnHumanTaskActivity}, ... . 
 */
public abstract class AbstractCancelableActivity extends AbstractActivity {

    /**
     * This method returns the identifier for an internalVariable. The identifier should be unique, as the token can
     * only work on one activity at a time.
     * 
     * @param prefix - the prefix of the variable
     * @param token - the token where the variable belongs to
     * @return a String representing the identifier
     */
    protected static final String internalVariableId(String prefix, Token token) {

        return prefix + "-" + token.getID() + "-" + token.getCurrentNode().getID();
    }
}
