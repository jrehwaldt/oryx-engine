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

    /** The process instance. */
    private Token instance;

    /**
     * Set up. An instance is build.
     */
    @BeforeClass
    public void setUp() {

        instance = simpleInstance();
    }

    /**
     * Test class.
     * 
     */
    @Test
    public void testClass() {

        Node node = instance.getCurrentNode();
        Node nextNode = node.getTransitions().get(0).getDestination();

        executeSplitAndJoin(instance);

        assertEquals(instance.getCurrentNode(), nextNode);
    }

    /**
     * Tear down.
     */
    @AfterClass
    public void tearDown() {

    }

    /**
     * Simple instance. An activity is set up, it gets a behavior and a transition to a second node.
     * 
     * @return the process instance that was created within the method
     */
    private TokenImpl simpleInstance() {


        Node node = new RoutingBehaviourTestFactory().createWithAndSplit();
        Node node2 = new RoutingBehaviourTestFactory().createWithAndSplit();
        node.transitionTo(node2);

        return new TokenImpl(node);
    }
    
    /**
     * Execute split and join.
     *
     * @param instance the instance
     * @return the list
     */
    private List<Token> executeSplitAndJoin(Token instance) {
        Node node = instance.getCurrentNode();
        IncomingBehaviour incomingBehaviour = node.getIncomingBehaviour();
        OutgoingBehaviour outgoingBehaviour = node.getOutgoingBehaviour();
        
        List<Token> joinedInstances = incomingBehaviour.join(instance);
        
        return outgoingBehaviour.split(joinedInstances);
    }
    
}
