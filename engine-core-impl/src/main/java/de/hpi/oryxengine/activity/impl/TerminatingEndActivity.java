package de.hpi.oryxengine.activity.impl;

import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.token.Token;

/**
 * This activity corresponds to the BPMN terminating end event. Upon its execution, all ongoing activities have to be
 * cancelled.
 */
public class TerminatingEndActivity extends EndActivity {

    @Override
    protected void executeIntern(Token token) {

        ProcessInstance instance = token.getInstance();
        // Cancel all ongoing acitivites
        // TODO Intercept the executing threads. a lot of fun.

        // Cancel all ongoing human activites
        for (Token tokenToCancel : instance.getTokens()) {
            tokenToCancel.cancelExecution();
        }

        token.getNavigator().signalEndedProcessInstance(instance);
    }

}
