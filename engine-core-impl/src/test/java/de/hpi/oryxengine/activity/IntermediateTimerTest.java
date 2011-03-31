package de.hpi.oryxengine.activity;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.impl.IntermediateTimer;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.process.definition.NodeParameter;
import de.hpi.oryxengine.process.definition.NodeParameterImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilder;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The Class IntermediateTimerTest. Checks if the intermediate timer is working.
 */
public class IntermediateTimerTest {
    
    private Token token;
    private Node node;
    private Node node2;
    private Node node3;
    public static final int WAITING_TIME = 300;
    public static final int SHORT_WAITING_TIME_TEST = 200;
    public static final int LONG_WAITING_TIME_TEST = 600;
    
  /**
   * Start a simple waiting process test. There are multiple assertions,
   * which test if the process is waiting and after continuing.
   *
   * @throws Exception the exception
   */
  @Test
  public void startSimpleWaitingProcessTest() throws Exception {
      assertEquals(token.getCurrentNode(), node);
      token.executeStep();
      assertEquals(token.getCurrentNode(), node2);
      token.executeStep();
      Thread.sleep(IntermediateTimerTest.SHORT_WAITING_TIME_TEST);
      assertEquals(token.getCurrentNode(), node2);
      Thread.sleep(IntermediateTimerTest.LONG_WAITING_TIME_TEST);
      assertEquals(token.getCurrentNode(), node3);
      
  }
  

  /**
   * Creates the process. It consists out of three nodes,
   * the central node is an intermediate timer event which waits for 300 MS until execution is continued
   */
  @BeforeClass
  public void beforeClass() {
      ProcessBuilder builder = new ProcessBuilderImpl();
      NodeParameter param = new NodeParameterImpl(
          new IntermediateTimer(IntermediateTimerTest.WAITING_TIME),
          new SimpleJoinBehaviour(),
          new TakeAllSplitBehaviour());
      
      NodeParameter nextParam = new NodeParameterImpl(
          mock(Activity.class),
          new SimpleJoinBehaviour(),
          new TakeAllSplitBehaviour());

      Navigator nav = new NavigatorImplMock();
      node = builder.createNode(nextParam);
      node2 = builder.createNode(param);
      node3 = builder.createNode(nextParam);
      builder.createTransition(node, node2);
      builder.createTransition(node2, node3);
      
      token = new TokenImpl(node, mock(ProcessInstance.class), nav);
  }

  /**
   * Delete used objects.
   */
  @AfterClass
  public void afterClass() {
      node = null;
      node2 = null;
      node3 = null;
      token = null;
  }

}
