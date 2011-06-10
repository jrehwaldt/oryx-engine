package org.jodaengine.factories.process;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import org.jodaengine.IdentityService;
import org.jodaengine.JodaEngineServices;
import org.jodaengine.RepositoryService;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.forms.AbstractForm;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.definition.bpmn.BpmnProcessDefinitionBuilder;
import org.jodaengine.resource.IdentityBuilder;

/**
 * The Class AbstractProcessDeployer.
 */
public abstract class AbstractProcessDeployer implements ProcessDeployer {

    /** The builder. */
    protected BpmnProcessDefinitionBuilder processDefinitionBuilder;
    protected IdentityBuilder identityBuilder;
    protected IdentityService identityService;
    protected RepositoryService repoService;

    @Override
    public ProcessDefinitionID deploy(JodaEngineServices engineServices)
    throws ResourceNotAvailableException, InstantiationException, IllegalAccessException, InvocationTargetException,
    IllegalStarteventException, NoSuchMethodException {

        this.processDefinitionBuilder = BpmnProcessDefinitionBuilder.newBuilder();
        this.identityService = engineServices.getIdentityService();
        this.identityBuilder = engineServices.getIdentityService().getIdentityBuilder();
        this.repoService = engineServices.getRepositoryService();

        this.createPseudoHuman();
        this.initializeNodes();

        ProcessDefinitionID id = deploy();
        return id;
    }

    /**
     * Deploys the process definition and all the specified artifacts.
     * 
     * @return the id of the deployed process
     * @throws IllegalStarteventException
     *             the illegal startevent exception
     */
    private ProcessDefinitionID deploy()
    throws IllegalStarteventException {

        ProcessDefinition definition = this.processDefinitionBuilder.buildDefinition();
        DeploymentBuilder deploymentBuilder = this.repoService.getDeploymentBuilder();
        deploymentBuilder.addProcessDefinition(definition);

        for (AbstractForm form : getFormsToDeploy()) {
            deploymentBuilder.addForm(form);
        }

        Deployment deployment = deploymentBuilder.buildDeployment();
        this.repoService.deployInNewScope(deployment);
        return definition.getID();
    }

    /**
     * Gets the artifacts to deploy. Override this method to specify artifacts that should be deployed with the
     * definition.
     * 
     * @return the artifacts to deploy
     */
    public Set<AbstractForm> getFormsToDeploy() {

        return new HashSet<AbstractForm>();
    }

    /**
     * Initialize nodes.
     * 
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     * @throws NoSuchMethodException
     *             the no such method exception
     */
    public abstract void initializeNodes()
    throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;

    /**
     * Creates a thread to complete human tasks. So human task Process deployers shall overwrite this method.
     * Nothing happens here for automated task nodes, so they must not overwrite this method.
     * 
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    public void createPseudoHuman()
    throws ResourceNotAvailableException {

    }

    @Override
    public void stop() {

        // do nothing
    }
}
