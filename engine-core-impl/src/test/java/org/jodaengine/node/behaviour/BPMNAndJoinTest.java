package org.jodaengine.node.behaviour;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.navigator.NavigatorImplMock;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.process.definition.bpmn.BpmnProcessDefinitionBuilder;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.AbstractToken;
import org.jodaengine.process.token.BpmnToken;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.builder.BpmnTokenBuilder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


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
     * @throws IllegalStarteventException 
     */
    @BeforeMethod
    public void setUp() throws IllegalStarteventException {

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
        assertEquals(newToken.getInstance().getAssignedTokens().size(), 1);
        assertEquals(newToken.getCurrentNode(), node3, "The new token should be on the node following the join node");
    }

    /**
     * Initialize tokens.
     * 
     * @return the process token
     * @throws IllegalStarteventException 
     */
    private List<Token> initializeTokens() throws IllegalStarteventException {

        navigator = new NavigatorImplMock();

        splitNode = mock(Node.class);

        BpmnProcessDefinitionBuilder builder = BpmnProcessDefinitionBuilder.newBuilder();
        node1 = BpmnCustomNodeFactory.createBpmnNullNode(builder);
        node2 = BpmnCustomNodeFactory.createBpmnNullNode(builder);

        joinNode = BpmnNodeFactory.createBpmnAndGatewayNode(builder);
        
        node3 = BpmnCustomNodeFactory.createBpmnNullNode(builder);
        
        BpmnNodeFactory.createControlFlowFromTo(builder, node1, joinNode);
        BpmnNodeFactory.createControlFlowFromTo(builder, node2, joinNode);
        BpmnNodeFactory.createControlFlowFromTo(builder, joinNode, node3);

        BpmnTokenBuilder tokenBuilder = new BpmnTokenBuilder(navigator, null);
        AbstractToken token = new BpmnToken(splitNode, new ProcessInstance(null, tokenBuilder), navigator);

        List<Token> newTokens = new ArrayList<Token>();
        newTokens.add(token.createToken(node1));
        newTokens.add(token.createToken(node2));
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
