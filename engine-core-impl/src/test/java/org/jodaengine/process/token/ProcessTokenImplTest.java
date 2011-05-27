package org.jodaengine.process.token;

import static org.testng.Assert.assertEquals;
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
import org.jodaengine.process.token.builder.BpmnTokenBuilder;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * The test for the process instance.
 */
public class ProcessTokenImplTest {

    /** The process instance. */
    private Token token = null;

    /** Different Nodes. */
    private NodeImpl node = null, node2 = null, node3 = null;

    /** The transition to be taken. */
    private Transition transitionToTake = null;

    /**
     * Set up.     
     * An instance is build.
     */
    @BeforeMethod
    public void setUp() {

        token = simpleToken();
    }

    /**
     * Test for taking all transitions.
     * Two new tokens shall be ready for execution if the parent token goes along all edges.
     * The new tokens should then point to the succeeding nodes of the initial token's node.
     *
     * @throws Exception the exception
     */
    @Test
    public void testTakeAllTransitions() throws Exception {

        Node currentNode = token.getCurrentNode();
        
        List<Token> newTokens = token.navigateTo(currentNode.getOutgoingTransitions());
        assertEquals(newTokens.size(), 2, "You should have two new process tokens");

        Node[] currentNodes = new Node[2];
        for (int i = 0; i < 2; i++) {
            currentNodes[i] = newTokens.get(i).getCurrentNode();
        }

        Node[] expectedCurrentNodes = {node2, node3};
        assertEqualsNoOrder(currentNodes, expectedCurrentNodes,
            "The new tokens should point to the following nodes.");
    }

    /**
     * Test the taking of a single transition.
     * 
     * @throws Exception if it fails
     */
    @Test
    public void testTakeSingleTransition() throws Exception {

        List<Transition> transitionList = new ArrayList<Transition>();
        transitionList.add(transitionToTake);
        List<Token> newTokens = token.navigateTo(transitionList);
        assertEquals(newTokens.size(), 1, "You should have a single process token.");

        Token newToken = newTokens.get(0);
        assertEquals(newToken, token,
            "The token should be the same, no child instance or something like that.");
        assertEquals(newToken.getCurrentNode(), node2, "The token should have moved on.");
    }

    /**
     * Creates a simple token.
     * 
     * @return the process instance impl
     */
    private TokenImpl simpleToken() {
        
        node = new NodeImpl(new NullActivity(), new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
        node2 = new NodeImpl(new NullActivity(), new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
        node3 = new NodeImpl(new NullActivity(), new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
        node.transitionTo(node2);
        
        transitionToTake = node.getOutgoingTransitions().get(0);
        
        node.transitionTo(node3);
        
        TokenBuilder tokenBuilder = new BpmnTokenBuilder(null, null);
        return new BpmnToken(node, new ProcessInstance(null, tokenBuilder), null);
    }
}
