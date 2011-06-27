package org.jodaengine.node.outgoingbehaviour;

import java.util.ArrayList;
import java.util.Collection;

import org.jodaengine.process.token.Token;


/**
 * The Class EmptyOutgoingBehaviour. This is needed for {@link BpmnEndEventActivity}s as they
 * don't have outgoing edges and need to handle this.
 */
public class EmptyOutgoingBehaviour implements OutgoingBehaviour {

    @Override
    public Collection<Token> split(Collection<Token> tokens) {

        return new ArrayList<Token>();
    }

}
