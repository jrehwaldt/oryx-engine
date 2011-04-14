package de.hpi.oryxengine.deploy;

import java.util.UUID;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.RepositoryService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.factory.definition.ProcessDefinitionFactory;
import de.hpi.oryxengine.factory.definition.SimpleProcessDefinitionFactory;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.repository.DeploymentBuilder;
import de.hpi.oryxengine.repository.importer.RawProcessDefintionImporter;

/**
 * The Class DeployerTest.
 */
@ContextConfiguration(locations = "/test.oryxengine.cfg.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DeploymentBuilderTest extends AbstractTestNGSpringContextTests {

    private DeploymentBuilder deploymentBuilder = null;
    private RepositoryService repo = null;
    private ProcessDefinition def = null;
    private UUID defID = null;

    /**
     * Sets the up.
     */
    @BeforeClass
    public void setUp() {

        repo = ServiceFactory.getRepositoryService();
        deploymentBuilder = repo.getDeploymentBuilder();
        ProcessDefinitionFactory factory = new SimpleProcessDefinitionFactory();
        defID = UUID.randomUUID();
        def = factory.create(defID);

    }

    /**
     * Test deployment.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testDeployment()
    throws Exception {

        // Best Regards Tom Baeyens
        deploymentBuilder.deployProcessDefinition(new RawProcessDefintionImporter(def));
        Assert.assertEquals(repo.getProcessDefinition(defID), def,
                            "The deployed process definition should be avaialable in the repository.");
    }
}
