package de.hpi.oryxengine.node.activity.bpmn;

import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.process.token.Token;

/**
 * This activity corresponds to the BPMN terminating end event. Upon its execution, all ongoing activities have to be
 * cancelled.
 */
public class BpmnTerminatingEndActivity extends BpmnEndActivity {

    @Override
    protected void executeIntern(Token token) {

        AbstractProcessInstance instance = token.getInstance();

        instance.cancel();

        token.getNavigator().signalEndedProcessInstance(instance);
    }

}
