package org.jodaengine.resource.allocation;

import org.jodaengine.ServiceFactory;
import org.jodaengine.factory.resource.ParticipantFactory;
import org.jodaengine.process.token.Token;
import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.allocation.pattern.creation.DirectDistributionPattern;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemState;
import org.jodaengine.resource.worklist.WorklistService;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.Mockito;
import org.testng.Assert;
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
    private AbstractParticipant participant = null;
    public static final String SIMPLE_TASK_SUBJECT = "Subject";
    public static final String SIMPLE_TASK_DESCRIPTION = "Description";

    /**
     * Set up.
     */
    @BeforeMethod
    public void setUp() {

        worklistService = jodaEngineServices.getWorklistService();
        participant = ParticipantFactory.createJannik();
        Token token = Mockito.mock(Token.class);
        
        CreationPattern pattern = new DirectDistributionPattern(
            SIMPLE_TASK_SUBJECT, 
            SIMPLE_TASK_DESCRIPTION, 
            null, 
            participant);
        
        worklistItem = pattern.createWorklistItem(token, jodaEngineServices.getRepositoryService());

        ServiceFactory.getInteralWorklistService().addWorklistItem(worklistItem, participant);
    }

    /**
     * Tests claiming a work list item.
     */
    @Test
    public void testClaimingWorklistItem() {

        worklistService.claimWorklistItemBy(worklistItem, participant);
        Assert.assertEquals(worklistItem.getStatus(), WorklistItemState.ALLOCATED);
    }

    /**
     * Tests beginning a worklist item.
     */
    @Test
    public void testBeginningWorklistItem() {

        worklistService.beginWorklistItemBy(worklistItem, participant);
        Assert.assertEquals(worklistItem.getStatus(), WorklistItemState.EXECUTING);
        Assert.assertEquals(worklistItem.getAssignedResources().size(), 1);
        Assert.assertEquals(worklistItem.getAssignedResources().iterator().next(), participant);
    }

    /**
     * Tests aborting a worklist item with the primitive abort function.
     * An item has be either in state ALLOCATED or OFFERED afterwards.
     * The assigned resources depend on the defaultPattern that is specified in the
     * {@link WorklistItemImpl Worklist Manager}
     */
    @Test
    public void testAbortAfterBeginningWorklistItem() {

        worklistService.beginWorklistItemBy(worklistItem, participant);
        // if an item is aborted it must be either in state OFFERED or ALLOCATED 
        worklistService.abortWorklistItemBy(worklistItem, participant);
        WorklistItemState state = worklistItem.getStatus();
        Assert.assertTrue(state.equals(WorklistItemState.ALLOCATED) || state.equals(WorklistItemState.OFFERED));
    }

    /**
     * Test completing worklist items.
     */
    @Test
    public void testCompletingWorklistItem() {

        worklistService.beginWorklistItemBy(worklistItem, participant);
        worklistService.completeWorklistItemBy(worklistItem, participant);

        Assert.assertEquals(worklistItem.getStatus(), WorklistItemState.COMPLETED);
        Assert.assertTrue(worklistService.getWorklistItems(participant).size() == 0);
    }
}
