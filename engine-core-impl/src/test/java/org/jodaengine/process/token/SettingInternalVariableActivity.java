package org.jodaengine.process.token;

import org.jodaengine.node.activity.AbstractActivity;

/**
 * Little ActivitBehavior for the {@link InternalVariableTest}.
 */
public class SettingInternalVariableActivity extends AbstractActivity {

    private String variableId;
    private Object variableValue;

    /**
     * Default Constructor.
     * 
     * @param variableId
     *            - id of the internal variable
     * @param variableValue
     *            - value of the internal variable
     */
    public SettingInternalVariableActivity(String variableId, int variableValue) {

        this.variableId = variableId;
        this.variableValue = variableValue;
    }

    @Override
    protected void executeIntern(AbstractToken token) {

        token.setInternalVariable(variableId, variableValue);
        token.suspend();
    }
}
