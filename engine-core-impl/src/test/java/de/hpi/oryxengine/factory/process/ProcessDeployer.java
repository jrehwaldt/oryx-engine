package de.hpi.oryxengine.factory.process;

import java.util.UUID;

import de.hpi.oryxengine.exception.IllegalStarteventException;

/**
 * A factory for creating Process objects.
 * Currently they create a token that points too the connected nodes.
 */
public interface ProcessDeployer {
    
    /**
     *
     * Deploys the process in the repository.
     *
     * @return the UUID of the process instance that you deployed, so you can start it in the Navigator.
     * @throws IllegalStarteventException the illegal startevent exception
     */
    UUID deploy() throws IllegalStarteventException;

}
