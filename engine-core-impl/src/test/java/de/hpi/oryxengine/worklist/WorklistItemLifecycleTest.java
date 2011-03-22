package de.hpi.oryxengine.worklist;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.ServiceFactoryForTesting;
import de.hpi.oryxengine.WorklistService;
import de.hpi.oryxengine.activity.impl.HumanTaskActivity;
import de.hpi.oryxengine.factory.node.GerardoNodeFactory;
import de.hpi.oryxengine.factory.worklist.TaskFactory;
import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.process.instance.ProcessInstanceContextImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.resource.Participant;

/**
 * Checking the Lifecycle of a WorklistItem.
 * 
 * This test simulates the usages of a {@link WorklistItem}.
 */
public class WorklistItemLifecycleTest {

    private WorklistService worklistService;
    private WorklistItem worklistItem;
    private Participant jannik;

    @BeforeMethod
    public void setUp() {

        worklistService = ServiceFactory.getWorklistService();

        Task task = TaskFactory.createJannikServesGerardoTask();
        jannik = (Participant) task.getAssignedResources().iterator().next();
        
        Node humanTaskNode = GerardoNodeFactory.createSimpleNodeWith(new HumanTaskActivity(null));
        Token token = new TokenImpl(humanTaskNode, new ProcessInstanceContextImpl(), new NavigatorImplMock());
        
        worklistItem = new WorklistItemImpl(task, token);

        ServiceFactory.getWorklistQueue().addWorklistItem(worklistItem, jannik);
    }

    @AfterMethod
    public void tearDown() {

        ServiceFactoryForTesting.clearWorklistManager();
        ServiceFactoryForTesting.clearIdentityService();
    }

    @Test
    public void testWorklistItemCreation() {

        Participant jannik = ServiceFactory.getIdentityService().getParticipants().iterator().next();
        // AllocationsStragegies are not important for that test
        Task task = new TaskImpl("Task Subject!!", "Task Decription!!", null, jannik);

        Token token = Mockito.mock(Token.class);

        WorklistItem worklistItemForGerardo = new WorklistItemImpl(task, token);

        Assert.assertEquals(worklistItemForGerardo.getSubject(), "Task Subject!!");
        Assert.assertEquals(worklistItemForGerardo.getDescription(), "Task Decription!!");
        Assert.assertEquals(worklistItemForGerardo.getCorrespondingToken(), token);
        Assert.assertEquals(worklistItemForGerardo.getStatus(), WorklistItemState.OFFERED);

        // Testing that the creation of a WorklistItem requires a Token
        try {

            worklistItemForGerardo = new WorklistItemImpl(task, null);
            String failureMessage = "An NullPointerException should have occurred, because the WorklistItem was created without a Token.";
            Assert.fail(failureMessage);
        } catch (NullPointerException nullPointerException) {
            // This was expected
        }

        // Testing that the creation of a WorklistItem requires a Task
        try {

            worklistItemForGerardo = new WorklistItemImpl(task, null);
            String failureMessage = "An NullPointerException should have occurred, because the WorklistItem was created without a Task.";
            Assert.fail(failureMessage);
        } catch (NullPointerException nullPointerException) {
            // This was expected
        }
    }

    @Test
    public void testClaimingWorklistItem()
    throws Exception {

        worklistService.claimWorklistItemBy(worklistItem, jannik);

        Assert.assertEquals(worklistItem.getStatus(), WorklistItemState.ALLOCATED);
    }

    @Test
    public void testBeginningWorklistItem()
    throws Exception {

        worklistService.beginWorklistItemBy(worklistItem, jannik);

        Assert.assertEquals(worklistItem.getStatus(), WorklistItemState.EXECUTING);
    }

    @Test
    public void testAbortAfterBeginningWorklistItem()
    throws Exception {

        worklistService.beginWorklistItemBy(worklistItem, jannik);
        worklistService.abortWorklistItemBy(worklistItem, jannik);
        
        // TODO Assertions are missing
    }

    @Test
    public void testCompletingWorklistItem()
    throws Exception {

        worklistService.beginWorklistItemBy(worklistItem, jannik);
        worklistService.completeWorklistItemBy(worklistItem, jannik);

        Assert.assertEquals(worklistItem.getStatus(), WorklistItemState.COMPLETED);
        Assert.assertTrue(worklistService.getWorklistItems(jannik).size() == 0);
    }
}
