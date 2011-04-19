package de.hpi.oryxengine.factories.process;

import static org.testng.Assert.assertNotNull;

import java.util.UUID;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.AbstractJodaEngineTest;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.definition.ProcessDefinition;

/**
 * Parent class for all other Process Deployers tests as otherwise it would  be a lot of code duplication.
 * Child classes basically just overwrite the setUp-method to use different deployers.
 * 
 * The tests just run the code to verify that there are no erros. More specific tests are likely to break everything
 * you do a simple thing like adding one node to the process.
 */
public abstract class AbstractProcessDeployerTest extends AbstractJodaEngineTest {

    protected ProcessDeployer deployer;
    protected UUID uuid;

    /**
     * Sets up the specific Process Deployer.
     * If something gos wrong here something in the Process Deplyoer is REALLY off.
     * @throws IllegalStarteventException 
     */
    @BeforeMethod
    abstract public void setUp() throws IllegalStarteventException;

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
        ProcessDefinition definition = ServiceFactory.getRepositoryService().getProcessDefinition(uuid);
        assertNotNull(definition);
    }
}
