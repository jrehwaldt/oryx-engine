package de.hpi.oryxengine.routing.behaviour;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;


import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.factory.RoutingBehaviourTestFactory;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;

/**
 * This class tests the BPMNAndJoin-Class.
 */
public class BPMNAndJoinTest {

    /** The node3. */
    private Node node1, node2, joinNode, splitNode, node3;

    /** The child instance2. */
    private Token token, childToken1, childToken2;

    /**
     * Sets the up.
     */
    @BeforeMethod
    public void setUp() {

        token = initializeTokens();
        List<Token> children = token.getChildTokens();
        childToken1 = children.get(0);
        childToken2 = children.get(1);
    }

    /**
     * Test single token reached join.
     */
    @Test
    public void testSingleTokenReachedJoin() {

        childToken1.setCurrentNode(joinNode);
        List<Token> newInstances = executeSplitAndJoin(childToken1);

        assertEquals(newInstances.size(), 0,
            "If only one child has reached the And Join, no new token should be scheduled");
        assertEquals(childToken1.getCurrentNode(), joinNode,
            "If only one child has reached the And Join, nothing should happen");
        assertEquals(token.getCurrentNode(), splitNode,
            "If only one child has reached the And Join, the parent token should not move on");
    }

    /**
     * Test all token reached join.
     */
    @Test
    public void testAllTokensReachedJoin() {

        childToken1.setCurrentNode(joinNode);
        childToken2.setCurrentNode(joinNode);
        List<Token> newTokens = executeSplitAndJoin(childToken1);
        assertEquals(newTokens.size(), 1, "There should only be one new token");

        Token newToken = newTokens.get(0);
        assertEquals(newToken, token,
            "The new token should be the parent of the token that executes the join behaviour");
        assertEquals(token.getCurrentNode(), node3,
            "If only one of the children has reached the And Join, the parent token should not move on");
    }

    // TODO Test with nested and splits and joins to simulate the situation that there are grandparents, etc.

    /**
     * Initialize tokens.
     * 
     * @return the process token
     */
    private Token initializeTokens() {

        splitNode = mock(Node.class);
        node1 = new RoutingBehaviourTestFactory().createWithAndSplit();
        node2 = new RoutingBehaviourTestFactory().createWithAndSplit();
        joinNode = new RoutingBehaviourTestFactory().createWithAndJoin();
        node1.transitionTo(joinNode);
        node2.transitionTo(joinNode);

        node3 = new RoutingBehaviourTestFactory().createWithAndSplit();
        joinNode.transitionTo(node3);

        token = new TokenImpl(splitNode);
        token.createChildToken(node1);
        token.createChildToken(node2);
        return token;
    }
    
    /**
     * Execute split and join.
     *
     * @param instance the token
     * @return the list
     */
    private List<Token> executeSplitAndJoin(Token token) {
        Node node = token.getCurrentNode();
        IncomingBehaviour incomingBehaviour = node.getIncomingBehaviour();
        OutgoingBehaviour outgoingBehaviour = node.getOutgoingBehaviour();
        
        List<Token> joinedTokens = incomingBehaviour.join(token);
        
        return outgoingBehaviour.split(joinedTokens);
    }
}
