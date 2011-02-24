package de.hpi.oryxengine.routingBehaviour;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processInstance.ProcessInstanceImpl;
import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.NodeImpl;
import de.hpi.oryxengine.routingBehaviour.impl.TakeAllBehaviour;

/**
 * The test of the TakeAllBehaviour-activity.
 */
public class BPMNTakeAllBehaviourTest {

    /** The routing behavior. */
    private RoutingBehaviour behaviour;

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
     * Test class.
     * 
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
     * Simple instance. An activity is set up, it gets a behavior and a transition to a second node.
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
