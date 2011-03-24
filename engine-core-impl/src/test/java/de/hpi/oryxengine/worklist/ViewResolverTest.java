package de.hpi.oryxengine.worklist;

import junit.framework.Assert;

import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.ServiceFactoryForTesting;
import de.hpi.oryxengine.factory.resource.ParticipantFactory;
import de.hpi.oryxengine.factory.worklist.TaskFactory;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.OrganizationUnit;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Position;
import de.hpi.oryxengine.resource.Role;

/**
 * TODO javaDoc und Implementierung.
 */
public class ViewResolverTest {

    IdentityService identityService;
    IdentityBuilder identityBuilder;
    
    Role hamburgGuysRole;
    Participant jannik, gerardo;
    Position jannikPosition, gerardoPosition;
    OrganizationUnit bpt;
    
    
    /**
     * Building a basic organisation structure.
     * @throws Exception
     */
    @BeforeMethod
    public void setUp() throws Exception {
        
        identityService = ServiceFactory.getIdentityService();
        identityBuilder = identityService.getIdentityBuilder();
        
        hamburgGuysRole = identityBuilder.createRole("hamburgGuys");
        jannik = ParticipantFactory.createJannik();
        jannikPosition = identityBuilder.createPosition("jannikPosition");
        gerardo = ParticipantFactory.createGerardo();
        gerardoPosition = identityBuilder.createPosition("gerardoPosition");
        bpt = identityBuilder.createOrganizationUnit("bpt");
        
        identityBuilder.participantBelongsToRole(jannik, hamburgGuysRole)
                       .participantBelongsToRole(gerardo, hamburgGuysRole);
        
        identityBuilder.participantOccupiesPosition(jannik, jannikPosition)
                       .participantOccupiesPosition(gerardo, gerardoPosition);
        
        identityBuilder.organizationUnitOffersPosition(bpt, jannikPosition)
                       .organizationUnitOffersPosition(bpt, gerardoPosition);
    }
    
    @AfterMethod
    public void tearDown() {
        ServiceFactoryForTesting.clearIdentityService();
    }
    
    @Test
    public void testViewResolver() throws Exception {

        Task task = TaskFactory.createSimpleTask(null);
        WorklistItem worklistItem = new WorklistItemImpl(task, Mockito.mock(Token.class));
        
        ServiceFactory.getWorklistQueue().addWorklistItem(worklistItem, hamburgGuysRole);
        Assert.assertTrue(ServiceFactory.getWorklistService().getWorklistItems(jannik).size() == 1);
        Assert.assertTrue(ServiceFactory.getWorklistService().getWorklistItems(gerardo).size() == 1);

        ServiceFactory.getWorklistQueue().addWorklistItem(worklistItem, jannik);
        Assert.assertTrue(ServiceFactory.getWorklistService().getWorklistItems(jannik).size() == 2);
        Assert.assertTrue(ServiceFactory.getWorklistService().getWorklistItems(gerardo).size() == 1);
    }
}
