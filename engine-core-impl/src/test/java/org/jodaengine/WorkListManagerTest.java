package org.jodaengine;

import java.util.List;
import java.util.UUID;

import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.exception.InvalidWorkItemException;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.factory.resource.ParticipantFactory;
import org.jodaengine.factory.worklist.CreationPatternFactory;
import org.jodaengine.process.token.Token;
import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.AbstractRole;
import org.jodaengine.resource.allocation.CreationPattern;
import org.jodaengine.resource.allocation.CreationPatternBuilder;
import org.jodaengine.resource.allocation.CreationPatternBuilderImpl;
import org.jodaengine.resource.allocation.PushPattern;
import org.jodaengine.resource.allocation.pattern.creation.AbstractCreationPattern;
import org.jodaengine.resource.allocation.pattern.creation.RoleBasedDistributionPattern;
import org.jodaengine.resource.allocation.pattern.push.AllocateSinglePattern;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemState;
import org.jodaengine.resource.worklist.WorklistService;
import org.jodaengine.util.mock.MockUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The Class WorkListManagerTest.
 * Here go tests for the worklist manager.
 */
public class WorkListManagerTest {

    private CreationPattern pattern;
    private AbstractParticipant jannik = null;
    private JodaEngineServices engineServices = null;

    /**
     * Creates the participant and task and initializes the test with necessary data.
     */
    @BeforeMethod
    public void createParticipantAndTaskAndInitialize() {

        // We need to start the engine in order to start the WorklistManager who then gets the identityService
        engineServices = JodaEngine.start();
        jannik = ParticipantFactory.createJannik();
        pattern = CreationPatternFactory.createDirectDistributionPattern(jannik);

        Token token = MockUtils.fullyMockedToken();

        AbstractWorklistItem item = pattern.createWorklistItem(token, ServiceFactory.getRepositoryService());
        PushPattern pushPattern = new AllocateSinglePattern();
        pushPattern.distributeWorkitem(ServiceFactory.getWorklistQueue(), item);
        // ServiceFactory.getTaskDistribution().distribute(pattern, token);

        // "hack" to get the participant the task belongs to
        jannik = (AbstractParticipant) ((AbstractCreationPattern) pattern).getAssignedResources().iterator().next();
    }

    /**
     * Test and assert that Jannik has one task with the description and subject given in the factory.
     * 
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    public void testAndAssertThatJannikHasOneTask()
    throws ResourceNotAvailableException {

        List<AbstractWorklistItem> items = ServiceFactory.getWorklistService().getWorklistItems(jannik.getID());

        Assert.assertEquals(items.size(), 1);
        // worklistitem is a task, tasks don't have an ID so we deceide to test it that way
        // compare against the values from the Factory
        Assert.assertEquals(items.get(0).getSubject(), CreationPatternFactory.SIMPLE_TASK_SUBJECT);
        Assert.assertEquals(items.get(0).getDescription(), CreationPatternFactory.SIMPLE_TASK_DESCRIPTION);
    }

    /**
     * Test get worklist items by the id of the participant.
     * 
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    @Test
    public void testGetWorklistItemsByIDOfParticipant()
    throws ResourceNotAvailableException {

        testAndAssertThatJannikHasOneTask();

    }

    /**
     * Creates the another participant with another task.
     * A lot of annoying setUps
     * 
     * @throws ResourceNotAvailableException
     */
    public void createAnotherParticipantWithAnotherTask()
    throws ResourceNotAvailableException {

        AbstractParticipant tobi = ParticipantFactory.createTobi();
        AbstractRole testRole = ServiceFactory.getIdentityService().getIdentityBuilder().createRole("testRole");
        ServiceFactory.getIdentityService().getIdentityBuilder()
        .participantBelongsToRole(tobi.getID(), testRole.getID());
        CreationPatternBuilder creationPatternBuilder = new CreationPatternBuilderImpl();
        CreationPattern anotherPattern = creationPatternBuilder.setItemSubject("Go shopping")
        .setItemDescription("I need milk").setItemFormID(null).addResourceAssignedToItem(tobi)
        .buildCreationPattern(RoleBasedDistributionPattern.class);
        Token token = MockUtils.fullyMockedToken();
        AbstractWorklistItem item = anotherPattern.createWorklistItem(token, engineServices.getRepositoryService());

        anotherPattern.getPushPattern().distributeWorkitem(ServiceFactory.getWorklistQueue(), item);
    }

    /**
     * Test get worklist items returns just items of this participant, not the ones of another participant.
     * 
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    @Test
    public void testGetWorklistItemsReturnsJustItemsOfThisParticipant()
    throws ResourceNotAvailableException {

        // first one gets created in setUp
        createAnotherParticipantWithAnotherTask();
        testAndAssertThatJannikHasOneTask();

    }

    /**
     * Test the getWorklistItem method, which is heavily used in the webservice.
     * An item is created and then a lookup with the id of the item is done.
     * 
     * @throws InvalidWorkItemException
     *             the invalid item exception
     */
    @Test
    public void testGettingAWorklistItem()
    throws InvalidWorkItemException {

        AbstractWorklistItem item = (AbstractWorklistItem) jannik.getWorklist().getWorklistItems().toArray()[0];
        AbstractWorklistItem serviceItem = ServiceFactory.getWorklistService().getWorklistItem(jannik, item.getID());
        Assert.assertEquals(item, serviceItem);
    }

    /**
     * Test the getWorklistItem method, which is heavily used in the webservice.
     * In this case, we use an invalid item id, so an exception should occur.
     * 
     * @throws InvalidWorkItemException
     *             the invalid item exception
     */
    @Test(expectedExceptions = InvalidWorkItemException.class)
    public void testGettingAWorklistItemException()
    throws InvalidWorkItemException {

        UUID itemId = UUID.randomUUID();
        ServiceFactory.getWorklistService().getWorklistItem(jannik, itemId);
    }

    /**
     * Test completing an item.
     * 
     */
    @Test
    public void testCompleteWorklistItemBy() {

        AbstractWorklistItem item = (AbstractWorklistItem) jannik.getWorklist().getWorklistItems().toArray()[0];
        ServiceFactory.getWorklistService().completeWorklistItemBy(item, jannik);
        Assert.assertEquals(item.getStatus(), WorklistItemState.COMPLETED);
    }

    /**
     * Tests the three parts of the worklist.
     * 
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    @Test
    public void testGetSpecificWorklistItems()
    throws ResourceNotAvailableException {

        WorklistService service = ServiceFactory.getWorklistService();
        List<AbstractWorklistItem> offeredItems = service.getOfferedWorklistItems(jannik);
        List<AbstractWorklistItem> allocatedItems = service.getAllocatedWorklistItems(jannik);
        List<AbstractWorklistItem> executingItems = service.getExecutingWorklistItems(jannik);

        Assert.assertEquals(offeredItems.size(), 0, "there should be no offered item to jannik");
        Assert.assertEquals(allocatedItems.size(), 1, "there should be one allocated items");
        Assert.assertEquals(executingItems.size(), 0, "there should be not items currently in execution");

        service.beginWorklistItemBy(allocatedItems.get(0), jannik);

        offeredItems = service.getOfferedWorklistItems(jannik);
        allocatedItems = service.getAllocatedWorklistItems(jannik);
        executingItems = service.getExecutingWorklistItems(jannik);

        Assert.assertEquals(offeredItems.size(), 0, "there should be one offered item to jannik");
        Assert.assertEquals(allocatedItems.size(), 0, "there should be no allocated items");
        Assert.assertEquals(executingItems.size(), 1, "the one item should be in execution now");

        service.completeWorklistItemBy(executingItems.get(0), jannik);

        offeredItems = service.getOfferedWorklistItems(jannik);
        allocatedItems = service.getAllocatedWorklistItems(jannik);
        executingItems = service.getExecutingWorklistItems(jannik);

        Assert.assertEquals(offeredItems.size(), 0, "there should be one offered item to jannik");
        Assert.assertEquals(allocatedItems.size(), 0, "there should be no allocated items");
        Assert.assertEquals(executingItems.size(), 0, "the no items in execution anymore");

    }

}
