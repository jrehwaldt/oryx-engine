package de.hpi.oryxengine.factory.process;

import static org.testng.Assert.assertNotNull;

import java.util.UUID;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.definition.ProcessDefinition;

/**
 * Tests the EcampleProcessDeplyoer class.
 */
@ContextConfiguration(locations = "/test.oryxengine.cfg.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ExampleProcessDeployerTest extends AbstractTestNGSpringContextTests {

    private ProcessDeployer deployer;
    private UUID uuid;

    /**
     * Sets the up.
     * If something gos wrong here something in the Process deplyoer is REALLY off.
     * @throws IllegalStarteventException 
     */
    @BeforeClass
    public void setUp() throws IllegalStarteventException {

        this.deployer = new ExampleProcessDeployer();
        this.uuid = deployer.deploy();
    }

    /**
     * Tests that the UUID after the deployment isn't null.
     * @throws IllegalStarteventException in case something went wrong in the process instances
     */
    @Test
    public void testDeployment() throws IllegalStarteventException {
        assertNotNull(uuid);
        
    }
    
    /**
     * Tests that the process definition may be retrieved from the repository.
     * @throws DefinitionNotFoundException when the definition is not found by its UUID
     */
    @Test
    public void retrieveProcessDefinitionTest() throws DefinitionNotFoundException {
        ProcessDefinition definition = ServiceFactory.getRepositoryService().getDefinition(uuid);
        assertNotNull(definition);
    }
}
