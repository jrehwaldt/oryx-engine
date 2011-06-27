package org.jodaengine.node.outgoingbehaviour;

import java.util.Collection;

import org.jodaengine.exception.NoValidPathException;
import org.jodaengine.process.token.Token;

/**
 * The Interface OutgoingBehaviour.
 */
public interface OutgoingBehaviour {

    /**
     * Split.
     * 
     * @param tokens
     *            the tokens to split/distribute according to outgoing {@link ControlFlow}s.
     * @return the list of new process instances that point to the destination-nodes
     *            of the outgoing {@link ControlFlow}s.
     * @throws NoValidPathException
     *             the routing found no path with a true condition
     */
    Collection<Token> split(Collection<Token> tokens)
    throws NoValidPathException;
}
