package de.hpi.oryxengine.plugin.activity;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.node.activity.fun.AutomatedDummyActivity;
import de.hpi.oryxengine.process.structure.ActivityBlueprint;
import de.hpi.oryxengine.process.structure.ActivityBlueprintImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.process.token.TokenImpl;

/**
 * The Class TokenPluginTest.
 */
public class TokenPluginTest {
  private TokenImpl token;
  private ArgumentCaptor<ActivityLifecycleChangeEvent> eventCapturer = null;
  private AbstractTokenPlugin mock;
    
  /**
   * Sets up a token that points to a node and registers a mocked plugin.
   */
  @BeforeMethod
  public void setUp() {
      Node node1, node2;
      Class<?>[] conSig = {String.class};
      Object[] conArgs = {"s.out"};
      ActivityBlueprint blueprint = new ActivityBlueprintImpl(AutomatedDummyActivity.class, conSig, conArgs);
      node1 = new NodeImpl(blueprint);
      this.token = new TokenImpl(node1);
      
      mock = mock(AbstractTokenPlugin.class);
      token.registerPlugin(mock);
      this.eventCapturer = ArgumentCaptor.forClass(ActivityLifecycleChangeEvent.class);
  }
  
  /**
   * Test the deregistration of the plugin.
   *
   * @throws DalmatinaException the dalmatina exception
   */
  @Test
  public void testDeregistration() throws DalmatinaException {
      token.deregisterPlugin(mock);
      token.executeStep();
      verify(mock, never()).update(eq(this.token), this.eventCapturer.capture());
  }
  
  /**
   * Tests that new tokens that are created by this token receive the same plugins the creator has.
   *
   * @throws DalmatinaException the dalmatina exception
   */
  @Test
  public void testPluginRegistrationInheritance() throws DalmatinaException {
      TokenImpl newToken = (TokenImpl) token.createNewToken(token.getCurrentNode());
      newToken.executeStep();
      verify(mock, times(2)).update(eq(newToken), this.eventCapturer.capture());
  }
  
  /**
   * Tests that newly created tokens do not receive plugins that were deregistered before.
   *
   * @throws DalmatinaException the dalmatina exception
   */
  @Test
  public void testPluginDeregistrationInheritance() throws DalmatinaException {
      token.deregisterPlugin(mock);
      TokenImpl newToken = (TokenImpl) token.createNewToken(token.getCurrentNode());
      newToken.executeStep();
      verify(mock, never()).update(eq(newToken), this.eventCapturer.capture());
  }
}
