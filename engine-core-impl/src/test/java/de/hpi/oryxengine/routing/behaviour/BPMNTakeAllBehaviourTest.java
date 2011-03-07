package de.hpi.oryxengine.routing.behaviour;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.factory.RoutingBehaviourTestFactory;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;

/**
 * The test of the TakeAllBehaviour-activity.
 */
public class BPMNTakeAllBehaviourTest {

    /** The process token. */
    private Token token;

    /**
     * Set up. A token is built.
     */
    @BeforeClass
    public void setUp() {

        token = simpleToken();
    }

    /**
     * Test class.
     * 
     */
    @Test
    public void testClass() {

        Node node = token.getCurrentNode();
        Node nextNode = node.getTransitions().get(0).getDestination();

        executeSplitAndJoin(token);

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
     * @return the process instance that was created within the method
     */
    private TokenImpl simpleToken() {


        Node node = new RoutingBehaviourTestFactory().createWithAndSplit();
        Node node2 = new RoutingBehaviourTestFactory().createWithAndSplit();
        node.transitionTo(node2);

        return new TokenImpl(node);
    }
    
    /**
     * Execute split and join.
     *
     * @param token the token
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
