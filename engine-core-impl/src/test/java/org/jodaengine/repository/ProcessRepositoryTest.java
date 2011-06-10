package org.jodaengine.repository;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.UUID;

import org.jodaengine.RepositoryService;
import org.jodaengine.RepositoryServiceImpl;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.process.definition.BpmnProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * Test implementation for {@link RepositoryService} class.
 * 
 * @author Jan Rehwaldt
 */
public class ProcessRepositoryTest {

    private static final UUID EMPTY_UUID = UUID.fromString("00000000-0000-002a-0000-00000000002a");
    private static final ProcessDefinitionID PROCESS_ID = new ProcessDefinitionID(EMPTY_UUID.toString(), 0);

    private RepositoryService repository = null;

    /**
     * Setup method.
     */
    @BeforeMethod
    public void setUp() {

        this.repository = new RepositoryServiceImpl(null);
    }

    /**
     * Tear down method.
     */
    @AfterMethod
    public void tearDown() {

        this.repository = null;
    }

    /**
     * Tests the throwing of an appropriate exception.
     * 
     * @throws DefinitionNotFoundException
     *             expected
     */
    @Test(expectedExceptions = DefinitionNotFoundException.class)
    public void testNoDefinitionException()
    throws DefinitionNotFoundException {

        assertFalse(this.repository.containsProcessDefinition(PROCESS_ID));
        this.repository.getProcessDefinition(PROCESS_ID);
    }

    /**
     * Tests the addition of a definition.
     * 
     * @throws DefinitionNotFoundException
     *             fails test
     */
    @Test
    public void testAddDefinition()
    throws DefinitionNotFoundException {

        final ProcessDefinition def = new BpmnProcessDefinition(PROCESS_ID, null, null, null);
        assertFalse(this.repository.containsProcessDefinition(PROCESS_ID));
        
        DeploymentBuilder builder = this.repository.getDeploymentBuilder();
        builder.addProcessDefinition(def);
        this.repository.deployInNewScope(builder.buildDeployment());

        assertTrue(this.repository.containsProcessDefinition(PROCESS_ID));
        assertEquals(def, this.repository.getProcessDefinition(PROCESS_ID));
    }

    /**
     * Tests the deletion of a definition.
     * 
     * @throws DefinitionNotFoundException
     *             fails test
     */
    @Test
    public void testDeleteDefinition()
    throws DefinitionNotFoundException {

        final ProcessDefinition def = new BpmnProcessDefinition(PROCESS_ID, null, null, null);
        assertFalse(this.repository.containsProcessDefinition(PROCESS_ID));

        DeploymentBuilder builder = this.repository.getDeploymentBuilder();
        Deployment deployment = builder.addProcessDefinition(def).buildDeployment();
        this.repository.deployInNewScope(deployment);

        assertTrue(this.repository.containsProcessDefinition(PROCESS_ID));
        this.repository.deleteProcessDefinition(PROCESS_ID);
        assertFalse(this.repository.containsProcessDefinition(PROCESS_ID));
    }
   
}
