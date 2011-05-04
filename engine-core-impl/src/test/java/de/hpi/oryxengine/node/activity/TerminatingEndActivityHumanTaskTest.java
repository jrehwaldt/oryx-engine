package de.hpi.oryxengine.node.activity;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.AbstractJodaEngineTest;
import de.hpi.oryxengine.IdentityServiceImpl;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.allocation.AllocationStrategies;
import de.hpi.oryxengine.allocation.Pattern;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.exception.JodaEngineException;
import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.node.activity.bpmn.BpmnHumanTaskActivity;
import de.hpi.oryxengine.node.factory.bpmn.BpmnFunNodeFactory;
import de.hpi.oryxengine.node.factory.bpmn.BpmnNodeFactory;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.allocation.AllocationStrategiesImpl;
import de.hpi.oryxengine.resource.allocation.TaskImpl;
import de.hpi.oryxengine.resource.allocation.pattern.DirectPushPattern;
import de.hpi.oryxengine.resource.allocation.pattern.SimplePullPattern;

/**
 * The Class TerminatingEndActivityTest.
 */
public class TerminatingEndActivityHumanTaskTest extends AbstractJodaEngineTest {
    private Task task = null;
    private AbstractResource<?> resource = null;
    private Node splitNode, humanTaskNode, terminatingEndNode;

    /**
     * Test cancelling of human tasks. A simple fork is created that leads to a human task activity and a terminating
     * end. Then the human task activity is executed and a worklist item created. We expect the TerminatingEndActivity
     * to remove the worklist item from the corresponding worklists.
     * 
     * @throws JodaEngineException
     *             the dalmatina exception
     */
    @Test
    public void testCancellingOfHumanTasks()
    throws JodaEngineException {

        AbstractProcessInstance instance = new ProcessInstanceImpl(null);
        NavigatorImplMock nav = new NavigatorImplMock();
        Token token = instance.createToken(splitNode, nav);

        // set this instance to running by hand
        nav.getRunningInstances().add(instance);

        token.executeStep();
        
        assertEquals(nav.getWorkQueue().size(), 2, "Split Node should have created two tokens.");

        // get the token that is on the human task activity. The other one is on the end node then.
        Token humanTaskToken = nav.getWorkQueue().get(0);
        Token endToken = nav.getWorkQueue().get(1);
        if (!(humanTaskToken.getCurrentNode().getActivityBlueprint().getActivityClass() == BpmnHumanTaskActivity.class)) {
            humanTaskToken = endToken;
            endToken = nav.getWorkQueue().get(0);
        }

        humanTaskToken.executeStep();

        assertEquals(ServiceFactory.getWorklistService().getWorklistItems(resource).size(), 1,
            "there should be one offered worklist item.");

        endToken.executeStep();
        assertEquals(ServiceFactory.getWorklistService().getWorklistItems(resource).size(), 0,
            "there should be no offered worklist items anymore.");

        assertEquals(instance.getAssignedTokens().size(), 0, "There should be no tokens assigned to this instance.");
        assertTrue(nav.getEndedInstances().contains(instance), "The instance should be now marked as finished.");
        assertFalse(nav.getRunningInstances().contains(instance), "The instance should not be marked as running.");
    }

    /**
     * Sets the up human task.
     * 
     * @throws Exception
     *             the exception
     */
    @BeforeClass
    public void setUpHumanTask()
    throws Exception {

        // Prepare the organisation structure

        IdentityBuilder identityBuilder = new IdentityServiceImpl().getIdentityBuilder();
        AbstractParticipant participant = identityBuilder.createParticipant("jannik");
        participant.setName("Jannik Streek");

        resource = participant;

        // Define the task
        String subject = "Jannik, get Gerardo a cup of coffee!";
        String description = "You know what I mean.";

        Pattern pushPattern = new DirectPushPattern();
        Pattern pullPattern = new SimplePullPattern();

        AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(pushPattern, pullPattern, null, null);

        task = new TaskImpl(subject, description, allocationStrategies, participant);
    }

    /**
     * Sets the up nodes.
     */
    @BeforeClass
    public void setUpProcessInstance() {

        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();

        splitNode = BpmnFunNodeFactory.createBpmnNullNode(builder);

        // param.setActivity(humanTask); TODO do something with the parameter of humanTask
        humanTaskNode = BpmnNodeFactory.createBpmnUserTaskNode(builder, task);

        terminatingEndNode = BpmnNodeFactory.createBpmnTerminatingEndEventNode(builder);

        BpmnNodeFactory.createTransitionFromTo(builder, splitNode, humanTaskNode);
        BpmnNodeFactory.createTransitionFromTo(builder, splitNode, terminatingEndNode);
    }
}
