package org.jodaengine.node.behaviour;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.navigator.NavigatorImplMock;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.process.instance.ProcessInstanceImpl;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BPMNToken;
import org.jodaengine.process.token.BPMNTokenImpl;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * This class tests the BPMNAndJoin-Class.
 */
public class BPMNAndJoinTest {

    /** The node3. */
    private Node node1 = null, node2 = null, joinNode = null, splitNode = null, node3 = null;

    /** The child instance2. */
    private BPMNToken newToken1 = null, newToken2 = null;

    private NavigatorImplMock navigator = null;

    /**
     * Sets the up.
     */
    @BeforeMethod
    public void setUp() {

        List<BPMNToken> bPMNTokens = initializeTokens();
        newToken1 = bPMNTokens.get(0);
        newToken2 = bPMNTokens.get(1);
    }

    /**
     * Test single token reached join.
     */
    @Test
    public void testSingleTokenReachedJoin() {

        List<BPMNToken> newTokens = null;
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

        List<BPMNToken> newTokens = null;
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

        BPMNToken newToken = newTokens.get(0);
        assertEquals(newToken.getInstance().getAssignedTokens().size(), 1);
        assertEquals(newToken.getCurrentNode(), node3, "The new token should be on the node following the join node");
    }

    /**
     * Initialize tokens.
     * 
     * @return the process token
     */
    private List<BPMNToken> initializeTokens() {

        navigator = new NavigatorImplMock();

        splitNode = mock(Node.class);

        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();

        node1 = BpmnCustomNodeFactory.createBpmnNullNode(builder);
        node2 = BpmnCustomNodeFactory.createBpmnNullNode(builder);

        joinNode = BpmnNodeFactory.createBpmnAndGatewayNode(builder);
        
        node3 = BpmnCustomNodeFactory.createBpmnNullNode(builder);
        
        BpmnNodeFactory.createTransitionFromTo(builder, node1, joinNode);
        BpmnNodeFactory.createTransitionFromTo(builder, node2, joinNode);
        BpmnNodeFactory.createTransitionFromTo(builder, joinNode, node3);

        BPMNToken bPMNToken = new BPMNTokenImpl(splitNode, new ProcessInstanceImpl(null), navigator);

        List<BPMNToken> newTokens = new ArrayList<BPMNToken>();
        newTokens.add(bPMNToken.createNewToken(node1));
        newTokens.add(bPMNToken.createNewToken(node2));
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
