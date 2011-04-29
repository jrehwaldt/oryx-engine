package de.hpi.oryxengine.routing.behaviour;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.impl.BPMNActivityFactory;
import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.AndJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;

/**
 * This class tests the BPMNAndJoin-Class.
 */
public class BPMNAndJoinTest {

    /** The node3. */
    private Node node1 = null, node2 = null, joinNode = null, splitNode = null, node3 = null;

    /** The child instance2. */
    private Token newToken1 = null, newToken2 = null;

    private NavigatorImplMock navigator = null;

    /**
     * Sets the up.
     */
    @BeforeMethod
    public void setUp() {

        List<Token> tokens = initializeTokens();
        newToken1 = tokens.get(0);
        newToken2 = tokens.get(1);
    }

    /**
     * Test single token reached join.
     */
    @Test
    public void testSingleTokenReachedJoin() {

        List<Token> newTokens = null;
        try {
            newToken1.executeStep();
            navigator.flushWorkQueue();
            newToken1.executeStep();
            newTokens = navigator.getWorkQueue();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(newTokens.size(), 0,
            "If only one child has reached the And Join, no new token should be scheduled");
        assertEquals(newToken1.getCurrentNode(), joinNode,
            "If only one child has reached the And Join, nothing should happen");
    }

    /**
     * Test all token reached join.
     */
    @Test
    public void testAllTokensReachedJoin() {

        List<Token> newTokens = null;
        try {
            newToken1.executeStep();
            newToken2.executeStep();
            navigator.flushWorkQueue();
            newToken2.executeStep();
            navigator.flushWorkQueue();
            newToken1.executeStep();
            newTokens = navigator.getWorkQueue();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(newTokens.size(), 1, "There should only be one new token");

        Token newToken = newTokens.get(0);
        assertEquals(newToken.getCurrentNode(), node3, "The new token should be on the node following the join node");
    }

    /**
     * Initialize tokens.
     * 
     * @return the process token
     */
    private List<Token> initializeTokens() {

        navigator = new NavigatorImplMock();

        splitNode = mock(Node.class);

        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();

        node1 = BPMNActivityFactory.createBPMNNullNode(builder);
        node2 = BPMNActivityFactory.createBPMNNullNode(builder);

        joinNode = BPMNActivityFactory.createBPMNAndGatewayNode(builder);
        
        node3 = BPMNActivityFactory.createBPMNNullNode(builder);
        
        BPMNActivityFactory.createTransitionFromTo(builder, node1, joinNode);
        BPMNActivityFactory.createTransitionFromTo(builder, node2, joinNode);
        BPMNActivityFactory.createTransitionFromTo(builder, joinNode, node3);

        Token token = new TokenImpl(splitNode, new ProcessInstanceImpl(null), navigator);

        List<Token> newTokens = new ArrayList<Token>();
        newTokens.add(token.createNewToken(node1));
        newTokens.add(token.createNewToken(node2));
        return newTokens;
    }
    /**
     * Execute split and join.
     * 
     * @param token
     *            the token
     * @return the list
     * @throws Exception
     *             the exception
     */
    /*
     * private List<Token> executeSplitAndJoin(Token token) throws Exception {
     * 
     * Node node = token.getCurrentNode(); IncomingBehaviour incomingBehaviour = node.getIncomingBehaviour();
     * OutgoingBehaviour outgoingBehaviour = node.getOutgoingBehaviour();
     * 
     * List<Token> joinedTokens = incomingBehaviour.join(token);
     * 
     * return outgoingBehaviour.split(joinedTokens); }
     */
}
