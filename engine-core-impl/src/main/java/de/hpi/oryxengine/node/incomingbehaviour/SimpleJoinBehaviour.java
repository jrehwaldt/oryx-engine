package de.hpi.oryxengine.node.incomingbehaviour;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;

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

}
