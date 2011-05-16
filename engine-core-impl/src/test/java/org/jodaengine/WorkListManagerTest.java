package org.jodaengine;

import static org.mockito.Mockito.mock;

import org.jodaengine.allocation.CreationPattern;
import org.jodaengine.allocation.PushPattern;
import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.exception.InvalidWorkItemException;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.factory.resource.ParticipantFactory;
import org.jodaengine.factory.worklist.CreationPatternFactory;
import org.jodaengine.process.token.TokenImpl;
import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.allocation.pattern.AllocateSinglePattern;
import org.jodaengine.resource.allocation.pattern.ConcreteResourcePattern;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemState;

import java.util.List;
import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * The Class WorkListManagerTest.
 * Here go tests for the worklist manager.
 */
public class WorkListManagerTest {
    
    private ConcreteResourcePattern pattern;
    private AbstractParticipant jannik;
    
    /**
     * Creates the participant and task and initializes the test with necessary data.
     */
    @BeforeMethod
    public void createParticipantAndTaskAndInitialize() {
     // We need to start the engine in order to start the WorklistManager who then gets the identityService
        JodaEngine.start();
        pattern = CreationPatternFactory.createJannikServesGerardoCreator();
        TokenImpl token = mock(TokenImpl.class);
        List<AbstractWorklistItem> items = pattern.createWorklistItems(token);
        PushPattern pushPattern = new AllocateSinglePattern();
        pushPattern.distributeWorkitems(ServiceFactory.getWorklistQueue(), items);
//        ServiceFactory.getTaskDistribution().distribute(pattern, token);
        // "hack" to get the participant the task belongs to
        jannik = (AbstractParticipant) pattern.getAssignedResources()[0];
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
        Assert.assertEquals(items.get(0).getSubject(), CreationPatternFactory.SIMPLE_TASK_SUBJECT);       
        Assert.assertEquals(items.get(0).getDescription(), CreationPatternFactory.SIMPLE_TASK_DESCRIPTION);
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
//        Pattern pushPattern = new DirectDistributionPattern();
//        Pattern pullPattern = new SimplePullPattern();
//        AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(pushPattern, pullPattern, null, null);
        // allocation patterns END
        ConcreteResourcePattern anotherPattern = new ConcreteResourcePattern("Go shopping", "I need milk", null, tobi);
        TokenImpl token = mock(TokenImpl.class);
        List<AbstractWorklistItem> items = anotherPattern.createWorklistItems(token);
        PushPattern pushPattern = new AllocateSinglePattern();
        pushPattern.distributeWorkitems(ServiceFactory.getWorklistQueue(), items);
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
        AbstractWorklistItem item = (AbstractWorklistItem) jannik.getWorklist().getWorklistItems().toArray()[0];
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
        AbstractWorklistItem item = (AbstractWorklistItem) jannik.getWorklist().getWorklistItems().toArray()[0];
        ServiceFactory.getWorklistService().completeWorklistItemBy(item, jannik);
        Assert.assertEquals(item.getStatus(), WorklistItemState.COMPLETED);
    }
    
    /**
     * Tests the three parts of the worklist.
     *
     * @throws ResourceNotAvailableException the resource not available exception
     */
    @Test
    public void testGetSpecificWorklistItems() throws ResourceNotAvailableException {
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
