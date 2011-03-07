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
    private Token instance, childInstance1, childInstance2;

    /**
     * Sets the up.
     */
    @BeforeMethod
    public void setUp() {

        instance = initializeInstances();
        List<Token> children = instance.getChildTokens();
        childInstance1 = children.get(0);
        childInstance2 = children.get(1);
    }

    /**
     * Test single instance reached join.
     */
    @Test
    public void testSingleInstanceReachedJoin() {

        childInstance1.setCurrentNode(joinNode);
        List<Token> newInstances = executeSplitAndJoin(childInstance1);

        assertEquals(newInstances.size(), 0,
            "If only one child has reached the And Join, no new instances should be scheduled");
        assertEquals(childInstance1.getCurrentNode(), joinNode,
            "If only one child has reached the And Join, nothing should happen");
        assertEquals(instance.getCurrentNode(), splitNode,
            "If only one child has reached the And Join, the parent instance should not move on");
    }

    /**
     * Test all instances reached join.
     */
    @Test
    public void testAllInstancesReachedJoin() {

        childInstance1.setCurrentNode(joinNode);
        childInstance2.setCurrentNode(joinNode);
        List<Token> newInstances = executeSplitAndJoin(childInstance1);
        assertEquals(newInstances.size(), 1, "There should only be one new instance");

        Token newInstance = newInstances.get(0);
        assertEquals(newInstance, instance,
            "The new instance should be the parent of the instance that executes the join behaviour");
        assertEquals(instance.getCurrentNode(), node3,
            "If only one of the children has reached the And Join, the parent instance should not move on");
    }

    // TODO Test with nested and splits and joins to simulate the situation that there are grandparents, etc.

    /**
     * Initialize instances.
     * 
     * @return the process instance
     */
    private Token initializeInstances() {

        splitNode = mock(Node.class);
        node1 = new RoutingBehaviourTestFactory().createWithAndSplit();
        node2 = new RoutingBehaviourTestFactory().createWithAndSplit();
        joinNode = new RoutingBehaviourTestFactory().createWithAndJoin();
        node1.transitionTo(joinNode);
        node2.transitionTo(joinNode);

        node3 = new RoutingBehaviourTestFactory().createWithAndSplit();
        joinNode.transitionTo(node3);

        instance = new TokenImpl(splitNode);
        instance.createChildToken(node1);
        instance.createChildToken(node2);
        return instance;
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
