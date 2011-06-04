package org.jodaengine.process.token.petri;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

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
import org.jodaengine.process.token.builder.PetriTokenBuilder;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * The test for the process instance.
 */
public class PetriTokenTest {

    /** The process instance. */
    private Token token = null;

    /** Different Nodes. */
    private Node node = null, node3 = null, node5 = null;
    
    private Node node2, node4;
    
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
        // Transition
        node4 = new NodeImpl(new NullActivity(), new TransitionJoinBehaviour(), new TransitionSplitBehaviour());
        node4.setAttribute("name", "4");
        // Place
        node5 = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        node5.setAttribute("name", "5");

        node.transitionTo(node2);
        node2.transitionTo(node3);
        node3.transitionTo(node4);
        node4.transitionTo(node5);
        
        
        TokenBuilder tokenBuilder = new PetriTokenBuilder(Mockito.mock(Navigator.class), null);
        instance = new ProcessInstance(null, tokenBuilder);
        token = instance.createToken(node);
    }

    /**
     * Test for taking all transitions.
     * Two new tokens shall be ready for execution if the parent token goes along all edges.
     * The new tokens should then point to the succeeding nodes of the initial token's node.
     *
     * @throws Exception the exception
     */
    @Test
    public void testTakeTransition() throws Exception {

        Node currentNode = token.getCurrentNode();
        
        List<Token> newTokens = token.navigateTo(currentNode.getOutgoingTransitions());
        
        assertEquals(instance.getAssignedTokens().size(), 1, "There should be one moved token.");
        assertEquals(newTokens.get(0).getCurrentNode(), node2,
            "The new token should point to the following node.");
    }

    /**
     * Test the taking of a single transition.
     * 
     * @throws Exception if it fails
     */
    @Test
    public void testSimplePetriStepExecution() throws Exception {

        Token newToken;
        token.executeStep();
        assertEquals(instance.getAssignedTokens().size(), 1, "There should be one new token created");
        newToken = instance.getAssignedTokens().get(0);
        assertFalse(newToken == token, "A new token should be produced, and not the old one reused");
        assertEquals(newToken.getCurrentNode(), node3,
            "After one step the token should be located on the next place and NOT on the Transition");
        
        newToken.executeStep();
        newToken = instance.getAssignedTokens().get(0);
        assertEquals(newToken.getCurrentNode(), node5,
        "After two steps the token should be locatedon the last place");
        
    }


}
