package org.jodaengine.process.token.petri;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.List;

import org.jodaengine.node.activity.NullActivity;
import org.jodaengine.node.incomingbehaviour.petri.TransitionJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.petri.PlaceSplitBehaviour;
import org.jodaengine.node.outgoingbehaviour.petri.TransitionSplitBehaviour;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeImpl;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.process.token.PetriToken;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenBuilder;
import org.jodaengine.process.token.builder.BpmnTokenBuilder;
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
        // Transition
        node2 = new NodeImpl(new NullActivity(), new TransitionJoinBehaviour(), new TransitionSplitBehaviour());
        // Place
        node3 = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        // Transition
        node4 = new NodeImpl(new NullActivity(), new TransitionJoinBehaviour(), new TransitionSplitBehaviour());
        // Place
        node5 = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());

        node.transitionTo(node2);
        node2.transitionTo(node3);
        node3.transitionTo(node4);
        node4.transitionTo(node5);
        
        
        TokenBuilder tokenBuilder = new BpmnTokenBuilder(null, null);
        instance = new ProcessInstance(null, tokenBuilder);
        token = new PetriToken(node, instance, null);
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
        assertEquals(newTokens.size(), 1, "You should have one new token");


        assertEquals(newTokens.get(0).getCurrentNode(), node2,
            "The new token should point to the following nodes.");
    }

    /**
     * Test the taking of a single transition.
     * 
     * @throws Exception if it fails
     */
    @Test
    public void testPetriStepExecution() throws Exception {

        token.executeStep();
        assertEquals(instance.getAssignedTokens().size(), 1, "There should be one new token created");
        Token newToken = instance.getAssignedTokens().get(0);
        assertFalse(newToken == token, "A new token should be produced, and not the old one reused");
        assertEquals(newToken.getCurrentNode(), node3,
            "After one step the token should be located on the next place and NOT on the Transition");
    }


}
