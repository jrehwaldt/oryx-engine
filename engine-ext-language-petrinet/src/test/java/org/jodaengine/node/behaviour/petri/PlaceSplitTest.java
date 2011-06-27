package org.jodaengine.node.behaviour.petri;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.jodaengine.exception.NoValidPathException;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.node.activity.NullActivity;
import org.jodaengine.node.incomingbehaviour.petri.TransitionJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.petri.PlaceSplitBehaviour;
import org.jodaengine.node.outgoingbehaviour.petri.TransitionSplitBehaviour;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeImpl;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenBuilder;
import org.jodaengine.process.token.builder.PetriNetTokenBuilder;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * The Class PlaceSplitTest. Tests the Class PlaceSplitBehaviour.
 */
public class PlaceSplitTest {
    
    /** The process instance. */
    private Token token = null;

    /** Different Nodes. */
    private Node node, node2, node3;
    
    private ProcessInstance instance;
    
    /**
     * Set up.     
     * An instance is build.
     */
    @BeforeMethod
    public void setUp() {

        // Place
        node = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        node.setAttribute("name", "1");
        // Transition
        node2 = new NodeImpl(new NullActivity(), new TransitionJoinBehaviour(), new TransitionSplitBehaviour());
        node2.setAttribute("name", "2");
        // Place
        node3 = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        node3.setAttribute("name", "3");

        node.controlFlowTo(node2);
        node2.controlFlowTo(node3);

        TokenBuilder tokenBuilder = new PetriNetTokenBuilder(Mockito.mock(Navigator.class), null);
        instance = new ProcessInstance(null, tokenBuilder);
        token = instance.createToken(node);
    }
    
    
  /**
   * Tests if the token was moved.
   *
   * @throws NoValidPathException the no valid path exception
   */
  @Test
  public void testMovedToken() throws NoValidPathException {
      Collection<Token> tokens = new ArrayList<Token>();
      Collection<Token> movedTokens = new ArrayList<Token>();
      tokens.add(token);
      
      movedTokens = token.getCurrentNode().getOutgoingBehaviour().split(tokens);
      assertTrue(movedTokens.iterator().next().getCurrentNode() == node2, "The returned token should be moved."); 
      
  }
  
  /**
   * Tests a case, where there is no possible {@link ControlFlow}s to take. The return value should be null.
   *
   * @throws NoValidPathException the no valid path exception
   */
  @Test
  public void testNotMovedToken() throws NoValidPathException {
      
      // Place
      Node node4 = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
      node4.setAttribute("name", "4");
      node4.controlFlowTo(node2);
      
      Collection<Token> tokens = new ArrayList<Token>();
      Collection<Token> movedTokens = null;
      tokens.add(token);
      
      movedTokens = token.getCurrentNode().getOutgoingBehaviour().split(tokens);
      assertTrue(movedTokens == null, "There should be no returned tokens,"
          + " because the the following PetriTransition can't be executed. There is a token on a place missing."); 
      
  }
}
