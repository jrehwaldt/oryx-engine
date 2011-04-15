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

        instance.cancel();

        token.getNavigator().signalEndedProcessInstance(instance);
    }

}
