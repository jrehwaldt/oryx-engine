package de.hpi.oryxengine.routing.behaviour.incoming;

import java.util.List;

import de.hpi.oryxengine.process.token.Token;

/**
 * The Interface JoinBehaviour.
 */
public interface IncomingBehaviour {

    /**
     * Join.
     * 
     * @param instance
     *            the instance for which the join-algorithm is invoked.
     * @return the result of the joining. Usually this list contains one or zero ProcessInstances (Example: And-Join).
     */
    List<Token> join(Token instance);
}
