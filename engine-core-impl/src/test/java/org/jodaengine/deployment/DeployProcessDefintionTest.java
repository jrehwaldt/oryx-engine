package org.jodaengine.deployment;

import java.util.UUID;

import org.jodaengine.RepositoryService;
import org.jodaengine.ServiceFactory;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.factory.definition.ProcessDefinitionFactory;
import org.jodaengine.factory.definition.SimpleProcessDefinitionFactory;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * Test class for deploying a {@link ProcessDefinition}.
 */
public class DeployProcessDefintionTest extends AbstractJodaEngineTest {

    private DeploymentBuilder deploymentBuilder = null;
    private RepositoryService repo = null;
    private ProcessDefinition def = null;
    private ProcessDefinitionID defID = null;

    /**
     * Sets up the deployment builder, reppositoryservice and other useful variables.
     */
    @BeforeClass
    public void setUp() {

        repo = ServiceFactory.getRepositoryService();
        deploymentBuilder = repo.getDeploymentBuilder();
        ProcessDefinitionFactory factory = new SimpleProcessDefinitionFactory();
        defID = new ProcessDefinitionID(UUID.randomUUID(), 0);
        def = factory.create(defID);

    }

    /**
     * Testing that a deployed {@link ProcessDefinition} can be retrieved by a {@link RepositoryService}.
     *
     * @throws DefinitionNotFoundException the definition not found exception
     */
    @Test
    public void testDeployment()
    throws DefinitionNotFoundException {

        // Best Regards Tom Baeyens
        deploymentBuilder.addProcessDefinition(def);
        repo.deployInNewScope(deploymentBuilder.buildDeployment());
        Assert.assertEquals(repo.getProcessDefinition(defID), def,
            "The deployed process definition should be avaialable in the repository.");
    }
}
