package de.hpi.oryxengine.activity;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.IdentityServiceImpl;
import de.hpi.oryxengine.WorklistManager;
import de.hpi.oryxengine.activity.impl.HumanTaskActivity;
import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.process.instance.ProcessInstanceContextImpl;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Resource;
import de.hpi.oryxengine.worklist.AllocationStrategies;
import de.hpi.oryxengine.worklist.AllocationStrategiesImpl;
import de.hpi.oryxengine.worklist.Pattern;
import de.hpi.oryxengine.worklist.Task;
import de.hpi.oryxengine.worklist.TaskImpl;
import de.hpi.oryxengine.worklist.TestingWorklistManager;
import de.hpi.oryxengine.worklist.WorklistItem;
import de.hpi.oryxengine.worklist.WorklistItemState;
import de.hpi.oryxengine.worklist.pattern.SimplePullPattern;
import de.hpi.oryxengine.worklist.pattern.SimplePushPattern;

/**
 * The test for the {@link HumanTaskActivity}.
 */
public class HumanTaskActivityTest {

    private Task task;
    private Resource<?> resource;
    
    private HumanTaskActivity humanTask;
    
    private Token token;

    /**
     * Set up.
     * @throws Exception
     *             the exception
     */
    @BeforeMethod
    public void setUp()
    throws Exception {

        // Prepare the organisation structure
        
        IdentityBuilder identityBuilder = new IdentityServiceImpl().getIdentityBuilder();
        Participant participant = identityBuilder.createParticipant("jannik");
        participant.setName("Jannik Streek");

        resource = participant;
        
        // Define the task
        String subject = "Jannik, get Gerardo a cup of coffee!";
        String description = "You know what I mean.";
        
        Pattern pushPattern = new SimplePushPattern();
        Pattern pullPattern = new SimplePullPattern();
        
        AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(pushPattern, pullPattern, null, null);
        
        task = new TaskImpl(subject, description, allocationStrategies, participant);
        
        humanTask = new HumanTaskActivity(task);
        token = new TokenImpl(new NodeImpl(humanTask), new ProcessInstanceContextImpl(), new NavigatorImplMock());
    }

    /**
     * Tear down.
     */
    @AfterMethod
    public void tearDown() {
        
        // Reseting the Worklist Manager after the test case
        TestingWorklistManager.clear();
    }
    
    /**
     * Test activity initialization.
     * The activity should not be null if it was instantiated correctly.
     */
    @Test
    public void testActivityInitialization() {

        assertNotNull(humanTask, "It should not be null since it should be instantiated correctly");
    }

    /**
     * Test that the humanTask pushes a task item into the Jannik's worklist.
     */
    @Test
    public void testJannikHasWorklistItem() {

        humanTask.execute(token);
        
        int worklistSize = WorklistManager.getWorklistService().getWorklistItems(resource).size();
        String failureMessage = "Jannik should now have 1 item in his worklist, but there are " + worklistSize + " item(s) in the worklist."; 
        assertTrue(worklistSize == 1, failureMessage);
        
        WorklistItem worklistItem = WorklistManager.getWorklistService().getWorklistItems(resource).get(0);
        assertEquals(worklistItem.getSubject(), task.getSubject());
        assertEquals(worklistItem.getDescription(), task.getDescription());
    }
    
    /**
     * Test that the assigned user begins with the humanTask.
     * @throws Exception 
     */
    @Test
    public void testJannikBeginsTheWorkItem() throws Exception {
        
        humanTask.execute(token);
        
        WorklistItem worklistItem = WorklistManager.getWorklistService().getWorklistItems(resource).get(0);
        System.out.println(worklistItem.getStatus());
        assertEquals(worklistItem.getStatus(), WorklistItemState.ALLOCATED);

        WorklistManager.getWorklistService().beginWorklistItem(worklistItem);
        assertEquals(worklistItem.getStatus(), WorklistItemState.EXECUTING);
    }

    /**
     * Test that the assigned user completes with the humanTask.
     * @throws Exception 
     */
    @Test
    public void testJannikCompletesTheWorkItem() throws Exception {
        
        humanTask.execute(token);
        
        WorklistItem worklistItem = WorklistManager.getWorklistService().getWorklistItems(resource).get(0);
        WorklistManager.getWorklistService().beginWorklistItem(worklistItem);
        assertEquals(worklistItem.getStatus(), WorklistItemState.EXECUTING);
        
        WorklistManager.getWorklistService().completeWorklistItem(worklistItem);
        assertEquals(worklistItem.getStatus(), WorklistItemState.COMPLETED);
        String failureMessage = "Jannik should have completed the task. So there should be no item in his worklist.";
        assertTrue(WorklistManager.getWorklistService().getWorklistItems(resource).size() == 0, failureMessage);
    }
}
