package org.jodaengine.resource.allocation.pattern.detour;

import org.jodaengine.ServiceFactory;
import org.jodaengine.process.token.Token;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.IdentityBuilder;
import org.jodaengine.resource.allocation.pattern.creation.AbstractCreationPattern;
import org.jodaengine.resource.allocation.pattern.creation.DirectDistributionPattern;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistServiceIntern;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The Class DetourPatternTest.
 */
public class DetourPatternTest extends AbstractJodaEngineTest {

    private AbstractResource<?> participant = null;
    private AbstractResource<?> participant2 = null;
    private static final String SIMPLE_TASK_SUBJECT = "Subject";
    private static final String SIMPLE_TASK_DESCRIPTION = "Description";
    private static final String PARTICIPANT_NAME = "participant";
    private static final String PARTICIPANT_NAME_2 = "participant2";
    private WorklistServiceIntern worklistServiceIntern = null;
    private AbstractWorklistItem item = null;
    

    /**
     * Set up.
     */
    @BeforeMethod
    public void setUp() {

        IdentityBuilder builder = jodaEngineServices.getIdentityService().getIdentityBuilder();
        participant = builder.createParticipant(PARTICIPANT_NAME);
        participant2 = builder.createParticipant(PARTICIPANT_NAME_2);
        worklistServiceIntern = ServiceFactory.getInteralWorklistService();
        AbstractCreationPattern creationPattern = new DirectDistributionPattern(
            SIMPLE_TASK_SUBJECT, 
            SIMPLE_TASK_DESCRIPTION,
            null, 
            participant);
        Token token = Mockito.mock(Token.class);
        item  = creationPattern.createWorklistItem(token, jodaEngineServices.getRepositoryService());
        creationPattern.getPushPattern().distributeItem(worklistServiceIntern, item);
    }
    
    /**
     * Test the simple delegation pattern that assigns a task from one participant to another.
     */
    @Test
    public void testSimpleDelegation() {
        worklistServiceIntern.executeDetourPattern(new SimpleDelegationPattern(), item, participant, participant2);
        
        Assert.assertEquals(item.getAssignedResources().size(), 1);
        Assert.assertEquals(item.getAssignedResources().iterator().next(), participant2);
    }
}
