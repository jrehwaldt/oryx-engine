package org.jodaengine.node.activity;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jodaengine.ServiceFactory;
import org.jodaengine.allocation.CreationPattern;
import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.navigator.NavigatorImplMock;
import org.jodaengine.node.activity.bpmn.BpmnHumanTaskActivity;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstanceImpl;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.IdentityBuilder;
import org.jodaengine.resource.allocation.pattern.AllocateSinglePattern;
import org.jodaengine.resource.allocation.pattern.ConcreteResourcePattern;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * The Class TerminatingEndActivityTest.
 */
public class TerminatingEndActivityHumanTaskTest extends AbstractJodaEngineTest {
    private CreationPattern pattern = null;
    private AbstractResource<?> resource = null;
    private Node splitNode, humanTaskNode, terminatingEndNode;

    /**
     * Test cancelling of human tasks. A simple fork is created that leads to a human task activity and a terminating
     * end. Then the human task activity is executed and a worklist item created. We expect the TerminatingEndActivity
     * to remove the worklist item from the corresponding worklists.
     * 
     * @throws JodaEngineException
     *             the joda engine exception
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
        if (!(humanTaskToken.getCurrentNode().getActivityBehaviour().getClass() == BpmnHumanTaskActivity.class)) {
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

        IdentityBuilder identityBuilder = jodaEngineServices.getIdentityService().getIdentityBuilder();
        AbstractParticipant participant = identityBuilder.createParticipant("jannik");
        participant.setName("Jannik Streek");

        resource = participant;

        // Define the task
        String subject = "Jannik, get Gerardo a cup of coffee!";
        String description = "You know what I mean.";

//        Pattern pushPattern = new DirectDistributionPattern();
//        Pattern pullPattern = new SimplePullPattern();
//
//        AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(pushPattern, pullPattern, null, null);
        pattern = new ConcreteResourcePattern(subject, description, null, participant);
    }

    /**
     * Sets the up nodes.
     */
    @BeforeClass
    public void setUpProcessInstance() {

        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();

        splitNode = BpmnCustomNodeFactory.createBpmnNullNode(builder);

        humanTaskNode = BpmnNodeFactory.createBpmnUserTaskNode(builder, pattern, new AllocateSinglePattern());

        terminatingEndNode = BpmnNodeFactory.createBpmnTerminatingEndEventNode(builder);

        BpmnNodeFactory.createTransitionFromTo(builder, splitNode, humanTaskNode);
        BpmnNodeFactory.createTransitionFromTo(builder, splitNode, terminatingEndNode);
    }
}
