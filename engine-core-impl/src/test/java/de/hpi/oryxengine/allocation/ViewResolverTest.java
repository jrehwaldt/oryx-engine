package de.hpi.oryxengine.allocation;

import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.ServiceFactoryForTesting;
import de.hpi.oryxengine.WorklistService;
import de.hpi.oryxengine.factory.resource.ParticipantFactory;
import de.hpi.oryxengine.factory.worklist.TaskFactory;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.OrganizationUnit;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Position;
import de.hpi.oryxengine.resource.Role;
import de.hpi.oryxengine.resource.worklist.WorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemImpl;

import junit.framework.Assert;

/**
 * Tests the {@link WorklistService} view resolution.
 */
public class ViewResolverTest {

    private IdentityService identityService = null;
    private IdentityBuilder identityBuilder = null;
    
    private Role hamburgGuysRole = null;
    private Participant jannik = null, gerardo = null;
    private Position jannikPosition, gerardoPosition = null;
    private OrganizationUnit bpt = null;
    
    private WorklistService worklistService = null;
    
    
    /**
     * Building a basic organisation structure.
     */
    @BeforeMethod
    public void setUp() {
        
        identityService = ServiceFactory.getIdentityService();
        identityBuilder = identityService.getIdentityBuilder();
        worklistService = ServiceFactory.getWorklistService();
        
        
        hamburgGuysRole = identityBuilder.createRole("hamburgGuys");
        jannik = ParticipantFactory.createJannik();
        jannikPosition = identityBuilder.createPosition("jannikPosition");
        gerardo = ParticipantFactory.createGerardo();
        gerardoPosition = identityBuilder.createPosition("gerardoPosition");
        bpt = identityBuilder.createOrganizationUnit("bpt");
        
        identityBuilder.participantBelongsToRole(jannik.getID(), hamburgGuysRole.getID())
                       .participantBelongsToRole(gerardo.getID(), hamburgGuysRole.getID());
        
        identityBuilder.participantOccupiesPosition(jannik.getID(), jannikPosition.getID())
                       .participantOccupiesPosition(gerardo.getID(), gerardoPosition.getID());
        
        identityBuilder.organizationUnitOffersPosition(bpt.getID(), jannikPosition.getID())
                       .organizationUnitOffersPosition(bpt.getID(), gerardoPosition.getID());
    }
    
    /**
     * Tear down.
     */
    @AfterMethod
    public void tearDown() {
        ServiceFactoryForTesting.clearIdentityService();
    }
    
    /**
     * Tests the view resolution for certain work list participants.
     */
    @Test
    public void testViewResolver() {

        Task task = TaskFactory.createSimpleTask(null);
        WorklistItem worklistItem = new WorklistItemImpl(task, Mockito.mock(Token.class));
        
        ServiceFactory.getWorklistQueue().addWorklistItem(worklistItem, hamburgGuysRole);
        Assert.assertTrue(worklistService.getWorklistItems(jannik).size() == 1);
        Assert.assertTrue(worklistService.getWorklistItems(gerardo).size() == 1);

        ServiceFactory.getWorklistQueue().addWorklistItem(worklistItem, jannik);
        Assert.assertTrue(worklistService.getWorklistItems(jannik).size() == 2);
        Assert.assertTrue(worklistService.getWorklistItems(gerardo).size() == 1);
    }
}
