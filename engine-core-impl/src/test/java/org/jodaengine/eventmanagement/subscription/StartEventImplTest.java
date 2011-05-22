package org.jodaengine.eventmanagement.subscription;

import org.jodaengine.deployment.Deployment;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.node.factory.TransitionFactory;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the {@link StartEventImpl}.
 */
public class StartEventImplTest extends AbstractJodaEngineTest {

    private ProcessDefinitionID processDefinitionID;

    //
    /**
     * Test process event triggering.
     * 
     * @throws DefinitionNotFoundException
     * @throws InterruptedException
     */
    @Test
    public void testProcessEventTriggering()
    throws DefinitionNotFoundException, InterruptedException {

        // Bind the startEvent to a deployed processDefinition
        StartEventImpl startEvent = new StartEventImpl(null, null, null, processDefinitionID);

        Assert.assertNotNull(startEvent.getDefinitionID());

        Assert.assertEquals(jodaEngineServices.getNavigatorService().getEndedInstances().size(), 0);

        // Trigger should start a processInstance of the deployed process
        startEvent.trigger();

        // Let the processInstance finish
        Thread.sleep(1000);
        Assert.assertEquals(jodaEngineServices.getNavigatorService().getEndedInstances().size(), 1);
    }

    @BeforeMethod
    public void setUp()
    throws IllegalStarteventException {

        ProcessDefinition processDefinition = buildLittleProcessDefinition();
        processDefinitionID = processDefinition.getID();
        Deployment deployment = jodaEngineServices.getRepositoryService().getDeploymentBuilder()
        .addProcessDefinition(processDefinition).buildDeployment();

        jodaEngineServices.getRepositoryService().deployInNewScope(deployment);

    }

    /**
     * Build a little process that should be started by the {@link ProcessEvent}.
     * 
     * @return the built {@link ProcessDefinition}
     */
    private ProcessDefinition buildLittleProcessDefinition()
    throws IllegalStarteventException {

        ProcessDefinitionBuilder definitionBuilder = jodaEngineServices.getRepositoryService().getDeploymentBuilder()
        .getProcessDefinitionBuilder();
        TransitionFactory.createTransitionFromTo(definitionBuilder,
            BpmnCustomNodeFactory.createBpmnNullStartNode(definitionBuilder),
            BpmnNodeFactory.createBpmnEndEventNode(definitionBuilder));

        BpmnProcessDefinitionModifier.decorateWithDefaultBpmnInstantiationPattern(definitionBuilder);
        return definitionBuilder.buildDefinition();
    }

}
