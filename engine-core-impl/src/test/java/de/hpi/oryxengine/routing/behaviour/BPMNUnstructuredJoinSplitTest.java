package de.hpi.oryxengine.routing.behaviour;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.factory.node.RoutingBehaviourTestFactory;
import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.process.definition.NodeParameter;
import de.hpi.oryxengine.process.definition.NodeParameterImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilder;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.AndJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

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
    private List<Node> workQueue = null;

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
            assertEquals(newTokens.size(), 3,
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
     * 
     * ->N1->->-> S->N2->IJ->OJ->E ->N3->
     * 
     * @return the list
     */
    private Token initializeToken() {

        ProcessBuilder builder = new ProcessBuilderImpl();
        NodeParameter param = new NodeParameterImpl(NullActivity.class, new SimpleJoinBehaviour(),
            new TakeAllSplitBehaviour());

        Node splitNode = builder.createNode(param);
        node1 = builder.createNode(param);
        node2 = builder.createNode(param);
        node3 = builder.createNode(param);

        param.setIncomingBehaviour(new AndJoinBehaviour());
        innerJoinNode = builder.createNode(param);
        outerJoinNode = builder.createNode(param);

        param.setIncomingBehaviour(new SimpleJoinBehaviour());
        endNode = new RoutingBehaviourTestFactory().createWithAndSplit();

        builder.createTransition(splitNode, node1).createTransition(splitNode, node2)
        .createTransition(splitNode, node3).createTransition(node1, innerJoinNode)
        .createTransition(node2, innerJoinNode).createTransition(innerJoinNode, outerJoinNode)
        .createTransition(node3, outerJoinNode).createTransition(outerJoinNode, endNode);

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
