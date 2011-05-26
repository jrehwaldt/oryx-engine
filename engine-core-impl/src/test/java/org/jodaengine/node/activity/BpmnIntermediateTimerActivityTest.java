package org.jodaengine.node.activity;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jodaengine.ServiceFactory;
import org.jodaengine.eventmanagement.EventManager;
import org.jodaengine.eventmanagement.timing.QuartzJobManager;
import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.navigator.NavigatorImplMock;
import org.jodaengine.node.factory.TransitionFactory;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.node.helper.ActivityLifecycleAssuranceListener;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.instance.ProcessInstanceContextImpl;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.AbstractToken;
import org.jodaengine.process.token.BpmnToken;
import org.jodaengine.process.token.Token;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * The Class IntermediateTimerTest. Checks if the intermediate timer is working.
 */
public class BpmnIntermediateTimerActivityTest extends AbstractJodaEngineTest {
    
    private Token token;
    private Node nullNodeStart;
    private Node intermediateTimerEvent;
    private Node nullNodeEnd;
    private static final int WAITING_TIME = 300;
    private static final int SHORT_WAITING_TIME_TEST = 200;
    private static final int LONG_WAITING_TIME_TEST = 600;
    private ActivityLifecycleAssuranceListener lifecycleTester;
    /**
       * Creates the process. It consists out of three nodes,
       * the central node is an intermediate timer event which waits for 300 MS until execution is continued
       */
      @BeforeMethod
      public void beforeMethod() {
    
          // Defining the LifeCycle Listener
          lifecycleTester = new ActivityLifecycleAssuranceListener();
          
          ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();
    
    
          nullNodeStart = BpmnCustomNodeFactory.createBpmnNullNode(builder);
          
          // Building the IntermediateTimer
          intermediateTimerEvent = BpmnNodeFactory.createBpmnIntermediateTimerEventNode(builder, WAITING_TIME);
          
          nullNodeEnd = BpmnCustomNodeFactory.createBpmnNullNode(builder);
          
          TransitionFactory.createTransitionFromTo(builder, nullNodeStart, intermediateTimerEvent);
          TransitionFactory.createTransitionFromTo(builder, intermediateTimerEvent, nullNodeEnd);
          
          Navigator nav = new NavigatorImplMock();
          ProcessInstanceContext processInstanceContextMock = new ProcessInstanceContextImpl(); 
          AbstractProcessInstance processInstanceMock = Mockito.mock(AbstractProcessInstance.class);
          Mockito.when(processInstanceMock.getContext()).thenReturn(processInstanceContextMock);
          
          
          token = new BpmnToken(nullNodeStart, processInstanceMock, nav);
      }

    /**
       * Delete used objects.
       */
      @AfterMethod
      public void afterMethod() {
          
          nullNodeStart = null;
          intermediateTimerEvent = null;
          nullNodeEnd = null;
          token = null;
    
      }

/**
   * Start a simple waiting process test. There are multiple assertions,
   * which test if the process is waiting and after that continuing.
   *
   * @throws Exception the exception
   */
  @Test
  public void testWaitingProcess() throws Exception {
      token.executeStep();
      assertEquals(token.getCurrentNode(), intermediateTimerEvent);
      token.executeStep();
      Thread.sleep(LONG_WAITING_TIME_TEST);
      assertEquals(token.getCurrentNode(), nullNodeEnd);
      
  }
  
  /**
   * Test activity COMPLETED activity state.
   *
   * @throws Exception the exception
   */
  @Test
  public void testActivityStateCompleted() throws Exception {
      token.executeStep();
      ((AbstractToken) token).registerListener(lifecycleTester);
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
      assertEquals(token.getCurrentActivityState(), ActivityState.WAITING);
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
      assertEquals(token.getCurrentNode(), intermediateTimerEvent);
      token.executeStep();
      Thread.sleep(SHORT_WAITING_TIME_TEST);
      assertFalse(token.getCurrentNode() == nullNodeEnd, "Current Node should not be the node after the timer,"
          + " because time wasn't sufficient.");
  }
  

  /**
   * Test the cancellation of the timer. After the timer is canceled the token should not move on.
   *
   * @throws JodaEngineException the JodaEngine exception
   * @throws InterruptedException the interrupted exception
   */
  @Test
  public void testCancelNode() throws JodaEngineException, InterruptedException {
      token.executeStep();
      token.executeStep();
      
      // Timer activated, now cancel the scheduled job
      token.getCurrentNode().getActivityBehaviour().cancel(token);
      
      // Wait until the timer would resume the token
      Thread.sleep(LONG_WAITING_TIME_TEST);
      
      assertEquals(token.getCurrentNode(), intermediateTimerEvent);  
  }

/**
   * Test the cancellation of the process with the timer.
   * After the process is canceled there should be not scheduled jobs in the quartz.
   *
   * @throws JodaEngineException the JodaEngine exception
   * @throws InterruptedException the interrupted exception
   */
  @Test
  public void testCancelProcess() throws JodaEngineException, InterruptedException {
      
      
      int jobGroups;
      EventManager eventManager = (EventManager) ServiceFactory.getCorrelationService();
      QuartzJobManager timer = (QuartzJobManager) eventManager.getTimer();

      //Step through the process and activate the timer
      token.executeStep();
      token.executeStep();
      
      //Assert that the timer job is scheduled
      jobGroups = timer.numberOfCurrentRunningJobs();
      assertEquals(jobGroups, 1);
      
      
      //Cancel the process, the cancellation should lead to the deletion of all started jobs
      token.cancelExecution();
      
      jobGroups = timer.numberOfCurrentRunningJobs();
      assertEquals(jobGroups, 0);
  }
}
