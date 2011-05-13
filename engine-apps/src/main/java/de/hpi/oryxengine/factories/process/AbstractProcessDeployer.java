package de.hpi.oryxengine.factories.process;

import java.util.UUID;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.JodaEngineServices;
import de.hpi.oryxengine.RepositoryService;
import de.hpi.oryxengine.deployment.DeploymentBuilder;
import de.hpi.oryxengine.deployment.importer.RawProcessDefintionImporter;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.resource.IdentityBuilder;

/**
 * The Class AbstractProcessDeployer.
 */
public abstract class AbstractProcessDeployer implements ProcessDeployer {

    /** The builder. */
    protected ProcessDefinitionBuilder processDefinitionBuilder;
    protected IdentityBuilder identityBuilder;
    protected IdentityService identityService;
    protected RepositoryService repoService;

    @Override
    public UUID deploy(JodaEngineServices engineServices)
    throws IllegalStarteventException, ResourceNotAvailableException {

        this.processDefinitionBuilder = engineServices.getRepositoryService().getDeploymentBuilder()
        .getProcessDefinitionBuilder();
        this.identityService = engineServices.getIdentityService();
        this.identityBuilder = engineServices.getIdentityService().getIdentityBuilder();
        this.repoService = engineServices.getRepositoryService();

        this.createPseudoHuman();
        this.initializeNodes();
        ProcessDefinition definition = this.processDefinitionBuilder.buildDefinition();
        DeploymentBuilder deploymentBuilder = engineServices.getRepositoryService().getDeploymentBuilder();
        deploymentBuilder.deployProcessDefinition(new RawProcessDefintionImporter(definition));
        return definition.getID();
    }

    /**
     * Initialize nodes.
     */
    abstract public void initializeNodes();

    /**
     * Creates a thread to complete human tasks. So human task Process deployers shall overwrite this method.
     * Nothing happens here for automated task nodes, so they must not overwrite this method.
     * 
     * @throws ResourceNotAvailableException
     */
    public void createPseudoHuman()
    throws ResourceNotAvailableException {

    }

    @Override
    public void stop() {

        // do nothing
    }
}
