package org.jodaengine.node.incomingbehaviour;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.process.token.Token;


/**
 * The Class SimpleJoinBehaviour. Just takes the incoming instance and performs no real joining at all.
 */
public class SimpleJoinBehaviour implements IncomingBehaviour {

    @Override
    public List<Token> join(Token token) {

        List<Token> joinedInstances = new ArrayList<Token>();
        joinedInstances.add(token);
        return joinedInstances;
    }

    @Override
    public boolean joinable(Token token) {

        return true;
    }

}
