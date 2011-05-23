package org.jodaengine.resource.allocation;

import java.util.HashSet;
import java.util.Set;

import org.jodaengine.ServiceFactory;
import org.jodaengine.WorklistService;
import org.jodaengine.allocation.Form;
import org.jodaengine.factory.worklist.CreationPatternFactory;
import org.jodaengine.process.token.BPMNToken;
import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.Participant;
import org.jodaengine.resource.allocation.pattern.ConcreteResourcePattern;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemImpl;
import org.jodaengine.resource.worklist.WorklistItemState;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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

        ConcreteResourcePattern pattern = CreationPatternFactory.createJannikServesGerardoCreator();
        jannik = (Participant) pattern.getAssignedResources().iterator().next();

        BPMNToken bPMNToken = Mockito.mock(BPMNToken.class);
        Set<AbstractResource<?>> resources = pattern.getAssignedResources();

        Form form = Mockito.mock(Form.class);
        worklistItem = new WorklistItemImpl(pattern.getItemSubject(), pattern.getItemDescription(),
            form, resources, bPMNToken);

        ServiceFactory.getWorklistQueue().addWorklistItem(worklistItem, jannik);
    }

    /**
     * Tear down.
     */
    @AfterMethod
    public void tearDown() {

        // ServiceFactoryForTesting.clearWorklistManager();
        // ServiceFactoryForTesting.clearIdentityService();
    }

    /**
     * Test creation of a worklist item.
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void testWorklistItemCreation() {

        AbstractParticipant part = ServiceFactory.getIdentityService().getParticipants().iterator().next();
        // AllocationsStragegies are not important for that test
        // Task task = new TaskImpl("Task Subject!!", "Task Decription!!", null, part);

        BPMNToken bPMNToken = Mockito.mock(BPMNToken.class);
        Set<AbstractResource<?>> assignedResources = new HashSet<AbstractResource<?>>();
        assignedResources.add(part);

        AbstractWorklistItem worklistItemForGerardo = new WorklistItemImpl("Task Subject!!", "Task Decription!!", null,
            assignedResources, bPMNToken);

        Assert.assertEquals(worklistItemForGerardo.getSubject(), "Task Subject!!");
        Assert.assertEquals(worklistItemForGerardo.getDescription(), "Task Decription!!");
        Assert.assertEquals(worklistItemForGerardo.getCorrespondingToken(), bPMNToken);
        Assert.assertEquals(worklistItemForGerardo.getStatus(), WorklistItemState.CREATED);

        // Testing that the creation of a WorklistItem requires a Token
        worklistItemForGerardo = new WorklistItemImpl("Task Subject!!", "Task Decription!!", null,
            assignedResources, null);
        String failureMessage = "An NullPointerException should have occurred, "
            + "because the WorklistItem was created without a Token.";
        Assert.fail(failureMessage);
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
    public void testBeginningWorklistItem() {

        worklistService.beginWorklistItemBy(worklistItem, jannik);
        Assert.assertEquals(worklistItem.getStatus(), WorklistItemState.EXECUTING);
    }

    /**
     * Tests aborting a worklist item.
     */
    @Test
    public void testAbortAfterBeginningWorklistItem() {

        worklistService.beginWorklistItemBy(worklistItem, jannik);
        Assert.assertEquals(worklistItem.getStatus(), WorklistItemState.EXECUTING);
        
        worklistService.abortWorklistItemBy(worklistItem, jannik);
        
        // TODO add assertions when abort is implemented
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
