package de.hpi.oryxengine.resource.allocation;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.AbstractJodaEngineTest;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.allocation.AllocationStrategies;
import de.hpi.oryxengine.allocation.Pattern;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.exception.JodaEngineException;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;
import de.hpi.oryxengine.factory.node.SimpleNodeFactory;
import de.hpi.oryxengine.factory.resource.ParticipantFactory;
import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.node.activity.bpmn.BpmnEndActivity;
import de.hpi.oryxengine.node.activity.bpmn.BpmnHumanTaskActivity;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.ActivityBlueprint;
import de.hpi.oryxengine.process.structure.ActivityBlueprintImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractRole;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.allocation.AllocationStrategiesImpl;
import de.hpi.oryxengine.resource.allocation.TaskImpl;
import de.hpi.oryxengine.resource.allocation.pattern.RolePushPattern;
import de.hpi.oryxengine.resource.allocation.pattern.SimplePullPattern;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemState;

/**
 * This test assigns a task to a role or to a resource that contains other resources.
 */
public class AssigningToRoleUserStoryTest extends AbstractJodaEngineTest {

    private Token token = null;
    private AbstractRole hamburgGuysRole = null;
    private AbstractRole mecklenRole = null;
    private AbstractParticipant jannik = null;
    private Node endNode = null;
    private AbstractParticipant gerardo = null;
    private AbstractParticipant tobi = null;
    private AbstractParticipant tobi2 = null;

    /**
     * Setup.
     * @throws ResourceNotAvailableException 
     */
    @BeforeMethod
    public void setUp() throws ResourceNotAvailableException {

        // The organization structure is already prepared in the factory
        // There is role containing Gerardo and Jannik
        gerardo = ParticipantFactory.createGerardo();
        jannik = ParticipantFactory.createJannik();
        tobi = ParticipantFactory.createTobi();
        tobi2 = ParticipantFactory.createTobi2();
        
        IdentityBuilder identityBuilder = ServiceFactory.getIdentityService().getIdentityBuilder();
        hamburgGuysRole = identityBuilder.createRole("hamburgGuys");
        mecklenRole = identityBuilder.createRole("Mecklenburger");
        
        // look out all these methods are are called on the identity builder (therefore the format)
        identityBuilder
            .participantBelongsToRole(jannik.getID(), hamburgGuysRole.getID())
            .participantBelongsToRole(gerardo.getID(), hamburgGuysRole.getID())
            .participantBelongsToRole(tobi2.getID(), mecklenRole.getID());

        Pattern pushPattern = new RolePushPattern();
        Pattern pullPattern = new SimplePullPattern();

        AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(pushPattern, pullPattern, null, null);

        Task task = new TaskImpl("Clean the office.", "It is very dirty.", allocationStrategies, hamburgGuysRole);

        Class<?>[] constructorSig = {Task.class};
        Object[] params = {task};
        ActivityBlueprint bp = new ActivityBlueprintImpl(BpmnHumanTaskActivity.class, constructorSig, params);
        Node humanTaskNode = SimpleNodeFactory.createSimpleNodeWith(bp);

        endNode = SimpleNodeFactory.createSimpleNodeWith(new ActivityBlueprintImpl(BpmnEndActivity.class));

        humanTaskNode.transitionTo(endNode);

        token = new TokenImpl(humanTaskNode, new ProcessInstanceImpl(null), new NavigatorImplMock());
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
     * Test receive work item.
     * 
     * @throws JodaEngineException test fails
     */
    @Test
    public void testHamburgGuysReceiveWorkItem()
    throws JodaEngineException {

        token.executeStep();

        List<AbstractWorklistItem> worklistItemsForHamburgGuys =
            ServiceFactory.getWorklistService().getWorklistItems(hamburgGuysRole);
        Assert.assertTrue(worklistItemsForHamburgGuys.size() == 1);

        List<AbstractWorklistItem> worklistItemsForJannik = ServiceFactory.getWorklistService().getWorklistItems(jannik);
        Assert.assertTrue(worklistItemsForJannik.size() == 1);
        
        List<AbstractWorklistItem> worklistItemsForGerardo = ServiceFactory.getWorklistService().getWorklistItems(gerardo);
        Assert.assertTrue(worklistItemsForGerardo.size() == 1);
        
        // Tobi doesn't belong to any role so he shouldn't have anything to do
        List<AbstractWorklistItem> worklistItemsForTobi = ServiceFactory.getWorklistService().getWorklistItems(tobi);
        Assert.assertTrue(worklistItemsForTobi.isEmpty());
        
        // Tobi2 doesn't belong to the HamburgGuysRole so he shouldn't have anything to do
        List<AbstractWorklistItem> worklistItemsForTobi2 = ServiceFactory.getWorklistService().getWorklistItems(tobi);
        Assert.assertTrue(worklistItemsForTobi2.isEmpty());
        
        AbstractWorklistItem worklistItemForHamburgGuy = worklistItemsForHamburgGuys.get(0);
        Assert.assertEquals(worklistItemForHamburgGuy.getStatus(), WorklistItemState.OFFERED);

        AbstractWorklistItem worklistItemForJannik = worklistItemsForHamburgGuys.get(0);
        Assert.assertEquals(worklistItemForJannik.getStatus(), WorklistItemState.OFFERED);
        
        Assert.assertEquals(worklistItemForJannik, worklistItemForHamburgGuy);
    }
    
    /**
     * Test work item claim.
     * 
     * @throws JodaEngineException test fails
     */
    @Test
    public void testJannikClaimsWorklistItem()
    throws JodaEngineException {
     
        token.executeStep();
        
        List<AbstractWorklistItem> worklistItemsForHamburgGuys =
            ServiceFactory.getWorklistService().getWorklistItems(hamburgGuysRole);
        AbstractWorklistItem worklistItemForHamburgGuy = worklistItemsForHamburgGuys.get(0);
        Assert.assertEquals(worklistItemForHamburgGuy.getStatus(), WorklistItemState.OFFERED);
        
        ServiceFactory.getWorklistService().claimWorklistItemBy(worklistItemForHamburgGuy, jannik);
        assertEquals(worklistItemForHamburgGuy.getStatus(), WorklistItemState.ALLOCATED);
        
        worklistItemsForHamburgGuys = ServiceFactory.getWorklistService().getWorklistItems(hamburgGuysRole);
        Assert.assertEquals(worklistItemsForHamburgGuys.size(), 0);
        
        List<AbstractWorklistItem> worklistItemsForGerardo = ServiceFactory.getWorklistService().getWorklistItems(gerardo);
        Assert.assertEquals(worklistItemsForGerardo.size(), 0);
        
        List<AbstractWorklistItem> worklistItemsForJannik = ServiceFactory.getWorklistService().getWorklistItems(jannik);
        Assert.assertEquals(worklistItemsForJannik.size(), 1);
    }
    
    /**
     * Test that Jannik begins the work on the work item.
     * 
     * @throws JodaEngineException test fails
     */
    @Test
    public void testJannikBeginsWorklistItem()
    throws JodaEngineException {

        token.executeStep();
        
        List<AbstractWorklistItem> worklistItemsForHamburgGuys =
            ServiceFactory.getWorklistService().getWorklistItems(hamburgGuysRole);
        AbstractWorklistItem worklistItemForHamburgGuy = worklistItemsForHamburgGuys.get(0);
        
        ServiceFactory.getWorklistService().claimWorklistItemBy(worklistItemForHamburgGuy, jannik);
        
        ServiceFactory.getWorklistService().beginWorklistItemBy(worklistItemForHamburgGuy, jannik);
        assertEquals(worklistItemForHamburgGuy.getStatus(), WorklistItemState.EXECUTING);
    }
    
    /**
     * Test the case that Jannik completes the work item.
     * 
     * @throws JodaEngineException test fails
     */
    @Test
    public void testJannikCompletesTheWorkItem()
    throws JodaEngineException {

        token.executeStep();
        
        List<AbstractWorklistItem> worklistItemsForHamburgGuys =
            ServiceFactory.getWorklistService().getWorklistItems(hamburgGuysRole);
        AbstractWorklistItem worklistItemForHamburgGuy = worklistItemsForHamburgGuys.get(0);
        
        ServiceFactory.getWorklistService().claimWorklistItemBy(worklistItemForHamburgGuy, jannik);        
        ServiceFactory.getWorklistService().beginWorklistItemBy(worklistItemForHamburgGuy, jannik);

        ServiceFactory.getWorklistService().completeWorklistItemBy(worklistItemForHamburgGuy, jannik);
        assertEquals(worklistItemForHamburgGuy.getStatus(), WorklistItemState.COMPLETED);
        
        worklistItemsForHamburgGuys = ServiceFactory.getWorklistService().getWorklistItems(hamburgGuysRole);
        String failureMessage = "Jannik should have completed the task."
                           + "So there should be no item in his worklist and in the worklist of the Role HamburgGuys.";
        Assert.assertTrue(ServiceFactory.getWorklistService().getWorklistItems(jannik).size() == 0, failureMessage);
        Assert.assertTrue(worklistItemsForHamburgGuys.size() == 0, failureMessage);
    }

    /**
     * Test work item resume.
     * 
     * @throws JodaEngineException test fails
     */
    @Test
    public void testResumptionOfProcess()
    throws JodaEngineException {

        token.executeStep();
        
        List<AbstractWorklistItem> worklistItemsForHamburgGuys =
            ServiceFactory.getWorklistService().getWorklistItems(hamburgGuysRole);
        AbstractWorklistItem worklistItemForHamburgGuy = worklistItemsForHamburgGuys.get(0);
        
        ServiceFactory.getWorklistService().claimWorklistItemBy(worklistItemForHamburgGuy, jannik);
        ServiceFactory.getWorklistService().beginWorklistItemBy(worklistItemForHamburgGuy, jannik);

        ServiceFactory.getWorklistService().completeWorklistItemBy(worklistItemForHamburgGuy, jannik);

        String failureMessage = "Token should point to the endNode, but it points to "
                                + token.getCurrentNode().getID() + ".";
        assertEquals(endNode, token.getCurrentNode(), failureMessage);
    }
}
