package org.jodaengine.resource.allocation.pattern.creation;

import org.jodaengine.factory.resource.ParticipantFactory;
import org.jodaengine.process.token.Token;
import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.AbstractRole;
import org.jodaengine.resource.allocation.pattern.push.AllocateSinglePattern;
import org.jodaengine.resource.allocation.pattern.push.OfferMultiplePattern;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemState;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The Class CreationPatternTest.
 */
public class CreationPatternTest extends AbstractJodaEngineTest {

    private AbstractParticipant participant = null;
    private AbstractRole role = null;
    private static final String SIMPLE_TASK_SUBJECT = "Subject";
    private static final String SIMPLE_TASK_DESCRIPTION = "Description";
    private static final String ROLE_NAME = "role";

    /**
     * Set up.
     */
    @BeforeMethod
    public void setUp() {

        participant = ParticipantFactory.createJannik();
        role = jodaEngineServices.getIdentityService().getIdentityBuilder().createRole(ROLE_NAME);
    }

    /**
     * Test the instantiation of the direct distribution pattern and the correct creation of an item for it.
     */
    @Test
    public void testDirectDistributionPatternAndItem() {

        // test correct creation of the pattern
        AbstractCreationPattern pattern = new DirectDistributionPattern(
            SIMPLE_TASK_SUBJECT, 
            SIMPLE_TASK_DESCRIPTION,
            null, 
            participant);
        Assert.assertEquals(pattern.getItemSubject(), SIMPLE_TASK_SUBJECT);
        Assert.assertEquals(pattern.getItemDescription(), SIMPLE_TASK_DESCRIPTION);
        Assert.assertEquals(pattern.getAssignedResources().iterator().next(), participant);
        Assert.assertEquals(pattern.getPushPattern().getClass(), AllocateSinglePattern.class);
        
        // test the correct functionality of creation method for items of the pattern
        Token token = Mockito.mock(Token.class);
        AbstractWorklistItem item = pattern.createWorklistItem(token, jodaEngineServices.getRepositoryService());
        Assert.assertEquals(item.getSubject(), SIMPLE_TASK_SUBJECT);
        Assert.assertEquals(item.getDescription(), SIMPLE_TASK_DESCRIPTION);
        Assert.assertEquals(item.getAssignedResources().iterator().next(), participant);
        Assert.assertEquals(item.getCorrespondingToken(), token);
        Assert.assertEquals(item.getStatus(), WorklistItemState.CREATED);
    }
    
    /**
     * Test the instantiation of the role based distribution pattern and the correct creation of an item for it.
     */
    @Test
    public void testRoleBasedDistributionPattern() {
        // test correct creation of the pattern
        AbstractCreationPattern pattern = new RoleBasedDistributionPattern(
            SIMPLE_TASK_SUBJECT, 
            SIMPLE_TASK_DESCRIPTION,
            null, 
            role);
        Assert.assertEquals(pattern.getItemSubject(), SIMPLE_TASK_SUBJECT);
        Assert.assertEquals(pattern.getItemDescription(), SIMPLE_TASK_DESCRIPTION);
        Assert.assertEquals(pattern.getAssignedResources().iterator().next(), role);
        Assert.assertEquals(pattern.getPushPattern().getClass(), OfferMultiplePattern.class);
        
        // test the correct functionality of creation method for items of the pattern
        Token token = Mockito.mock(Token.class);
        AbstractWorklistItem item = pattern.createWorklistItem(token, jodaEngineServices.getRepositoryService());
        Assert.assertEquals(item.getSubject(), SIMPLE_TASK_SUBJECT);
        Assert.assertEquals(item.getDescription(), SIMPLE_TASK_DESCRIPTION);
        Assert.assertEquals(item.getAssignedResources().iterator().next(), role);
        Assert.assertEquals(item.getCorrespondingToken(), token);
        Assert.assertEquals(item.getStatus(), WorklistItemState.CREATED);
    }
}
