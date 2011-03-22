package de.hpi.oryxengine.worklist;

import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.ServiceFactoryForTesting;
import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.HumanTaskActivity;
import de.hpi.oryxengine.factory.node.GerardoNodeFactory;
import de.hpi.oryxengine.factory.worklist.TaskFactory;
import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.process.instance.ProcessInstanceContextImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.resource.Resource;

/**
 * This test assigns a task directly to a participant. 
 */
public class AssigningToParticipantUserStoryTest {

    Token token;
    Resource<?> jannik;
    Node endNode;
    
    @BeforeMethod
    public void setUp()
    throws Exception {

        // The organization structure is already prepared in the factory
        // The task is assigned to Jannik
        Task task = TaskFactory.createJannikServesGerardoTask();
        jannik = task.getAssignedResources().get(0);

        Activity humanTaskActivity = new HumanTaskActivity(task);
        Node humanTaskNode = GerardoNodeFactory.createSimpleNodeWith(humanTaskActivity);

        AbstractActivity endactivity = new EndActivity();
        endNode = GerardoNodeFactory.createSimpleNodeWith(endactivity);
        
        humanTaskNode.transitionTo(endNode);
                
        token = new TokenImpl(humanTaskNode, new ProcessInstanceContextImpl(), new NavigatorImplMock());
    }

    @AfterMethod
    public void tearDown() {
        ServiceFactoryForTesting.clearWorklistManager();
    }
    
    /**
     * Test that the assigned user begins with the humanTask.
     * @throws Exception 
     */
    @Test
    public void testJannikBeginsTheWorkItem() throws Exception {
        
        token.executeStep();
        
        WorklistItem worklistItem = ServiceFactory.getWorklistService().getWorklistItems(jannik).get(0);
        assertEquals(worklistItem.getStatus(), WorklistItemState.ALLOCATED);

        ServiceFactory.getWorklistService().beginWorklistItemBy(worklistItem, jannik);
        assertEquals(worklistItem.getStatus(), WorklistItemState.EXECUTING);
    }

    /**
     * Test that the assigned user completes with the humanTask.
     * @throws Exception 
     */
    @Test
    public void testJannikCompletesTheWorkItem() throws Exception {
        
        token.executeStep();
        
        WorklistItem worklistItem = ServiceFactory.getWorklistService().getWorklistItems(jannik).get(0);
        ServiceFactory.getWorklistService().beginWorklistItemBy(worklistItem, jannik);
        assertEquals(worklistItem.getStatus(), WorklistItemState.EXECUTING);
        
        ServiceFactory.getWorklistService().completeWorklistItemBy(worklistItem,jannik);
        assertEquals(worklistItem.getStatus(), WorklistItemState.COMPLETED);
        String failureMessage = "Jannik should have completed the task. So there should be no item in his worklist.";
        Assert.assertTrue(ServiceFactory.getWorklistService().getWorklistItems(jannik).size() == 0, failureMessage);
    }
    
    @Test
    public void testResumptionOfProcess() throws Exception {

        token.executeStep();
        
        WorklistItem worklistItem = ServiceFactory.getWorklistService().getWorklistItems(jannik).get(0);
        ServiceFactory.getWorklistService().beginWorklistItemBy(worklistItem, jannik);
        
        ServiceFactory.getWorklistService().completeWorklistItemBy(worklistItem, jannik);
        
        String failureMessage = "Token should point to the endNode, but it points to " + token.getCurrentNode().getID() + ".";
        assertEquals(endNode, token.getCurrentNode(), failureMessage);
    }
}
