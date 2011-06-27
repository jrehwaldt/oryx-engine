package org.jodaengine.node.incomingbehaviour;

import java.util.Arrays;
import java.util.Collection;

import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;


/**
 * The Class SimpleJoinBehaviour. Just takes the incoming instance and performs no real joining at all.
 */
public class SimpleJoinBehaviour implements IncomingBehaviour {

    @Override
    public Collection<Token> join(Token token) {
        
        return Arrays.asList(token);
    }

    @Override
    public boolean joinable(Token token, Node node) {

        return true;
    }

}
