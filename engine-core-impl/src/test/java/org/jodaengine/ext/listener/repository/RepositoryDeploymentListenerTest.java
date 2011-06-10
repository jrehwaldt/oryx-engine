package org.jodaengine.ext.listener.repository;

import static org.mockito.Mockito.mock;

import org.jodaengine.RepositoryServiceImpl;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.ext.listener.RepositoryDeploymentListener;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.bpmn.BpmnProcessDefinitionBuilder;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * This class tests the proper invocation of {@link RepositoryDeploymentListener}s.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-29
 */
public class RepositoryDeploymentListenerTest extends AbstractJodaEngineTest {
    
    private RepositoryDeploymentListener listener;
    
    /**
     * Setup a fresh mock {@link RepositoryDeploymentListener}.
     */
    @BeforeMethod
    public void setUp() {
        this.listener = mock(RepositoryDeploymentListener.class);
    }
    
    /**
     * Tests that a registered listener is properly invoked.
     * 
     * @throws IllegalStarteventException test fails
     * @throws DefinitionNotFoundException test fails
     * @throws ProcessArtifactNotFoundException test fails
     */
    @Test
    public void testRegisteringAndProperInvocationOfListener()
    throws IllegalStarteventException, DefinitionNotFoundException, ProcessArtifactNotFoundException {
        
        RepositoryServiceImpl repository = (RepositoryServiceImpl) this.jodaEngineServices.getRepositoryService();
        Assert.assertTrue(repository.supportsExtension(RepositoryDeploymentListener.class));
        
        repository.registerListeners(RepositoryDeploymentListener.class, this.listener);
        Assert.assertTrue(repository.getListeners(RepositoryDeploymentListener.class).contains(this.listener));
        
        //
        // Definition test
        //
        BpmnProcessDefinitionBuilder builder = BpmnProcessDefinitionBuilder.newBuilder();
        BpmnCustomNodeFactory.createBpmnNullStartNode(builder);
        BpmnProcessDefinitionModifier.decorateWithDefaultBpmnInstantiationPattern(builder);
        ProcessDefinition definition = builder.buildDefinition();
        
        repository.addProcessDefinition(definition);
        Assert.assertNotNull(repository.getProcessDefinition(definition.getID()));
        repository.deleteProcessDefinition(definition.getID());
        
        Mockito.verify(this.listener, Mockito.times(1)).definitionDeployed(repository, definition);
        Mockito.verify(this.listener, Mockito.times(1)).definitionDeleted(repository, definition);
        
        //
        // Deployment test
        //
        DeploymentBuilder deploymentBuilder = repository.getDeploymentBuilder();
        deploymentBuilder.addProcessDefinition(definition);
        Deployment deployment = deploymentBuilder.buildDeployment();
        
        repository.deployInNewScope(deployment);
        
        Mockito.verify(this.listener, Mockito.times(1)).deploymentDeployed(repository, deployment);
        Mockito.verify(this.listener, Mockito.never()).deploymentDeleted(repository, deployment);
        Mockito.verify(this.listener, Mockito.times(2)).definitionDeployed(repository, definition);
        
        //
        // Artifact test
        //
        AbstractProcessArtifact artifact = new ProcessArtifact("artifact", null);
        repository.addProcessArtifact(artifact, definition.getID());
        Assert.assertNotNull(repository.getProcessArtifact(artifact.getID(), definition.getID()));
        repository.deleteProcessArtifact(artifact.getID(), definition.getID());
        
        Mockito.verify(this.listener, Mockito.times(1)).artifactDeployed(repository, artifact);
        Mockito.verify(this.listener, Mockito.times(1)).artifactDeleted(repository, artifact);
    }
}
