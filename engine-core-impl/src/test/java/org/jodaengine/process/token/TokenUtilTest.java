package org.jodaengine.process.token;

import static org.testng.Assert.assertTrue;

import org.jodaengine.navigator.Navigator;
import org.jodaengine.node.activity.NullActivity;
import org.jodaengine.node.incomingbehaviour.petri.TransitionJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.petri.PlaceSplitBehaviour;
import org.jodaengine.node.outgoingbehaviour.petri.TransitionSplitBehaviour;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeImpl;
import org.jodaengine.process.token.builder.PetriTokenBuilder;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * The test for the utility class.
 */
public class TokenUtilTest {

    /** Different Nodes. */
    private Node node, node2, node3;
    
    private ProcessInstance instance;
    private TokenUtil util;
    private final static int TOKENS_ON_NODE = 4;
    private final static int TOKENS_ON_NODE3 = 2;

    /**
     * Set up.     
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


        node.transitionTo(node2);
        node2.transitionTo(node3);
        
        TokenBuilder tokenBuilder = new PetriTokenBuilder(Mockito.mock(Navigator.class), null);
        instance = new ProcessInstance(null, tokenBuilder);
        
        util = new TokenUtil();
        
        instance.createToken(node);
        instance.createToken(node);
        instance.createToken(node);
        instance.createToken(node);
        instance.createToken(node3);
        instance.createToken(node3);
    }

     /**
      * Tests the count of how many tokens remain on a node.
      */
     @Test
     public void testTokensOnNode() {
         assertTrue(util.getTokensWhichAreOnNode(node, instance).size() == TOKENS_ON_NODE);
         assertTrue(util.getTokensWhichAreOnNode(node3, instance).size() == TOKENS_ON_NODE3);
         
     }
}
