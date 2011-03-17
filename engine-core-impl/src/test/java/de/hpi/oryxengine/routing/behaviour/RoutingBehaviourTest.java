package de.hpi.oryxengine.routing.behaviour;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.*;

import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.factory.node.RoutingBehaviourTestFactory;
import de.hpi.oryxengine.process.definition.NodeParameter;
import de.hpi.oryxengine.process.definition.NodeParameterImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilder;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The test of the routing behavior.
 */
public class RoutingBehaviourTest {

    /** The process token. */
    private Token token;

    /**
     * Set up. An instance is build.
     */
    @BeforeClass
    public void setUp() {

        token = simpleToken();

    }

    /**
     * Test class. A routing from the current node to the next node is done. The instance's current node should now be
     * this next node.
     */
    @Test
    public void testClass() {

        Node node = token.getCurrentNode();
        Node nextNode = node.getOutgoingTransitions().get(0).getDestination();

        IncomingBehaviour incomingBehaviour = node.getIncomingBehaviour();
        OutgoingBehaviour outgoingBehaviour = node.getOutgoingBehaviour();
        
        List<Token> joinedTokens = incomingBehaviour.join(token);
        
        try {
            outgoingBehaviour.split(joinedTokens);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(token.getCurrentNode(), nextNode);
    }

    /**
     * Tear down.
     */
    @AfterClass
    public void tearDown() {

    }

    /**
     * Simple token. An activity is set up, it gets a behavior and a transition to a second node.
     * 
     * @return the process token that was created within the method
     */
    private TokenImpl simpleToken() {

        ProcessBuilder builder = new ProcessBuilderImpl();
        NodeParameter param = new NodeParameterImpl(mock(Activity.class), new SimpleJoinBehaviour(),
            new TakeAllSplitBehaviour());

        Node node = builder.createNode(param);
        Node node2 = builder.createNode(param);
        builder.createTransition(node, node2);

        return new TokenImpl(node);
    }
}
