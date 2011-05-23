package org.jodaengine.node.outgoingbehaviour;

import java.util.List;

import org.jodaengine.exception.NoValidPathException;
import org.jodaengine.process.token.BPMNToken;

/**
 * The Interface SplitBehaviour.
 */
public interface OutgoingBehaviour {

    /**
     * Split.
     *
     * @param bPMNTokens the tokens
     * @return the list of new process instances that point to the destination-nodes of the outgoing transitions.
     * @throws NoValidPathException the routing found no path with a true condition
     */
    List<BPMNToken> split(List<BPMNToken> bPMNTokens)
    throws NoValidPathException;
}
