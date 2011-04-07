package de.hpi.oryxengine.factory.process;

import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.token.Token;

/**
 * A factory for creating Process objects.
 * Currently they create a token that points too the connected nodes.
 */
public interface ProcessTokenFactory {
    
    /**
     * Creates the Token which starts the process Instance (points to the first node).
     *
     * @return the token
     */
    void deploy() throws IllegalStarteventException;

}
