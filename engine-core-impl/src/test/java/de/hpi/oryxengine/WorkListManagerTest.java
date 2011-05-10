package de.hpi.oryxengine;

import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.UUID;


import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.allocation.AllocationStrategies;
import de.hpi.oryxengine.allocation.Pattern;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.bootstrap.JodaEngine;
import de.hpi.oryxengine.exception.InvalidWorkItemException;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;
import de.hpi.oryxengine.factory.resource.ParticipantFactory;
import de.hpi.oryxengine.factory.worklist.TaskFactory;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.IdentityBuilderImpl;
import de.hpi.oryxengine.resource.allocation.AllocationStrategiesImpl;
import de.hpi.oryxengine.resource.allocation.TaskImpl;
import de.hpi.oryxengine.resource.allocation.pattern.DirectPushPattern;
import de.hpi.oryxengine.resource.allocation.pattern.SimplePullPattern;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemState;

/**
 * The Class WorkListManagerTest.
 * Here go tests for the worklist manager.
 */
public class WorkListManagerTest {
    
    private Task task;
    private AbstractParticipant jannik;
    
    /**
     * Creates the participant and task and initializes the test with necessary data.
     */
    @BeforeMethod
    public void createParticipantAndTaskAndInitialize() {
     // We need to start the engine in order to start the WorklistManager who then gets the identityService
        JodaEngine.start();
        task = TaskFactory.createJannikServesGerardoTask();
        TokenImpl token = mock(TokenImpl.class);
        ServiceFactory.getTaskDistribution().distribute(task, token);
        // "hack" to get the participant the task belongs to
        jannik = (AbstractParticipant) task.getAssignedResources().toArray()[0];
    }
    
    /**
     * Test and assert that Jannik has one task with the description and subject given in the factory.
     * @throws ResourceNotAvailableException 
     */
    public void testAndAssertThatJannikHasOneTask() throws ResourceNotAvailableException {
        List<AbstractWorklistItem> items = ServiceFactory.getWorklistService().getWorklistItems(jannik.getID());
        
        Assert.assertEquals(items.size(), 1);
        // worklistitem is a task, tasks don't have an ID so we deceide to test it that way
        // compare against the values from the Factory
        Assert.assertEquals(items.get(0).getSubject(), TaskFactory.SIMPLE_TASK_SUBJECT);       
        Assert.assertEquals(items.get(0).getDescription(), TaskFactory.SIMPLE_TASK_DESCRIPTION);
    }
    
    /**
     * Test get worklist items by the id of the participant.
     * @throws ResourceNotAvailableException 
     */
    @Test
    public void testGetWorklistItemsByIDOfParticipant() throws ResourceNotAvailableException {        
        testAndAssertThatJannikHasOneTask();
        
        
    }
    
    /**
     * Creates the another participant with another task.
     * A lot of annoying setUps
     */
    public void createAnotherParticipantWithAnotherTask() {
        AbstractParticipant tobi = ParticipantFactory.createTobi();
        // allocation patterns START
        Pattern pushPattern = new DirectPushPattern();
        Pattern pullPattern = new SimplePullPattern();
        AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(pushPattern, pullPattern, null, null);
        // allocation patterns END
        Task anotherTask = new TaskImpl("Go shopping", "I need milk", allocationStrategies, tobi);
        TokenImpl token = mock(TokenImpl.class);
        ServiceFactory.getTaskDistribution().distribute(anotherTask, token);
    }
    
    /**
     * Test get worklist items returns just items of this participant, not the ones of another participant.
     * @throws ResourceNotAvailableException 
     */
    @Test
    public void testGetWorklistItemsReturnsJustItemsOfThisParticipant() throws ResourceNotAvailableException {   
        // first one gets created in setUp
        createAnotherParticipantWithAnotherTask();
        testAndAssertThatJannikHasOneTask();
        
    }
    
    /**
     * Test the getWorklistItem method, which is heavily used in the webservice.
     * An item is created and then a lookup with the id of the item is done.
     *
     * @throws InvalidWorkItemException the invalid item exception
     */
    @Test
    public void testGettingAWorklistItem() throws InvalidWorkItemException {
        AbstractWorklistItem item = (AbstractWorklistItem) jannik.getWorklist().getAllWorklistItems().toArray()[0];
        AbstractWorklistItem serviceItem = ServiceFactory.getWorklistService().getWorklistItem(jannik, item.getID());
        Assert.assertEquals(item, serviceItem);
    }
    
    /**
     * Test the getWorklistItem method, which is heavily used in the webservice.
     * In this case, we use an invalid item id, so an exception should occur.
     *
     * @throws InvalidWorkItemException the invalid item exception
     */
    @Test(expectedExceptions = InvalidWorkItemException.class)
    public void testGettingAWorklistItemException() throws InvalidWorkItemException {
        UUID itemId = UUID.randomUUID();
        ServiceFactory.getWorklistService().getWorklistItem(jannik, itemId);
    }
    
    /**
     * Test completing an item.
     *
     */
    @Test
    public void testCompleteWorklistItemBy() {
        AbstractWorklistItem item = (AbstractWorklistItem) jannik.getWorklist().getAllWorklistItems().toArray()[0];
        ServiceFactory.getWorklistService().completeWorklistItemBy(item, jannik);
        Assert.assertEquals(item.getStatus(), WorklistItemState.COMPLETED);
    }
    
    @Test
    public void testGetSpecificWorklistItems() throws ResourceNotAvailableException {
//        IdentityService identity = ServiceFactory.getIdentityService();
//        IdentityBuilder builder = ((IdentityServiceImpl) identity).getIdentityBuilder();
//        
//        AbstractParticipant resourceA = builder.createParticipant("A");
//        
//        Task task = TaskFactory.createParticipantTask(resourceA);
//        TokenImpl token = mock(TokenImpl.class);
//        ServiceFactory.getTaskDistribution().distribute(task, token);
        WorklistService service = ServiceFactory.getWorklistService();
        List<AbstractWorklistItem> offeredItems = service.getOfferedWorklistItems(jannik);
        List<AbstractWorklistItem> allocatedItems = service.getAllocatedWorklistItems(jannik);
        List<AbstractWorklistItem> executingItems = service.getExecutingWorklistItems(jannik);        
        
        Assert.assertEquals(offeredItems.size(), 0, "there should be no offered item to jannik");
        Assert.assertEquals(allocatedItems.size(), 1, "there should be one allocated items");
        Assert.assertEquals(executingItems.size(), 0, "there should be not items currently in execution");
        
        service.beginWorklistItemBy(allocatedItems.get(0), jannik);
        
        Assert.assertEquals(offeredItems.size(), 0, "there should be one offered item to jannik");
        Assert.assertEquals(allocatedItems.size(), 0, "there should be no allocated items");
        Assert.assertEquals(executingItems.size(), 1, "the one item should be in execution now");
        
        service.completeWorklistItemBy(executingItems.get(0), jannik);        
        Assert.assertEquals(offeredItems.size(), 0, "there should be one offered item to jannik");
        Assert.assertEquals(allocatedItems.size(), 0, "there should be no allocated items");
        Assert.assertEquals(executingItems.size(), 0, "the no items in execution anymore");
        
    }
    

}
