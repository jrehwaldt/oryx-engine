package org.jodaengine.resource.allocation;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.jodaengine.ServiceFactory;
import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.factory.node.SimpleNodeFactory;
import org.jodaengine.factory.resource.ParticipantFactory;
import org.jodaengine.navigator.NavigatorImplMock;
import org.jodaengine.node.activity.bpmn.BpmnEndEventActivity;
import org.jodaengine.node.activity.bpmn.BpmnHumanTaskActivity;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BpmnToken;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.builder.BpmnTokenBuilder;
import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.AbstractRole;
import org.jodaengine.resource.IdentityBuilder;
import org.jodaengine.resource.allocation.pattern.creation.RoleBasedDistributionPattern;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemState;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
     * 
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    @BeforeMethod
    public void setUp()
    throws ResourceNotAvailableException {

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
        identityBuilder.participantBelongsToRole(jannik.getID(), hamburgGuysRole.getID())
        .participantBelongsToRole(gerardo.getID(), hamburgGuysRole.getID())
        .participantBelongsToRole(tobi2.getID(), mecklenRole.getID());

        CreationPattern pattern = new RoleBasedDistributionPattern("Clean the office.", "It is very dirty.", null,
            hamburgGuysRole);

        // Task task = new TaskImpl("Clean the office.", "It is very dirty.", allocationStrategies, hamburgGuysRole);

        Node humanTaskNode = SimpleNodeFactory.createSimpleNodeWith(new BpmnHumanTaskActivity(pattern));

        endNode = SimpleNodeFactory.createSimpleNodeWith(new BpmnEndEventActivity());

        humanTaskNode.transitionTo(endNode);

        //TODO Jannik refactor to builder
        token = new BpmnToken(humanTaskNode, new ProcessInstance(null, Mockito.mock(BpmnTokenBuilder.class)), new NavigatorImplMock());
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
     * Test receive work item.
     * 
     * @throws JodaEngineException
     *             test fails
     */
    @Test
    public void testHamburgGuysReceiveWorkItem()
    throws JodaEngineException {

        token.executeStep();

        List<AbstractWorklistItem> worklistItemsForHamburgGuys = ServiceFactory.getWorklistService().getWorklistItems(
            hamburgGuysRole);
        Assert.assertTrue(worklistItemsForHamburgGuys.size() == 1);

        List<AbstractWorklistItem> worklistItemsForJannik = ServiceFactory.getWorklistService()
        .getWorklistItems(jannik);
        Assert.assertTrue(worklistItemsForJannik.size() == 1);

        List<AbstractWorklistItem> worklistItemsForGerardo = ServiceFactory.getWorklistService().getWorklistItems(
            gerardo);
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
     * @throws JodaEngineException
     *             test fails
     */
    @Test
    public void testJannikClaimsWorklistItem()
    throws JodaEngineException {

        token.executeStep();

        List<AbstractWorklistItem> worklistItemsForHamburgGuys = ServiceFactory.getWorklistService().getWorklistItems(
            hamburgGuysRole);
        AbstractWorklistItem worklistItemForHamburgGuy = worklistItemsForHamburgGuys.get(0);
        Assert.assertEquals(worklistItemForHamburgGuy.getStatus(), WorklistItemState.OFFERED);

        ServiceFactory.getWorklistService().claimWorklistItemBy(worklistItemForHamburgGuy, jannik);
        assertEquals(worklistItemForHamburgGuy.getStatus(), WorklistItemState.ALLOCATED);

        worklistItemsForHamburgGuys = ServiceFactory.getWorklistService().getWorklistItems(hamburgGuysRole);
        Assert.assertEquals(worklistItemsForHamburgGuys.size(), 0);

        List<AbstractWorklistItem> worklistItemsForGerardo = ServiceFactory.getWorklistService().getWorklistItems(
            gerardo);
        Assert.assertEquals(worklistItemsForGerardo.size(), 0);

        List<AbstractWorklistItem> worklistItemsForJannik = ServiceFactory.getWorklistService()
        .getWorklistItems(jannik);
        Assert.assertEquals(worklistItemsForJannik.size(), 1);
    }

    /**
     * Test that Jannik begins the work on the work item.
     * 
     * @throws JodaEngineException
     *             test fails
     */
    @Test
    public void testJannikBeginsWorklistItem()
    throws JodaEngineException {

        token.executeStep();

        List<AbstractWorklistItem> worklistItemsForHamburgGuys = ServiceFactory.getWorklistService().getWorklistItems(
            hamburgGuysRole);
        AbstractWorklistItem worklistItemForHamburgGuy = worklistItemsForHamburgGuys.get(0);

        ServiceFactory.getWorklistService().claimWorklistItemBy(worklistItemForHamburgGuy, jannik);

        ServiceFactory.getWorklistService().beginWorklistItemBy(worklistItemForHamburgGuy, jannik);
        assertEquals(worklistItemForHamburgGuy.getStatus(), WorklistItemState.EXECUTING);
    }

    /**
     * Test the case that Jannik completes the work item.
     * 
     * @throws JodaEngineException
     *             test fails
     */
    @Test
    public void testJannikCompletesTheWorkItem()
    throws JodaEngineException {

        token.executeStep();

        List<AbstractWorklistItem> worklistItemsForHamburgGuys = ServiceFactory.getWorklistService().getWorklistItems(
            hamburgGuysRole);
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
     * @throws JodaEngineException
     *             test fails
     */
    @Test
    public void testResumptionOfProcess()
    throws JodaEngineException {

        token.executeStep();

        List<AbstractWorklistItem> worklistItemsForHamburgGuys = ServiceFactory.getWorklistService().getWorklistItems(
            hamburgGuysRole);
        AbstractWorklistItem worklistItemForHamburgGuy = worklistItemsForHamburgGuys.get(0);

        ServiceFactory.getWorklistService().claimWorklistItemBy(worklistItemForHamburgGuy, jannik);
        ServiceFactory.getWorklistService().beginWorklistItemBy(worklistItemForHamburgGuy, jannik);

        ServiceFactory.getWorklistService().completeWorklistItemBy(worklistItemForHamburgGuy, jannik);

        String failureMessage = "Token should point to the endNode, but it points to " + token.getCurrentNode().getID()
            + ".";
        assertEquals(endNode, token.getCurrentNode(), failureMessage);
    }
}
