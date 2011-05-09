package de.hpi.oryxengine.rest.demo;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.deployment.DeploymentBuilder;
import de.hpi.oryxengine.deployment.importer.RawProcessDefintionImporter;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;
import de.hpi.oryxengine.process.definition.ProcessDefinition;

/**
 * The Class RefProcessAndRoleDeployer - yeah we deploy stuff.
 */
public class RefProcessAndRoleDeployer {
    private static boolean invoked = false;
    
    public static synchronized void generate()
    throws IllegalStarteventException, DefinitionNotFoundException, ResourceNotAvailableException {

        if (!invoked) {
            ShortenedReferenceProcessDeployer.createRoles();
            ShortenedReferenceProcessDeployer.initializeNodes();
            ProcessDefinition definition = ShortenedReferenceProcessDeployer.buildDefinition();
            DeploymentBuilder deploymentBuilder = ServiceFactory.getRepositoryService().getDeploymentBuilder();
            deploymentBuilder.deployProcessDefinition(new RawProcessDefintionImporter(definition));
            invoked = true;
        }
    }
}
