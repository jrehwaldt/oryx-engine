package de.hpi.oryxengine.worklist;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.ServiceFactoryForTesting;
import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.HumanTaskActivity;
import de.hpi.oryxengine.factory.node.GerardoNodeFactory;
import de.hpi.oryxengine.factory.resource.ParticipantFactory;
import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.process.instance.ProcessInstanceContextImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Role;
import de.hpi.oryxengine.worklist.pattern.RolePushPattern;
import de.hpi.oryxengine.worklist.pattern.SimplePullPattern;

/**
 * This test assigns a task to a role or to a resource that contains other resources.
 */
public class AssigningToRoleUserStoryTest {

    Token token;
    Role hamburgGuysRole;
    Participant jannik;
    Node endNode;

    @BeforeMethod
    public void setUp()
    throws Exception {

        // The organization structure is already prepared in the factory
        // There is role containing Gerardo and Jannik
        Participant gerardo = ParticipantFactory.createGerardo();
        jannik = ParticipantFactory.createJannik();
        IdentityBuilder identityBuilder = ServiceFactory.getIdentityService().getIdentityBuilder();
        hamburgGuysRole = identityBuilder.createRole("hamburgGuys");
        identityBuilder.participantBelongsToRole(jannik, hamburgGuysRole).participantBelongsToRole(gerardo,
            hamburgGuysRole);

        Pattern pushPattern = new RolePushPattern();
        Pattern pullPattern = new SimplePullPattern();

        AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(pushPattern, pullPattern, null, null);

        Task task = new TaskImpl("Clean the office.", "It is very dirty.", allocationStrategies, hamburgGuysRole);

        Activity humanTaskActivity = new HumanTaskActivity(task);
        Node humanTaskNode = GerardoNodeFactory.createSimpleNodeWith(humanTaskActivity);

        AbstractActivity endactivity = new EndActivity();
        endNode = GerardoNodeFactory.createSimpleNodeWith(endactivity);

        humanTaskNode.transitionTo(endNode);

        token = new TokenImpl(humanTaskNode, new ProcessInstanceContextImpl(), new NavigatorImplMock());
    }

    @AfterMethod
    public void tearDown() {

        ServiceFactoryForTesting.clearWorklistManager();
    }

    @Test
    public void testHamburgGuysReceiveWorkItem()
    throws Exception {

        token.executeStep();

        List<WorklistItem> worklistItemsForHamburgGuys = ServiceFactory.getWorklistService().getWorklistItems(hamburgGuysRole);
        Assert.assertTrue(worklistItemsForHamburgGuys.size() == 1);

        List<WorklistItem> worklistItemsForJannik = ServiceFactory.getWorklistService().getWorklistItems(jannik);
        Assert.assertTrue(worklistItemsForJannik.size() == 1);
        
        WorklistItem worklistItemForHamburgGuy = worklistItemsForHamburgGuys.get(0);
        Assert.assertEquals(worklistItemForHamburgGuy.getStatus(), WorklistItemState.OFFERED);

        WorklistItem worklistItemForJannik = worklistItemsForHamburgGuys.get(0);
        Assert.assertEquals(worklistItemForJannik.getStatus(), WorklistItemState.OFFERED);
        
        Assert.assertEquals(worklistItemForJannik, worklistItemForHamburgGuy);
    }

    @Test
    public void testJannikClaimsWorklistItem() throws Exception {
     
        token.executeStep();
        
        List<WorklistItem> worklistItemsForHamburgGuys = ServiceFactory.getWorklistService().getWorklistItems(hamburgGuysRole);
        WorklistItem worklistItemForHamburgGuy = worklistItemsForHamburgGuys.get(0);
        Assert.assertEquals(worklistItemForHamburgGuy.getStatus(), WorklistItemState.OFFERED);
        
        ServiceFactory.getWorklistService().claimWorklistItemBy(worklistItemForHamburgGuy, jannik);
        assertEquals(worklistItemForHamburgGuy.getStatus(), WorklistItemState.ALLOCATED);
        
        Assert.assertTrue(worklistItemsForHamburgGuys.size() == 0);
    }
    
    @Test
    public void testJannikBeginsWorklistItem() throws Exception {

        token.executeStep();
        
        List<WorklistItem> worklistItemsForHamburgGuys = ServiceFactory.getWorklistService().getWorklistItems(hamburgGuysRole);
        WorklistItem worklistItemForHamburgGuy = worklistItemsForHamburgGuys.get(0);
        
        ServiceFactory.getWorklistService().claimWorklistItemBy(worklistItemForHamburgGuy, jannik);
        
        ServiceFactory.getWorklistService().beginWorklistItemBy(worklistItemForHamburgGuy, jannik);
        assertEquals(worklistItemForHamburgGuy.getStatus(), WorklistItemState.EXECUTING);
    }
    
    @Test
    public void testJannikCompletesTheWorkItem()
    throws Exception {

        token.executeStep();
        
        List<WorklistItem> worklistItemsForHamburgGuys = ServiceFactory.getWorklistService().getWorklistItems(hamburgGuysRole);
        WorklistItem worklistItemForHamburgGuy = worklistItemsForHamburgGuys.get(0);
        
        ServiceFactory.getWorklistService().claimWorklistItemBy(worklistItemForHamburgGuy, jannik);        
        ServiceFactory.getWorklistService().beginWorklistItemBy(worklistItemForHamburgGuy, jannik);

        ServiceFactory.getWorklistService().completeWorklistItemBy(worklistItemForHamburgGuy, jannik);
        assertEquals(worklistItemForHamburgGuy.getStatus(), WorklistItemState.COMPLETED);
        
        String failureMessage = "Jannik should have completed the task."
                                + "So there should be no item in his worklist and in the worklist of the Role HamburgGuys.";
        Assert.assertTrue(ServiceFactory.getWorklistService().getWorklistItems(jannik).size() == 0, failureMessage);
        Assert.assertTrue(worklistItemsForHamburgGuys.size() == 0, failureMessage);
    }

    @Test
    public void testResumptionOfProcess()
    throws Exception {

        token.executeStep();
        
        List<WorklistItem> worklistItemsForHamburgGuys = ServiceFactory.getWorklistService().getWorklistItems(hamburgGuysRole);
        WorklistItem worklistItemForHamburgGuy = worklistItemsForHamburgGuys.get(0);
        
        ServiceFactory.getWorklistService().claimWorklistItemBy(worklistItemForHamburgGuy, jannik);        
        ServiceFactory.getWorklistService().beginWorklistItemBy(worklistItemForHamburgGuy, jannik);

        ServiceFactory.getWorklistService().completeWorklistItemBy(worklistItemForHamburgGuy, jannik);

        String failureMessage = "Token should point to the endNode, but it points to "
                                + token.getCurrentNode().getID() + ".";
        assertEquals(endNode, token.getCurrentNode(), failureMessage);
    }
}
