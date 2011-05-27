package org.jodaengine.factories.process;

import java.lang.reflect.InvocationTargetException;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.process.definition.ProcessDefinitionID;

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
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     * @throws NoSuchMethodException
     *             the no such method exception
     */
    ProcessDefinitionID deploy(JodaEngineServices engineServices)
    throws IllegalStarteventException, ResourceNotAvailableException, InstantiationException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException;

    /**
     * Stops remaining resources that may have been created. Default behaviour is doing nothing, otherwise the deployer
     * himself is responsible for what he does.
     */
    void stop();

}
