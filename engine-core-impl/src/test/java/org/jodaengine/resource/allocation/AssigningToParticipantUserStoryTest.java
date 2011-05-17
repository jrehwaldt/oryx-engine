package org.jodaengine.resource.allocation;

import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.jodaengine.ServiceFactory;
import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.factory.node.SimpleNodeFactory;
import org.jodaengine.factory.worklist.CreationPatternFactory;
import org.jodaengine.navigator.NavigatorImplMock;
import org.jodaengine.node.activity.bpmn.BpmnHumanTaskActivity;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstanceImpl;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenImpl;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.allocation.pattern.AllocateSinglePattern;
import org.jodaengine.resource.allocation.pattern.ConcreteResourcePattern;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemState;
import org.jodaengine.util.testing.AbstractJodaEngineTest;

/**
 * This test assigns a task directly to a participant.
 */
public class AssigningToParticipantUserStoryTest extends AbstractJodaEngineTest {

    private Token token = null;
    private AbstractResource<?> jannik = null;
    private Node endNode = null;

    /**
     * Set up.
     */
    @BeforeMethod
    public void setUp() {

        // The organization structure is already prepared in the factory
        // The task is assigned to Jannik
        ConcreteResourcePattern pattern = CreationPatternFactory.createJannikServesGerardoCreator();
        jannik = pattern.getAssignedResources().iterator().next();;

        Node humanTaskNode = SimpleNodeFactory.createSimpleNodeWith(new BpmnHumanTaskActivity(pattern,
            new AllocateSinglePattern()));

        endNode = SimpleNodeFactory
        .createSimpleNodeWith(new BpmnHumanTaskActivity(pattern, new AllocateSinglePattern()));

        humanTaskNode.transitionTo(endNode);

        AbstractProcessInstance instance = new ProcessInstanceImpl(null);
        token = new TokenImpl(humanTaskNode, instance, new NavigatorImplMock());
    }

    /**
     * Test that the assigned user begins with the humanTask.
     * 
     * @throws JodaEngineException
     */
    @Test
    public void testJannikBeginsTheWorkItem()
    throws JodaEngineException {

        token.executeStep();

        AbstractWorklistItem worklistItem = ServiceFactory.getWorklistService().getWorklistItems(jannik).get(0);
        assertEquals(worklistItem.getStatus(), WorklistItemState.ALLOCATED);

        ServiceFactory.getWorklistService().beginWorklistItemBy(worklistItem, jannik);
        assertEquals(worklistItem.getStatus(), WorklistItemState.EXECUTING);
    }

    /**
     * Test that the assigned user completes with the humanTask.
     * 
     * @throws JodaEngineException
     */
    @Test
    public void testJannikCompletesTheWorkItem()
    throws JodaEngineException {

        token.executeStep();

        AbstractWorklistItem worklistItem = ServiceFactory.getWorklistService().getWorklistItems(jannik).get(0);
        ServiceFactory.getWorklistService().beginWorklistItemBy(worklistItem, jannik);
        assertEquals(worklistItem.getStatus(), WorklistItemState.EXECUTING);

        ServiceFactory.getWorklistService().completeWorklistItemBy(worklistItem, jannik);
        assertEquals(worklistItem.getStatus(), WorklistItemState.COMPLETED);
        String failureMessage = "Jannik should have completed the task. So there should be no item in his worklist.";
        Assert.assertTrue(ServiceFactory.getWorklistService().getWorklistItems(jannik).size() == 0, failureMessage);
    }

    /**
     * Test resume of a process instance.
     * 
     * @throws JodaEngineException
     *             test fails
     */
    @Test
    public void testResumptionOfProcess()
    throws JodaEngineException {

        token.executeStep();

        AbstractWorklistItem worklistItem = ServiceFactory.getWorklistService().getWorklistItems(jannik).get(0);
        ServiceFactory.getWorklistService().beginWorklistItemBy(worklistItem, jannik);

        ServiceFactory.getWorklistService().completeWorklistItemBy(worklistItem, jannik);

        String failureMessage = "Token should point to the endNode, but it points to " + token.getCurrentNode().getID()
            + ".";
        assertEquals(endNode, token.getCurrentNode(), failureMessage);
    }
}
