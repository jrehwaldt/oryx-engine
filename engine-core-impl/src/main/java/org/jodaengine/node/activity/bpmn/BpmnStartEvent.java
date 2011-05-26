package org.jodaengine.node.activity.bpmn;

import org.jodaengine.node.activity.AbstractBpmnActivity;
import org.jodaengine.process.token.Token;


/**
 * This class represents the BPMN-StartEvent. It indicates the start of a BPMN process.
 */
public class BpmnStartEvent extends AbstractBpmnActivity {

    /**
     * The start event doesn't really execute something, so it's blank. {@inheritDoc}
     */
    @Override
    protected void executeIntern(Token token) {

    }

}
