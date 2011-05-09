package de.hpi.oryxengine.node.activity.bpmn;

import de.hpi.oryxengine.node.activity.AbstractActivity;
import de.hpi.oryxengine.process.token.Token;

/**
 * This class represents the BPMN-StartEvent. It indicates the start of a BPMN process.
 */
public class BpmnStartEvent extends AbstractActivity {

    /**
     * The start event doesn't really execute something, so it's blank. {@inheritDoc}
     */
    @Override
    protected void executeIntern(Token token) {

    }

}
