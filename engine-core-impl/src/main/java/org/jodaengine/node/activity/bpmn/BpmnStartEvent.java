package org.jodaengine.node.activity.bpmn;

import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.token.AbstractToken;


/**
 * This class represents the BPMN-StartEvent. It indicates the start of a BPMN process.
 */
public class BpmnStartEvent extends AbstractActivity {

    /**
     * The start event doesn't really execute something, so it's blank. {@inheritDoc}
     */
    @Override
    protected void executeIntern(AbstractToken token) {

    }
}
