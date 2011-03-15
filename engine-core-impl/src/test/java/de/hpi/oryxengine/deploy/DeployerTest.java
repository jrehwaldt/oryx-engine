package de.hpi.oryxengine.deploy;

import static org.testng.Assert.assertEquals;

import java.util.UUID;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.factory.definition.ProcessDefinitionFactory;
import de.hpi.oryxengine.factory.definition.SimpleProcessDefinitionFactory;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.repository.ProcessRepository;
import de.hpi.oryxengine.repository.ProcessRepositoryImpl;

/**
 * The Class DeployerTest.
 */
public class DeployerTest {

    private Deployer deployer;
    private ProcessRepository repo;
    private ProcessDefinition def;
    private UUID defID;

    /**
     * Sets the up.
     */
    @BeforeClass
    public void setUp() {

        deployer = new DeployerImpl();
        repo = ProcessRepositoryImpl.getInstance();
        ProcessDefinitionFactory factory = new SimpleProcessDefinitionFactory();
        defID = UUID.randomUUID();
        def = factory.create(defID);

    }

    /**
     * Test deployment.
     *
     * @throws Exception the exception
     */
    @Test
    public void testDeployment()
    throws Exception {

        deployer.deploy(def);
        assertEquals(repo.getDefinition(defID), def,
            "The deployed process definition should be avaialable in the repository.");
    }
}
