package org.jodaengine.deployment;

import java.util.UUID;

import org.jodaengine.RepositoryService;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.instantiation.StartInstantiationPattern;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the class {@link Deployment} and the DeploymentBuilder deploying it.
 */
public class DeploymentTest extends AbstractJodaEngineTest {

    private RepositoryService repository;
    private DeploymentBuilder builder;
    private ProcessDefinitionBuilder defBuilder;

    @BeforeMethod
    public void setUp() {

        repository = jodaEngineServices.getRepositoryService();
        builder = repository.getDeploymentBuilder();
        defBuilder = builder.getProcessDefinitionBuilder();

    }

    @Test
    public void testProcessDefinitionDeployment()
    throws IllegalStarteventException {

        ProcessDefinitionID id = new ProcessDefinitionID(UUID.randomUUID());
        defBuilder.addStartInstantiationPattern(Mockito.mock(StartInstantiationPattern.class));

        ProcessDefinition definition = defBuilder.buildDefinition();
        Whitebox.setInternalState(definition, "id", id);
        builder.addProcessDefinition(definition);
        Deployment deployment = builder.buildDeployment();
        DeploymentScope scope = repository.deployInNewScope(deployment);

        Assert.assertTrue(repository.containsProcessDefinition(id));
       
    }

    @Test
    public void testAutomatedVersioning()
    throws IllegalStarteventException {

        UUID processUUID = UUID.randomUUID();

        ProcessDefinitionID id = new ProcessDefinitionID(processUUID);
        defBuilder.addStartInstantiationPattern(Mockito.mock(StartInstantiationPattern.class));
        ProcessDefinition definition = defBuilder.buildDefinition();

        // we have to manipulate the id for our test case.
        Whitebox.setInternalState(definition, "id", id);
        builder.addProcessDefinition(definition);

        Deployment deployment = builder.buildDeployment();
        repository.deployInNewScope(deployment);

        ProcessDefinitionID anotherID = new ProcessDefinitionID(processUUID);
        defBuilder.addStartInstantiationPattern(Mockito.mock(StartInstantiationPattern.class));
        ProcessDefinition anotherDefinition = defBuilder.buildDefinition();

        // we have to manipulate the id for our test case.
        Whitebox.setInternalState(anotherDefinition, "id", anotherID);
        builder.addProcessDefinition(anotherDefinition);

        Deployment anotherDeployment = builder.buildDeployment();
        repository.deployInNewScope(anotherDeployment);

        Assert.assertEquals(definition.getID().getVersion(), 0, "The first deployment should have version 0");
        Assert.assertEquals(anotherDefinition.getID().getVersion(), 1,
            "The second deployment should have version 1, as it has the same id");

    }

    /**
     * Tests that a process scope is created when a deployment is deployed.
     */
    @Test
    public void testScopeDeployment() {

    }

}
