package de.hpi.oryxengine.factories.process;

import java.util.UUID;

import de.hpi.oryxengine.JodaEngineServices;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;

/**
 * A factory for creating Process objects. Currently they create a token that points too the connected nodes.
 */
public interface ProcessDeployer {

    /**
     * Deploys the process in the repository.
     * 
     * @param engineServices
     *            the engine services that are used for interaction (e.g. process deploying, resource creation etc.)
     * @return the UUID of the process instance that you deployed, so you can start it in the Navigator.
     * @throws IllegalStarteventException
     *             the illegal startevent exception
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    UUID deploy(JodaEngineServices engineServices)
    throws IllegalStarteventException, ResourceNotAvailableException;

    /**
     * Stops remaining resources that may have been created. Default behaviour is doing nothing, otherwise the deployer
     * himself is responsible for what he does.
     */
    void stop();

}
