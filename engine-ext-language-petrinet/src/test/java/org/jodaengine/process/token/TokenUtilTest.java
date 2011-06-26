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
import org.jodaengine.process.token.builder.PetriNetTokenBuilder;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * The test for the utility class.
 */
public class TokenUtilTest {

    /** Different Nodes. */
    private Node startPlace, petriTransition, endPlace;
    
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
        startPlace = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        startPlace.setAttribute("name", "1");
        // Transition
        petriTransition = new NodeImpl(new NullActivity(), new TransitionJoinBehaviour(), new TransitionSplitBehaviour());
        petriTransition.setAttribute("name", "2");
        // Place
        endPlace = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        endPlace.setAttribute("name", "3");


        startPlace.controlFlowTo(petriTransition);
        petriTransition.controlFlowTo(endPlace);
        
        TokenBuilder tokenBuilder = new PetriNetTokenBuilder(Mockito.mock(Navigator.class), null);
        instance = new ProcessInstance(null, tokenBuilder);
        
        util = new TokenUtil();
        
        instance.createToken(startPlace);
        instance.createToken(startPlace);
        instance.createToken(startPlace);
        instance.createToken(startPlace);
        instance.createToken(endPlace);
        instance.createToken(endPlace);
    }

     /**
      * Tests the count of how many tokens remain on a node.
      */
     @Test
     public void testTokensOnNode() {
         assertTrue(util.getTokensWhichAreOnNode(startPlace, instance).size() == TOKENS_ON_NODE);
         assertTrue(util.getTokensWhichAreOnNode(endPlace, instance).size() == TOKENS_ON_NODE3);
         
     }
}
