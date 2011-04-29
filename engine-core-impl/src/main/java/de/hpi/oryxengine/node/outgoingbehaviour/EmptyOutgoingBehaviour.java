package de.hpi.oryxengine.node.outgoingbehaviour;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;

/**
 * The Class EmptyOutgoingBehaviour. This is needed for {@link BpmnEndActivity}s as they don't have outgoing edges and need
 * to handle this.
 */
public class EmptyOutgoingBehaviour implements OutgoingBehaviour {

    @Override
    public List<Token> split(List<Token> tokens) {
        return new ArrayList<Token>();
    }

}
