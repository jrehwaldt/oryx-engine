package de.hpi.oryxengine.resource.allocation;

import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.AbstractJodaEngineTest;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.impl.HumanTaskActivity;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.factory.node.GerardoNodeFactory;
import de.hpi.oryxengine.factory.worklist.TaskFactory;
import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.ActivityBlueprint;
import de.hpi.oryxengine.process.structure.ActivityBlueprintImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemState;

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
        Task task = TaskFactory.createJannikServesGerardoTask();
        jannik = task.getAssignedResources().iterator().next();

        Class<?>[] constructorSig = {Task.class};
        Object[] params = {task};
        ActivityBlueprint bp = new ActivityBlueprintImpl(HumanTaskActivity.class, constructorSig, params);
        Node humanTaskNode = GerardoNodeFactory.createSimpleNodeWith(bp);

        Class<?>[] emptyConstructorSig = {};
        Object[] emtpyParams = {};
        bp = new ActivityBlueprintImpl(HumanTaskActivity.class, emptyConstructorSig, emtpyParams);
        endNode = GerardoNodeFactory.createSimpleNodeWith(bp);
        
        humanTaskNode.transitionTo(endNode);
                
        AbstractProcessInstance instance = new ProcessInstanceImpl(null);
        token = new TokenImpl(humanTaskNode, instance, new NavigatorImplMock());
    }

    /**
     * Tear Down.
     */
    @AfterMethod
    public void tearDown() {
//        ServiceFactoryForTesting.clearWorklistManager();
//        ServiceFactoryForTesting.clearIdentityService();
    }
    
    /**
     * Test that the assigned user begins with the humanTask.
     * @throws DalmatinaException 
     */
    @Test
    public void testJannikBeginsTheWorkItem()
    throws DalmatinaException {
        
        token.executeStep();
        
        AbstractWorklistItem worklistItem = ServiceFactory.getWorklistService().getWorklistItems(jannik).get(0);
        assertEquals(worklistItem.getStatus(), WorklistItemState.ALLOCATED);

        ServiceFactory.getWorklistService().beginWorklistItemBy(worklistItem, jannik);
        assertEquals(worklistItem.getStatus(), WorklistItemState.EXECUTING);
    }

    /**
     * Test that the assigned user completes with the humanTask.
     * @throws DalmatinaException 
     */
    @Test
    public void testJannikCompletesTheWorkItem()
    throws DalmatinaException {
        
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
     * @throws DalmatinaException test fails
     */
    @Test
    public void testResumptionOfProcess()
    throws DalmatinaException {

        token.executeStep();
        
        AbstractWorklistItem worklistItem = ServiceFactory.getWorklistService().getWorklistItems(jannik).get(0);
        ServiceFactory.getWorklistService().beginWorklistItemBy(worklistItem, jannik);
        
        ServiceFactory.getWorklistService().completeWorklistItemBy(worklistItem, jannik);
        
        String failureMessage =
            "Token should point to the endNode, but it points to " + token.getCurrentNode().getID() + ".";
        assertEquals(endNode, token.getCurrentNode(), failureMessage);
    }
}
