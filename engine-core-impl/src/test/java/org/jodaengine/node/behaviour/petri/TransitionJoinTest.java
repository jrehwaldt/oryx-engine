package org.jodaengine.node.behaviour.petri;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.jodaengine.navigator.Navigator;
import org.jodaengine.node.activity.NullActivity;
import org.jodaengine.node.incomingbehaviour.petri.TransitionJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.petri.PlaceSplitBehaviour;
import org.jodaengine.node.outgoingbehaviour.petri.TransitionSplitBehaviour;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeImpl;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenBuilder;
import org.jodaengine.process.token.TokenUtil;
import org.jodaengine.process.token.builder.PetriTokenBuilder;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * The Class PlaceSplitTest. Tests the Class TransitionSplitBehaviour.
 */
public class TransitionJoinTest {
    
    /** The process instance. */
    private Token token = null;

    /** Different Nodes. */
    private Node node, node2, node3, node4;
    
    private ProcessInstance instance;
    private TokenUtil util;
    
    /**
     * Set up.     
     * An instance is build.
     */
    @BeforeMethod
    public void setUp() {

        // Place
        node = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        node.setAttribute("name", "1");
        
        // Transition, we start right here (just for testing purposes)
        node2 = new NodeImpl(new NullActivity(), new TransitionJoinBehaviour(), new TransitionSplitBehaviour());
        node2.setAttribute("name", "2");
        
        // Place
        node3 = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        node3.setAttribute("name", "3");
        
        // Place
        node4 = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        node4.setAttribute("name", "4");

        node.transitionTo(node2);
        node3.transitionTo(node2);
        node2.transitionTo(node4);

        util = new TokenUtil();
        TokenBuilder tokenBuilder = new PetriTokenBuilder(Mockito.mock(Navigator.class), null);
        instance = new ProcessInstance(null, tokenBuilder);
        token = instance.createToken(node);
    }
    
    
  /**
   * Test false join ability.
   */
  @Test
  public void testFalseJoinAbility() {
      assertFalse(node2.getIncomingBehaviour().joinable(token, node2), "It should return false,"
          + " because a token is missing.");
      
  }
  
  /**
   * Test true join ability.
   */
  @Test
  public void testTrueJoinAbility() {
      instance.createToken(node3);
      assertTrue(node2.getIncomingBehaviour().joinable(token, node2), "It should return true,"
          + " because the missing token is there.");
      
  }
  
  /**
   * Test consuming tokens after join.
   */
  @Test
  public void testConsumingTokensAfterJoin() {
      instance.createToken(node3);
      token.setCurrentNode(node2);
      node2.getIncomingBehaviour().join(token);
      
      assertTrue(instance.getAssignedTokens().size() == 1, "There should be now just one token left.");
      assertTrue(util.getTokensWhichAreOnPlace(node3, instance).size() == 0, "Start nodes should be token free.");
      assertTrue(util.getTokensWhichAreOnPlace(node, instance).size() == 0, "Start nodes should be token free.");
      
  }
  
  /**
   * Tests the join.
   */
  @Test
  public void testJoin() {
      instance.createToken(node3);
      token.setCurrentNode(node2);
      
      List<Token> newTokens = null;
      newTokens = node2.getIncomingBehaviour().join(token);
      
      assertTrue(newTokens.size() == 1, "One token was moved, so the size should be 1.");
      
      
  }
  

}
