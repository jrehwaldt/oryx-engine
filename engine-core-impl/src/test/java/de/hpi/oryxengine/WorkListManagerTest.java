package de.hpi.oryxengine;

import static org.mockito.Mockito.mock;

import java.util.List;


import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.allocation.AllocationStrategies;
import de.hpi.oryxengine.allocation.Pattern;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.bootstrap.OryxEngine;
import de.hpi.oryxengine.factory.resource.ParticipantFactory;
import de.hpi.oryxengine.factory.worklist.TaskFactory;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.allocation.AllocationStrategiesImpl;
import de.hpi.oryxengine.resource.allocation.TaskImpl;
import de.hpi.oryxengine.resource.allocation.pattern.DirectPushPattern;
import de.hpi.oryxengine.resource.allocation.pattern.SimplePullPattern;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;

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
        OryxEngine.start();
        task = TaskFactory.createJannikServesGerardoTask();
        TokenImpl token = mock(TokenImpl.class);
        ServiceFactory.getTaskDistribution().distribute(task, token);
        // "hack" to get the participant the task belongs to
        jannik = (AbstractParticipant) task.getAssignedResources().toArray()[0];
    }
    
    /**
     * Test and assert that Jannik has one task with the description and subject given in the factory.
     */
    public void testAndAssertThatJannikHasOneTask() {
        List<AbstractWorklistItem> items = ServiceFactory.getWorklistService().getWorklistItems(jannik.getID());
        
        Assert.assertEquals(items.size(), 1);
        // worklistitem is a task, tasks don't have an ID so we deceide to test it that way
        // compare against the values from the Factory
        Assert.assertEquals(items.get(0).getSubject(), TaskFactory.SIMPLE_TASK_SUBJECT);       
        Assert.assertEquals(items.get(0).getDescription(), TaskFactory.SIMPLE_TASK_DESCRIPTION);
    }
    
    /**
     * Test get worklist items by the id of the participant.
     */
    @Test
    public void testGetWorklistItemsByIDOfParticipant() {        
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
     */
    @Test
    public void testGetWorklistItemsReturnsJustItemsOfThisParticipant() {   
        // first one gets created in setUp
        createAnotherParticipantWithAnotherTask();
        testAndAssertThatJannikHasOneTask();
        
    }
    

}
