package de.hpi.oryxengine.worklist;

import java.util.ArrayList;

import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.ServiceFactoryForTesting;
import de.hpi.oryxengine.WorklistService;
import de.hpi.oryxengine.activity.impl.HumanTaskActivity;
import de.hpi.oryxengine.process.instance.ProcessInstanceContextImpl;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.worklist.pattern.SimplePullPattern;
import de.hpi.oryxengine.worklist.pattern.SimplePushPattern;

/**
 * Checking the Lifecycle of a WorklistItem.
 * 
 * This test simulates the usages of a {@link WorklistItem}.
 */
public class WorklistItemLifecycleTest {

    private WorklistService worklistService;
    private WorklistItem worklistItem;
    private Participant participantGerardo;

    @BeforeMethod
    public void setUp() {

        worklistService = ServiceFactory.getWorklistService();

        participantGerardo = ServiceFactory.getIdentityService()
                                           .getIdentityBuilder()
                                           .createParticipant("gerardo.navarro-suarez");
        participantGerardo.setName("Gerardo Navarro Suarez");

        Pattern pushPattern = new SimplePushPattern();
        Pattern pullPattern = new SimplePullPattern();

        AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(pushPattern, pullPattern, null, null);

        Task task = new TaskImpl("Task Subject!!", "Task Decription!!", allocationStrategies, participantGerardo);

        Token token = new TokenImpl(new NodeImpl(new HumanTaskActivity(null)), new ProcessInstanceContextImpl());
        Whitebox.setInternalState(token, "tempProcessingTokens", new ArrayList<Token>());
        
        worklistItem = new WorklistItemImpl(task, token);

        ServiceFactory.getWorklistQueue().addWorklistItem(worklistItem, participantGerardo);
    }

    @AfterMethod
    public void tearDown() {

        ServiceFactoryForTesting.clearWorklistManager();
    }

    @Test
    public void testWorklistItemCreation() {

        Participant participantGerardo = ServiceFactory.getIdentityService().getParticipants().iterator().next();
        // AllocationsStragegies are not important for that test
        Task task = new TaskImpl("Task Subject!!", "Task Decription!!", null, participantGerardo);

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

        worklistService.claimWorklistItem(worklistItem);

        Assert.assertEquals(worklistItem.getStatus(), WorklistItemState.ALLOCATED);
    }

    @Test
    public void testBeginningWorklistItem()
    throws Exception {

        worklistService.beginWorklistItem(worklistItem);

        Assert.assertEquals(worklistItem.getStatus(), WorklistItemState.EXECUTING);
    }

    @Test
    public void testAbortAfterBeginningWorklistItem()
    throws Exception {

        worklistService.beginWorklistItem(worklistItem);
        worklistService.abortWorklistItem(worklistItem);
    }

    @Test
    public void testCompletingWorklistItem()
    throws Exception {

        worklistService.beginWorklistItem(worklistItem);
        worklistService.completeWorklistItem(worklistItem);
        

        Assert.assertEquals(worklistItem.getStatus(), WorklistItemState.COMPLETED);
        Assert.assertTrue(worklistService.getWorklistItems(participantGerardo).size() == 0);
    }
}
