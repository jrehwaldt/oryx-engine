package org.jodaengine.node.incomingbehaviour;

import java.util.List;

import org.jodaengine.process.token.Token;

/**
 * The Interface JoinBehaviour.
 *
 * @param <T> the generic type Token. Bust be a subclass of Token, e.g. BPMNToken
 */
public interface IncomingBehaviour <T extends Token<?>> {

    /**
     * Join.
     *
     * @param token the token
     * @return the result of the joining. Usually this list contains one or zero ProcessInstances (Example: And-Join).
     */
    List<T> join(T token);
}
