package de.hpi.oryxengine.routing.behaviour;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.node.factory.bpmn.BpmnFunNodeFactory;
import de.hpi.oryxengine.node.factory.bpmn.BpmnNodeFactory;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;

/**
 * The Class BPMNUnstructuredJoinSplitTest. We want to test that unstructured AND splits and joins are handled
 * correctly.
 */
public class BPMNUnstructuredJoinSplitTest {

    /** The initial token. */
    private Token initialToken = null;

    /** The end node. */
    private Node node1 = null, node2 = null, node3 = null, innerJoinNode = null, outerJoinNode = null, endNode = null;

    private NavigatorImplMock navigator = null;

    /**
     * Before test.
     */
    @BeforeClass
    public void beforeTest() {

        initialToken = initializeToken();
    }

    /**
     * Test overall behaviour. Start with the execution of the first node, validate results on the way and the final
     * result.
     */
    @Test
    public void testOverallBehaviour() {

        List<Token> newTokens;
        try {
            initialToken.executeStep();
            newTokens = navigator.getWorkQueue();
            assertEquals(newTokens.size(), 2 + 1,
                "Two new tokens have been created, one one the inner join node and one on node3");

            // we need to find out, which of the new tokens is on node3 and which on the inner join node
            Token tokenOnNode1 = tokenOnNode(newTokens, node1);
            Token tokenOnNode2 = tokenOnNode(newTokens, node2);
            Token tokenOnNode3 = tokenOnNode(newTokens, node3);
            navigator.flushWorkQueue();

            // Execute node 1 and 2.
            tokenOnNode1.executeStep();
            tokenOnNode2.executeStep();
            navigator.flushWorkQueue();

            // Execute join node, actually tokenOnNode1 and tokenOnNode2 have advanced to the join Node
            tokenOnNode1.executeStep();

            newTokens = navigator.getWorkQueue();
            assertEquals(newTokens.size(), 0,
                "Only the first token has signaled, that it has arrived, no tokens should have been synchronized");

            tokenOnNode2.executeStep();
            newTokens = navigator.getWorkQueue();
            assertEquals(newTokens.size(), 1, "The two tokens should be synchronized now");

            Token innerJoinedToken = newTokens.get(0);
            assertEquals(innerJoinedToken.getCurrentNode(), outerJoinNode, "It should be on the outer join now.");
            navigator.flushWorkQueue();

            // Execute node 3
            tokenOnNode3.executeStep();

            // Execute outer join
            tokenOnNode3.executeStep();
            navigator.flushWorkQueue();

            innerJoinedToken.executeStep();

            newTokens = navigator.getWorkQueue();
            assertEquals(newTokens.size(), 1, "Again, there should be one synchronized token");
            assertEquals(newTokens.get(0).getCurrentNode(), endNode,
                "It should point to the node after the outer join, which is the endNode");
            navigator.flushWorkQueue();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Initialize tokens and graph structure.
     *   /-> N1-\
     * SN        -> IJN -> OJN -> EN
     *  \\-> N2-/      /
     *   \-> N3-------/
     * @return the list
     */
    private Token initializeToken() {

        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();
        
        Node splitNode = BpmnFunNodeFactory.createBpmnNullNode(builder);
        node1 = BpmnFunNodeFactory.createBpmnNullNode(builder);
        node2 = BpmnFunNodeFactory.createBpmnNullNode(builder);
        node3 = BpmnFunNodeFactory.createBpmnNullNode(builder);

        innerJoinNode = BpmnNodeFactory.createBpmnAndGatewayNode(builder);
        outerJoinNode = BpmnNodeFactory.createBpmnAndGatewayNode(builder);

//        endNode = new RoutingBehaviourTestFactory().createWithAndSplit();
        endNode = BpmnFunNodeFactory.createBpmnNullNode(builder);

        BpmnNodeFactory.createTransitionFromTo(builder, splitNode, node1);
        BpmnNodeFactory.createTransitionFromTo(builder, splitNode, node2);
        BpmnNodeFactory.createTransitionFromTo(builder, splitNode, node3);
        BpmnNodeFactory.createTransitionFromTo(builder, node1, innerJoinNode);
        BpmnNodeFactory.createTransitionFromTo(builder, node2, innerJoinNode);
        BpmnNodeFactory.createTransitionFromTo(builder, innerJoinNode, outerJoinNode);
        BpmnNodeFactory.createTransitionFromTo(builder, node3, outerJoinNode);
        BpmnNodeFactory.createTransitionFromTo(builder, outerJoinNode, endNode);
        
        navigator = new NavigatorImplMock();

        Token token = new TokenImpl(splitNode, new ProcessInstanceImpl(null), navigator);

        return token;
    }

    /**
     * Returns the token out of the candidate nodes that points to the given node.
     * 
     * @param candidateTokens
     *            the candidate tokens
     * @param n
     *            the node that the pointing token is searched for
     * @return the token
     */
    private Token tokenOnNode(List<Token> candidateTokens, Node n) {

        Token result = null;
        for (Token token : candidateTokens) {
            if (token.getCurrentNode() == n) {
                result = token;
                break;
            }
        }
        return result;
    }
}
