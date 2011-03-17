package de.hpi.oryxengine.routing.behaviour.incoming.impl;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;

// TODO: Auto-generated Javadoc
/**
 * The Class SimpleJoinBehaviour. Just takes the incoming instance and performs no real joining at all.
 */
public class SimpleJoinBehaviour implements IncomingBehaviour {

    /**
     * Join Behviour.
     *
     * @param token the instance
     * @return the list of joined instances
     * @see de.hpi.oryxengine.IncomingBehaviour.JoinBehaviour#join(de.hpi.oryxengine.process.token.Token)
     */
    public List<Token> join(Token token) {

        List<Token> joinedInstances = new ArrayList<Token>();
        joinedInstances.add(token);
        return joinedInstances;
    }

}
