package de.hpi.oryxengine.deploy;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

import java.util.UUID;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.factory.definition.ProcessDefinitionFactory;
import de.hpi.oryxengine.factory.definition.SimpleProcessDefinitionFactory;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.repository.ProcessRepository;

/**
 * The Class DeployerTest.
 */
public class DeployerTest {

    private Deployer deployer = null;
    private ProcessRepository repo = null;
    private ProcessDefinition def = null;
    private UUID defID = null;

    /**
     * Sets the up.
     */
    @BeforeClass
    public void setUp() {

        deployer = new DeployerImpl();
        repo = ServiceFactory.getRepositoryService();
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

        deployer.deploy(def, mock(Navigator.class));
        assertEquals(repo.getDefinition(defID), def,
            "The deployed process definition should be avaialable in the repository.");
    }
}
