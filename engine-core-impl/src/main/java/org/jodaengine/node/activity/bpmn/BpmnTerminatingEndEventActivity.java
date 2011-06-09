package org.jodaengine.node.activity.bpmn;

import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.token.Token;

/**
 * This activity corresponds to the BPMN terminating end event. Upon its execution, all ongoing activities have to be
 * cancelled. In contrast to the BPMN 2.0 specification, ongoing activity execution are not interrupted, only suspended
 * activities are cancelled, etc.
 */
public class BpmnTerminatingEndEventActivity extends BpmnEndEventActivity {

    @Override
    protected void executeIntern(Token token) {

        AbstractProcessInstance instance = token.getInstance();

        token.getNavigator().cancelProcessInstance(instance);
    }
}
