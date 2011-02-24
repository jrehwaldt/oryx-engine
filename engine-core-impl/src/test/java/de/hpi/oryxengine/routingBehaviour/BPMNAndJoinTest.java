package de.hpi.oryxengine.routingBehaviour;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processInstance.ProcessInstanceImpl;
import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.NodeImpl;
import de.hpi.oryxengine.routingBehaviour.impl.AndJoinGatewayBehaviour;
import de.hpi.oryxengine.routingBehaviour.impl.TakeAllBehaviour;
import de.hpi.oryxengine.routingBehaviour.joinBehaviour.impl.AndJoinBehaviour;

/**
 * This class tests the BPMNAndJoin-Class.
 */
public class BPMNAndJoinTest {

    /** The node3. */
    private Node node1, node2, joinNode, splitNode, node3;

    /** The join behaviour. */
    private RoutingBehaviour behaviour, joinBehaviour;

    /** The child instance2. */
    private ProcessInstance instance, childInstance1, childInstance2;

    /**
     * Sets the up.
     */
    @BeforeMethod
    public void setUp() {

        instance = initializeInstances();
        List<ProcessInstance> children = instance.getChildInstances();
        childInstance1 = children.get(0);
        childInstance2 = children.get(1);
    }

    /**
     * Test single instance reached join.
     */
    @Test
    public void testSingleInstanceReachedJoin() {

        childInstance1.setCurrentNode(joinNode);
        RoutingBehaviour behaviour = childInstance1.getCurrentNode().getRoutingBehaviour();
        List<ProcessInstance> newInstances = behaviour.execute(childInstance1);

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
        RoutingBehaviour behaviour = childInstance1.getCurrentNode().getRoutingBehaviour();
        List<ProcessInstance> newInstances = behaviour.execute(childInstance1);

        assertEquals(newInstances.size(), 1, "There should only be one new instance");

        ProcessInstance newInstance = newInstances.get(0);
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
    private ProcessInstance initializeInstances() {

        Activity activity = mock(Activity.class);
        splitNode = mock(Node.class);
        behaviour = new TakeAllBehaviour();
        joinBehaviour = new AndJoinGatewayBehaviour();

        node1 = new NodeImpl(activity, behaviour);
        node1.setId("1");
        node2 = new NodeImpl(activity, behaviour);
        node2.setId("2");
        joinNode = new NodeImpl(activity, joinBehaviour);
        node1.transitionTo(joinNode);
        node2.transitionTo(joinNode);

        node3 = new NodeImpl(activity, behaviour);
        node3.setId("3");
        joinNode.transitionTo(node3);

        ProcessInstance instance = new ProcessInstanceImpl(splitNode);
        instance.createChildInstance(node1);
        instance.createChildInstance(node2);
        return instance;
    }
}
