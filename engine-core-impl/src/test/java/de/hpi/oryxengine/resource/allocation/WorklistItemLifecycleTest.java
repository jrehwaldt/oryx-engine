package de.hpi.oryxengine.resource.allocation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.WorklistService;
import de.hpi.oryxengine.allocation.CreationPattern;
import de.hpi.oryxengine.factory.worklist.CreationPatternFactory;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemImpl;
import de.hpi.oryxengine.resource.worklist.WorklistItemState;
import de.hpi.oryxengine.util.testing.AbstractJodaEngineTest;

/**
 * Checking the Lifecycle of a WorklistItem.
 * 
 * This test simulates the usages of a {@link AbstractWorklistItem}.
 */
public class WorklistItemLifecycleTest extends AbstractJodaEngineTest {

    private WorklistService worklistService = null;
    private AbstractWorklistItem worklistItem = null;
    private AbstractParticipant jannik = null;

    /**
     * Set up.
     */
    @BeforeMethod
    public void setUp() {

        worklistService = ServiceFactory.getWorklistService();

        CreationPattern pattern = CreationPatternFactory.createJannikServesGerardoCreator();
        jannik = (Participant) pattern.getAssignedResources()[0];

        Token token = Mockito.mock(Token.class);
        Set<AbstractResource<?>> resources = new HashSet(Arrays.asList(pattern.getAssignedResources()));

        worklistItem = new WorklistItemImpl(pattern.getItemSubject(), pattern.getItemDescription(), pattern.getItemForm(), resources, token);

        ServiceFactory.getWorklistQueue().addWorklistItem(worklistItem, jannik);
    }

    /**
     * Tear down.
     */
    @AfterMethod
    public void tearDown() {

//        ServiceFactoryForTesting.clearWorklistManager();
//        ServiceFactoryForTesting.clearIdentityService();
    }

    /**
     * Test creation of a worklist item.
     */
    @Test
    public void testWorklistItemCreation() {

        AbstractParticipant part = ServiceFactory.getIdentityService().getParticipants().iterator().next();
        // AllocationsStragegies are not important for that test
//        Task task = new TaskImpl("Task Subject!!", "Task Decription!!", null, part);

        Token token = Mockito.mock(Token.class);
        Set<AbstractResource<?>> assignedResources = new HashSet<AbstractResource<?>>();
        assignedResources.add(part);

        AbstractWorklistItem worklistItemForGerardo = new WorklistItemImpl("Task Subject!!", "Task Decription!!", null, assignedResources, token);

        Assert.assertEquals(worklistItemForGerardo.getSubject(), "Task Subject!!");
        Assert.assertEquals(worklistItemForGerardo.getDescription(), "Task Decription!!");
        Assert.assertEquals(worklistItemForGerardo.getCorrespondingToken(), token);
        Assert.assertEquals(worklistItemForGerardo.getStatus(), WorklistItemState.OFFERED);

        // Testing that the creation of a WorklistItem requires a Token
        try {

            worklistItemForGerardo = new WorklistItemImpl("Task Subject!!", "Task Decription!!", null, assignedResources, null);
            String failureMessage = "An NullPointerException should have occurred, "
                + "because the WorklistItem was created without a Token.";
            Assert.fail(failureMessage);
        } catch (NullPointerException nullPointerException) {
            // This was expected
        }

        // Testing that the creation of a WorklistItem requires a Task
//        try {
//
//            worklistItemForGerardo = new WorklistItemImpl(task, null);
//            String failureMessage = "An NullPointerException should have occurred, "
//                + "because the WorklistItem was created without a Task.";
//            Assert.fail(failureMessage);
//        } catch (NullPointerException nullPointerException) {
//            // This was expected
//        }
    }

    /**
     * Tests claiming a work list item.
     */
    @Test
    public void testClaimingWorklistItem() {

        worklistService.claimWorklistItemBy(worklistItem, jannik);

        Assert.assertEquals(worklistItem.getStatus(), WorklistItemState.ALLOCATED);
    }

    /**
     * Tests beginning a worklist item.
     */
    @Test
    public void testBeginningWorklistItem()  {

        worklistService.beginWorklistItemBy(worklistItem, jannik);

        Assert.assertEquals(worklistItem.getStatus(), WorklistItemState.EXECUTING);
    }

    /**
     * Tests aborting a worklist item.
     */
    @Test
    public void testAbortAfterBeginningWorklistItem()  {

        worklistService.beginWorklistItemBy(worklistItem, jannik);
        worklistService.abortWorklistItemBy(worklistItem, jannik);

        // TODO Assertions are missing
    }

    /**
     * Test completing worklist items.
     */
    @Test
    public void testCompletingWorklistItem() {

        worklistService.beginWorklistItemBy(worklistItem, jannik);
        worklistService.completeWorklistItemBy(worklistItem, jannik);

        Assert.assertEquals(worklistItem.getStatus(), WorklistItemState.COMPLETED);
        Assert.assertTrue(worklistService.getWorklistItems(jannik).size() == 0);
    }
}
