package org.jodaengine.factories.process;

import static org.testng.Assert.assertNotNull;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.ServiceFactory;
import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Parent class for all other Process Deployers tests as otherwise it would be a lot of code duplication.
 * Child classes basically just overwrite the setUp-method to use different deployers.
 * 
 * The tests just run the code to verify that there are no erros. More specific tests are likely to break everything
 * you do a simple thing like adding one node to the process.
 */
public abstract class AbstractProcessDeployerTest extends AbstractJodaEngineTest {

    protected ProcessDeployer deployer;
    protected ProcessDefinitionID id;
    protected JodaEngineServices engineServices;

    /**
     * Sets up the specific Process Deployer.
     * If something gos wrong here something in the Process Deplyoer is REALLY off.
     * 
     * @throws IllegalStarteventException
     * @throws ResourceNotAvailableException
     */
    @BeforeMethod
    public void setUp() {

        engineServices = JodaEngine.start();

    }

    @AfterMethod
    public void tearDown() {

        engineServices.shutdown();
    }

    @BeforeMethod(dependsOnMethods = "setUp")
    abstract public void executeDeployer()
    throws IllegalStarteventException, ResourceNotAvailableException;

    /**
     * Tests that the UUID after the deployment isn't null.
     * 
     * @throws IllegalStarteventException
     *             in case something went wrong in the process instances
     */
    @Test
    public void testDeployment()
    throws IllegalStarteventException {

        assertNotNull(id);

    }

    /**
     * Tests that the process definition may be retrieved from the repository.
     * 
     * @throws DefinitionNotFoundException
     *             when the definition is not found by its UUID
     */
    @Test
    public void retrieveProcessDefinitionTest()
    throws DefinitionNotFoundException {

        ProcessDefinition definition = ServiceFactory.getRepositoryService().getProcessDefinition(id);
        assertNotNull(definition);
    }
}
