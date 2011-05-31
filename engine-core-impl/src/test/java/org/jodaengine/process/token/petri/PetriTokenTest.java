package org.jodaengine.process.token.petri;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertEqualsNoOrder;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.node.activity.NullActivity;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
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
    private PetriNetPlace node = null, node3 = null, node5 = null;
    
    private PetriNetTransition node2, node4;

    /** The transition to be taken. */
    private Transition transitionToTake = null;
    
    private ProcessInstance instance;

    /**
     * Set up.     
     * An instance is build.
     */
    @BeforeMethod
    public void setUp() {

        node = new PetriNetPlace();
        node2 = new PetriNetTransition();
        node3 = new PetriNetPlace();
        node4 = new PetriNetTransition();
        node5 = new PetriNetPlace();

        node.transitionTo(node2);
        node2.transitionTo(node3);
        node3.transitionTo(node4);
        node4.transitionTo(node5);
        
        transitionToTake = place.getOutgoingTransitions().get(0);
        
        TokenBuilder tokenBuilder = new BpmnTokenBuilder(null, null, null);
        instance = new ProcessInstance(null, tokenBuilder);
        token = PetriToken(place, instance, null);
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
        assertEquals(newTokens.size(), 1, "You should have two new process tokens");


        Node[] expectedCurrentNodes = {node2};
        assertEqualsNoOrder(currentNodes, expectedCurrentNodes,
            "The new tokens should point to the following nodes.");
    }

    /**
     * Test the taking of a single transition.
     * 
     * @throws Exception if it fails
     */
    @Test
    public void testAPetriStepExecution() throws Exception {

        token.executeStep();
        assertEquals(instance.getAssignedTokens().size(), 1, "There should be one new token created");
        Token newToken = instance.getAssignedTokens().get(0);
        assertFalse(newToken == token, "A new token should be produced, and not the old one reused");
        assertEquals(newToken.getCurrentNode(), node3, "After one step the token should be located on the next place and NOT on the Transition");
    }


}
