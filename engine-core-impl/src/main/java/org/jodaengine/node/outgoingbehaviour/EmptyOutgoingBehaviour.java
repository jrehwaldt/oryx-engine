package org.jodaengine.node.outgoingbehaviour;

import org.jodaengine.node.activity.bpmn.BpmnEndActivity;
import org.jodaengine.process.token.Token;

import java.util.ArrayList;
import java.util.List;


/**
 * The Class EmptyOutgoingBehaviour. This is needed for {@link BpmnEndActivity}s as they don't have outgoing edges and
 * need
 * to handle this.
 */
public class EmptyOutgoingBehaviour implements OutgoingBehaviour {

    @Override
    public List<Token> split(List<Token> tokens) {

        return new ArrayList<Token>();
    }

}
