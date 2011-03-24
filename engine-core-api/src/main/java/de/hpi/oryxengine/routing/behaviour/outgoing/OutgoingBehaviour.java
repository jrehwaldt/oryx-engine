package de.hpi.oryxengine.routing.behaviour.outgoing;

import java.util.List;

import de.hpi.oryxengine.exception.NoValidPathException;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Interface SplitBehaviour.
 */
public interface OutgoingBehaviour {

    /**
     * Split.
     *
     * @param instances the instances to split/distribute according to outgoing transitions.
     * @return the list of new process instances that point to the destination-nodes of the outgoing transitions.
     * @throws NoValidPathException the routing found no path with a true condition
     */
    List<Token> split(List<Token> instances) throws NoValidPathException;
}
