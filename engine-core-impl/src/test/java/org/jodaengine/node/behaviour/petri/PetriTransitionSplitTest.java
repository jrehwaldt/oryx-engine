package org.jodaengine.node.behaviour.petri;

import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.exception.NoValidPathException;
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
import org.jodaengine.process.token.builder.PetriTokenBuilder;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * The Class PlaceSplitTest. Tests the Class TransitionSplitBehaviour.
 */
public class PetriTransitionSplitTest {
    
    /** The process instance. */
    private Token token = null;

    /** Different Nodes. */
    private Node node2, node3;
    
    private ProcessInstance instance;
    
    /**
     * Set up.     
     * An instance is build.
     */
    @BeforeMethod
    public void setUp() {

        // Transition, we start right here (just for testing purposes)
        node2 = new NodeImpl(new NullActivity(), new TransitionJoinBehaviour(), new TransitionSplitBehaviour());
        node2.setAttribute("name", "2");
        // Place
        node3 = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        node3.setAttribute("name", "3");

        node2.transitionTo(node3);

        TokenBuilder tokenBuilder = new PetriTokenBuilder(Mockito.mock(Navigator.class), null);
        instance = new ProcessInstance(null, tokenBuilder);
        token = instance.createToken(node2);
    }
    
    
  /**
   * Tests if a new token has been produced, which is located on the last node.
   *
   * @throws NoValidPathException the no valid path exception
   */
  @Test
  public void testMovedToken() throws NoValidPathException {
      List<Token> tokens = new ArrayList<Token>();
      tokens.add(token);
      token.getCurrentNode().getOutgoingBehaviour().split(tokens);
      
      Token newToken = instance.getAssignedTokens().get(0);
      assertTrue(newToken.getCurrentNode() == node3, "A new token should have been generated which"
          + " is located on the next place.");
      assertTrue(instance.getAssignedTokens().size() == 1, "There should be just one token left.");
      assertNotSame(token, newToken, "It should be a new token, not the old one.");
      
  }
  

}
