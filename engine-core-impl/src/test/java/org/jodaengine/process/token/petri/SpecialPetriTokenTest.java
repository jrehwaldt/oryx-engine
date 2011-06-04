package org.jodaengine.process.token.petri;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

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
 * The test for the process instance.
 */
public class SpecialPetriTokenTest {

    /** The process instance. */
    private Token token2 = null, token3 = null;

    /** Different Nodes. */
    private Node node = null, node3 = null, node5 = null, node6 = null;
    
    private Node node2, node4;
    
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
        // Transition
        node2 = new NodeImpl(new NullActivity(), new TransitionJoinBehaviour(), new TransitionSplitBehaviour());
        node2.setAttribute("name", "2");
        // Place
        node3 = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        node3.setAttribute("name", "3");
        // Transition
        node4 = new NodeImpl(new NullActivity(), new TransitionJoinBehaviour(), new TransitionSplitBehaviour());
        node4.setAttribute("name", "4");
        // Place
        node5 = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        node5.setAttribute("name", "5");
        // Place
        node6 = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        node6.setAttribute("name", "6");

        node.transitionTo(node2);
        node2.transitionTo(node3);
        node.transitionTo(node4);
        node5.transitionTo(node4);
        node4.transitionTo(node6);
        
        
        TokenBuilder tokenBuilder = new PetriTokenBuilder(Mockito.mock(Navigator.class), null);
        instance = new ProcessInstance(null, tokenBuilder);
        instance.createToken(node);
        token2 = instance.createToken(node5);
        token3 = instance.createToken(node);
        
        util = new TokenUtil();
    }

    /**
     * Tests the lower path.
     * 
     * @throws Exception if it fails
     */
    @Test
    public void testTheLowerPetriStepExecution() throws Exception {

        Token newToken;
        token2.executeStep();
        assertEquals(instance.getAssignedTokens().size(), 2, "There should be one token consumed,"
            + " and one left at the end and one left at the beginning node.");
        assertEquals(util.getTokensWhichAreOnNode(node6, instance).size(), 1);
        newToken = util.getTokensWhichAreOnNode(node6, instance).get(0);
        assertFalse(newToken == token2, "A new token should be produced, and not the old one reused");
        assertEquals(token3.getCurrentNode(), node, "The third token should not be affected");
        
    }

    
    /**
     * Tests both ways of the example petri net. First the lower way is taken, then the upper way.
     * 
     * @throws Exception if it fails
     */
    @Test
    public void testBothSimplePetriStepExecution() throws Exception {
        
        token2.executeStep();
        // We don't now which token is left, so lookup one of it
        Token remainingToken = util.getTokensWhichAreOnNode(node, instance).get(0);
        remainingToken.executeStep();

        assertEquals(instance.getAssignedTokens().size(), 2, "There should one token be consumed and two at the end.");
        assertEquals(util.getTokensWhichAreOnNode(node3, instance).size(), 1);
        assertEquals(util.getTokensWhichAreOnNode(node6, instance).size(), 1);
        
    }
    
    /**
     * Tests dead ends.
     * 
     * @throws Exception if it fails
     */
    @Test
    public void testdeadEndPetriStepExecution() throws Exception {
        
        token2.executeStep();
        // We dont now which token is left, so lookup one of it
        Token remainingToken;
        remainingToken = util.getTokensWhichAreOnNode(node, instance).get(0);
        remainingToken.executeStep();
        
        // Both paths are at the end, now try to execute one of the tokens, which are located at the end.
        remainingToken = util.getTokensWhichAreOnNode(node3, instance).get(0);
        remainingToken.executeStep();
        
        // There should not occur an error, nor should there be new tokens created.
        assertEquals(instance.getAssignedTokens().size(), 2, "There should be no new tokens.");
        assertEquals(util.getTokensWhichAreOnNode(node3, instance).size(), 1);
        assertEquals(util.getTokensWhichAreOnNode(node6, instance).size(), 1);
        
    }


}
