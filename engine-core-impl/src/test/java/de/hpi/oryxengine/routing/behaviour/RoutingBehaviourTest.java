package de.hpi.oryxengine.routing.behaviour;

import static org.testng.Assert.*;

import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.factory.RoutingBehaviourTestFactory;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;

/**
 * The test of the routing behavior.
 */
public class RoutingBehaviourTest {

    /** The process instance. */
    private ProcessInstance instance;

    /**
     * Set up. An instance is build.
     */
    @BeforeClass
    public void setUp() {

        instance = simpleInstance();

    }

    /**
     * Test class. A routing from the current node to the next node is done. The instance's current node should now be
     * this next node.
     */
    @Test
    public void testClass() {

        Node node = instance.getCurrentNode();
        Node nextNode = node.getTransitions().get(0).getDestination();

        IncomingBehaviour incomingBehaviour = node.getIncomingBehaviour();
        OutgoingBehaviour outgoingBehaviour = node.getOutgoingBehaviour();
        
        List<ProcessInstance> joinedInstances = incomingBehaviour.join(instance);
        
        outgoingBehaviour.split(joinedInstances);

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
    private ProcessInstanceImpl simpleInstance() {

        RoutingBehaviourTestFactory factory = new RoutingBehaviourTestFactory();

        Node node = factory.createWithAndSplit();
        Node node2 = factory.createWithAndSplit();
        node.transitionTo(node2);

        return new ProcessInstanceImpl(node);
    }
}
