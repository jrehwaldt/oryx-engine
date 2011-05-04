package de.hpi.oryxengine.node.activity;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.quartz.SchedulerException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.AbstractJodaEngineTest;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.correlation.timing.TimingManager;
import de.hpi.oryxengine.exception.JodaEngineException;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.node.factory.TransitionFactory;
import de.hpi.oryxengine.node.factory.bpmn.BpmnCustomNodeFactory;
import de.hpi.oryxengine.node.factory.bpmn.BpmnNodeFactory;
import de.hpi.oryxengine.plugin.activity.ActivityLifecycleAssurancePlugin;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilderImpl;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;

/**
 * The Class IntermediateTimerTest. Checks if the intermediate timer is working.
 * @author Jannik Streek
 */
public class IntermediateTimerTest extends AbstractJodaEngineTest {
    
    private Token token;
    private Node node;
    private Node node2;
    private Node node3;
    private static final int WAITING_TIME = 300;
    private static final int SHORT_WAITING_TIME_TEST = 200;
    private static final int LONG_WAITING_TIME_TEST = 600;
    private ActivityLifecycleAssurancePlugin lifecycleTester;
    
  /**
   * Start a simple waiting process test. There are multiple assertions,
   * which test if the process is waiting and after that continuing.
   *
   * @throws Exception the exception
   */
  @Test
  public void testWaitingProcess() throws Exception {
      token.executeStep();
      assertEquals(token.getCurrentNode(), node2);
      token.executeStep();
      Thread.sleep(LONG_WAITING_TIME_TEST);
      assertEquals(token.getCurrentNode(), node3);
      
  }
  
  /**
   * Test activity COMPLETED activity state.
   *
   * @throws Exception the exception
   */
  @Test
  public void testActivityStateCompleted() throws Exception {
      token.executeStep();
      ((TokenImpl) token).registerPlugin(lifecycleTester);
      token.executeStep();
      assertFalse(lifecycleTester.isCompletedCalled());
      Thread.sleep(LONG_WAITING_TIME_TEST);
      assertTrue(lifecycleTester.isCompletedCalled());
  }
  
  /**
   * Test activity ACTIVE activity state.
   *
   * @throws Exception the exception
   */
  @Test
  public void testActivityStateActive() throws Exception {
      token.executeStep();
      token.executeStep();
      Thread.sleep(SHORT_WAITING_TIME_TEST);
      assertEquals(token.getCurrentActivityState(), ActivityState.SUSPENDED);
  }
  
  
  /**
   * Start a simple failing waiting process test. Waiting time is here not sufficient,
   * so the process can't step forward when the assertion is made.
   *
   * @throws Exception the exception
   */
  @Test
  public void testFailingWaitingProcess() throws Exception {
      token.executeStep();
      assertEquals(token.getCurrentNode(), node2);
      token.executeStep();
      Thread.sleep(SHORT_WAITING_TIME_TEST);
      assertFalse(token.getCurrentNode() == node3, "Current Node should not be the node after the timer,"
          + " because time wasn't sufficient.");
      
  }
  

  /**
   * Creates the process. It consists out of three nodes,
   * the central node is an intermediate timer event which waits for 300 MS until execution is continued
   */
  @BeforeMethod
  public void beforeMethod() {

      // Defining the LifeCycle Plugin
      lifecycleTester = new ActivityLifecycleAssurancePlugin();
      
      ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();

      Navigator nav = new NavigatorImplMock();

      node = BpmnCustomNodeFactory.createBpmnNullNode(builder);
      
      // Building the IntermediateTimer
      node2 = BpmnNodeFactory.createBpmnIntermediateTimerEventNode(builder, WAITING_TIME);
      
      node3 = BpmnCustomNodeFactory.createBpmnNullNode(builder);
      
      TransitionFactory.createTransitionFromTo(builder, node, node2);
      TransitionFactory.createTransitionFromTo(builder, node2, node3);
      
      token = new TokenImpl(node, mock(AbstractProcessInstance.class), nav);
      
      
      // Cleanup the scheduler, remove old jobs to avoid testing problems
      try {
          ServiceFactory.getCorrelationService().getTimer().emptyScheduler();
      } catch (SchedulerException e) {
          e.printStackTrace();
      }

  }
  
  /**
   * Test the cancellation of the timer. After the timer is canceled the token should not move on.
   *
   * @throws JodaEngineException the dalmatina exception
   * @throws InterruptedException the interrupted exception
   */
  @Test
  public void testCancelNode() throws JodaEngineException, InterruptedException {
      token.executeStep();
      token.executeStep();
      
      //Timer activated, now cancel the scheduled job
      token.getCurrentActivity().cancel();
      
      //Wait until the timer would resume the token
      Thread.sleep(LONG_WAITING_TIME_TEST);
      
      assertEquals(token.getCurrentNode(), node2);  
  }
  
  /**
   * Test the cancellation of the process with the timer.
   * After the process is canceled there should be not scheduled jobs in the quartz.
   *
   * @throws JodaEngineException the dalmatina exception
   * @throws InterruptedException the interrupted exception
   */
  @Test
  public void testCancelProcess() throws JodaEngineException, InterruptedException {
      
      
      int jobGroups;
      TimingManager timer = ServiceFactory.getCorrelationService().getTimer();

      //Step through the process and activate the timer
      token.executeStep();
      token.executeStep();
      
      //Assert that the timer job is scheduled
      jobGroups = timer.countScheduledJobGroups();
      assertEquals(jobGroups, 1);
      
      //Cancel the process, the cancellation should lead to the deletion of all started jobs
      token.cancelExecution();
      
      jobGroups = timer.countScheduledJobGroups();
      assertEquals(jobGroups, 0);
  }

  /**
   * Delete used objects.
   */
  @AfterMethod
  public void afterMethod() {
      
      node = null;
      node2 = null;
      node3 = null;
      token = null;

  }

}
