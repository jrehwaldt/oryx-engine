package de.hpi.oryxengine.routing.behaviour;

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.factory.RoutingBehaviourTestFactory;

import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.ConditionImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;

/**
 * The test of the TakeAllBehaviour-activity.
 */
public class BPMNXORBehaviourTest {

    /** The process instance. */
    private Token instance;

    /**
     * Set up. An instance is build.
     */
    @BeforeMethod
    public void setUp() {
        instance = simpleInstance();
    }
    
    
    /**
     * Test count of child instances.
     */
    @Test
    public void testCountOfChildInstances() {
        executeSplitAndJoin(instance);
        assertEquals(instance.getChildTokens().size(), 0);
    }

    /**
     * Test the true next destination node.
     * 
     */
    @Test
    public void testTrueNextNode() {

        Node node = instance.getCurrentNode();
        Node nextNode = node.getTransitions().get(1).getDestination();

        executeSplitAndJoin(instance);

        assertEquals(instance.getCurrentNode(), nextNode);
    }

    /**
     * Test true condition.
     */
    @Test
    public void testTrueConditionNode() {
        instance.setVariable("a", 1);
        Node node = instance.getCurrentNode();
        Node nextNode = node.getTransitions().get(0).getDestination();

        executeSplitAndJoin(instance);

        assertEquals(instance.getCurrentNode(), nextNode);
    }
    
    /**
     * Test false destination node.
     */
    @Test
    public void testFalseDestinationNode() {
        
        Node node = instance.getCurrentNode();
        Node nextNode = node.getTransitions().get(0).getDestination();
        
        executeSplitAndJoin(instance);
        assert instance.getCurrentNode() != nextNode;
    }

    /**
     * Tear down.
     */
    @AfterMethod
    public void tearDown() {
        instance = null;
    }

    /**
     * Simple instance. An activity is set up, it gets a behavior and a transition to a second and third node.
     * The first transition gets a false condition, which has to be not taken.
     * 
     * @return the process instance that was created within the method
     */
    private TokenImpl simpleInstance() {

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("a", 1);

        Condition c = new ConditionImpl(map);
        RoutingBehaviourTestFactory factory = new RoutingBehaviourTestFactory();

        Node node = factory.createWithXORSplit();
        Node node2 = factory.createWithXORSplit();
        Node node3 = factory.createWithXORSplit();
        node.transitionToWithCondition(node2, c);
        node.transitionTo(node3);

        return new TokenImpl(node);
    }
    
    /**
     * Execute split and join.
     *
     * @param instance the instance
     */
    private void executeSplitAndJoin(Token instance) {
        Node node = instance.getCurrentNode();
        IncomingBehaviour incomingBehaviour = node.getIncomingBehaviour();
        OutgoingBehaviour outgoingBehaviour = node.getOutgoingBehaviour();
        
        List<Token> joinedInstances = incomingBehaviour.join(instance);
        
        outgoingBehaviour.split(joinedInstances);
    }
}
