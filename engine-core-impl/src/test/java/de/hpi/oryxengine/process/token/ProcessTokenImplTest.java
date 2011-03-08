package de.hpi.oryxengine.process.token;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.*;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.process.structure.Transition;

/**
 * The test for the process instance.
 */
public class ProcessTokenImplTest {

    /** The process instance. */
    private Token token;

    /** Different Nodes. */
    private NodeImpl node, node2, node3;

    /** The transition to be taken. */
    private Transition transitionToTake;

    /**
     * Set up.     
     * An instance is build.
     */
    @BeforeTest
    public void setUp() {

        token = simpleToken();
    }

    /**
     * Test for taking all transitions.
     * TODO see if description is correct
     * Two new tokens shall be ready for execution if the parent token goes along all edges.
     * Both of them should have the same parent token.
     * The new tokens should then point to the succeeding nodes of the parent token's node.
     * @throws Exception 
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
     * Test take single transition.
     * TODO JavaDoc for the test
     * @throws Exception 
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
     * Simple token.
     * TODO more JavaDoc description
     * 
     * @return the process instance impl
     */
    private TokenImpl simpleToken() {

        Activity activity = mock(Activity.class);

        node = new NodeImpl(activity);
        node2 = new NodeImpl(activity);
        node3 = new NodeImpl(activity);
        node.transitionTo(node2);

        transitionToTake = node.getOutgoingTransitions().get(0);

        node.transitionTo(node3);

        return new TokenImpl(node);
    }
}
