package org.jodaengine.resource.allocation.pattern.push;

import org.jodaengine.ServiceFactory;
import org.jodaengine.process.token.Token;
import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.AbstractRole;
import org.jodaengine.resource.IdentityBuilder;
import org.jodaengine.resource.allocation.PushPattern;
import org.jodaengine.resource.allocation.pattern.creation.AbstractCreationPattern;
import org.jodaengine.resource.allocation.pattern.creation.DirectDistributionPattern;
import org.jodaengine.resource.allocation.pattern.creation.RoleBasedDistributionPattern;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemState;
import org.jodaengine.resource.worklist.WorklistServiceIntern;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The Class PushPatternTest.
 */
public class PushPatternTest extends AbstractJodaEngineTest {
    
    private AbstractParticipant participant = null;
    private AbstractRole role = null;
    private static final String SIMPLE_TASK_SUBJECT = "Subject";
    private static final String SIMPLE_TASK_DESCRIPTION = "Description";
    private static final String ROLE_NAME = "role";
    private static final String PARTICIPANT_NAME = "participant";
    private WorklistServiceIntern worklistServiceIntern;
    

    /**
     * Set up.
     */
    @BeforeMethod
    public void setUp() {

        IdentityBuilder builder = jodaEngineServices.getIdentityService().getIdentityBuilder();
        participant = builder.createParticipant(PARTICIPANT_NAME);
        role = builder.createRole(ROLE_NAME);
        worklistServiceIntern = ServiceFactory.getInteralWorklistService();
    }

    /**
     * Test the allocate single pattern and the distribution.
     */
    @Test
    public void testAllocateSinglePattern() {
        AbstractCreationPattern creationPattern = new DirectDistributionPattern(
            SIMPLE_TASK_SUBJECT, 
            SIMPLE_TASK_DESCRIPTION,
            null, 
            participant);
        Token token = Mockito.mock(Token.class);
        AbstractWorklistItem item = 
            creationPattern.createWorklistItem(token, jodaEngineServices.getRepositoryService());
        PushPattern pushPattern = new AllocateSinglePattern();
        pushPattern.distributeItem(worklistServiceIntern, item);
        
        Assert.assertEquals(item.getStatus(), WorklistItemState.ALLOCATED);
        Assert.assertEquals(item.getAssignedResources().size(), 1);
        Assert.assertEquals(item.getAssignedResources().iterator().next(), participant);
        Assert.assertTrue(participant.getWorklist().getWorklistItems().contains(item));
    }
    
    /**
     * Test the offer multiple pattern and the distribution.
     */
    @Test
    public void testOfferMultiplePattern() {
        AbstractCreationPattern creationPattern = new RoleBasedDistributionPattern(
            SIMPLE_TASK_SUBJECT, 
            SIMPLE_TASK_DESCRIPTION,
            null, 
            role);
        Token token = Mockito.mock(Token.class);
        AbstractWorklistItem item = 
            creationPattern.createWorklistItem(token, jodaEngineServices.getRepositoryService());
        PushPattern pushPattern = new OfferMultiplePattern();
        pushPattern.distributeItem(worklistServiceIntern, item);
        
        Assert.assertEquals(item.getStatus(), WorklistItemState.OFFERED);
        Assert.assertEquals(item.getAssignedResources().size(), 1);
        Assert.assertEquals(item.getAssignedResources().iterator().next(), role);
        Assert.assertTrue(role.getWorklist().getWorklistItems().contains(item));
    }
}
