package de.hpi.oryxengine.routing.behaviour;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.*;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.routing.behaviour.RoutingBehaviour;
import de.hpi.oryxengine.routing.behaviour.impl.TakeAllBehaviour;

/**
 * The test of the routing behavior.
 */
public class RoutingBehaviourTest {

    /** The routing behavior. */
    private RoutingBehaviour behaviour;

    /** The process instance. */
    private ProcessInstance instance;

    /**
     * Set up.
     * An instance is build.
     */
    @BeforeClass
    public void setUp() {

        instance = simpleInstance();

    }

    /**
     * Test class.
     * A routing from the current node to the next node is done.
     * The instance's current node should now be this next node.
     */
    @Test
    public void testClass() {

        Node node = instance.getCurrentNode();
        Node nextNode = node.getTransitions().get(0).getDestination();

        behaviour.execute(instance);

        assertEquals(instance.getCurrentNode(), nextNode);
    }

    /**
     * Tear down.
     */
    @AfterClass
    public void tearDown() {

    }

    /**
     * Simple instance.
     * An activity is set up, it gets a behavior and a transition to a second node.
     * 
     * @return the process instance that was created within the method
     */
    private ProcessInstanceImpl simpleInstance() {

        Activity activity = mock(Activity.class);
        behaviour = new TakeAllBehaviour();

        NodeImpl node = new NodeImpl(activity, behaviour);
        node.setId("1");
        NodeImpl node2 = new NodeImpl(activity, behaviour);
        node2.setId("2");
        node.transitionTo(node2);

        return new ProcessInstanceImpl(node);
    }
}
