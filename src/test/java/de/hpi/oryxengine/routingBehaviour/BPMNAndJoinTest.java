package de.hpi.oryxengine.routingBehaviour;

import static org.mockito.Mockito.mock;

import java.util.List;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processInstanceImpl.ProcessInstanceImpl;
import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.impl.NodeImpl;
import de.hpi.oryxengine.routingBehaviour.impl.BPMNAndJoinBehaviour;
import de.hpi.oryxengine.routingBehaviour.impl.BPMNTakeAllBehaviour;

public class BPMNAndJoinTest {

    Node node1, node2, joinNode, splitNode, node3;
    RoutingBehaviour behaviour, joinBehaviour;
    ProcessInstance instance, childInstance1, childInstance2;

    @BeforeMethod
    public void setUp() {

        instance = initializeInstances();
        List<ProcessInstance> children = instance.getChildInstances();
        childInstance1 = children.get(0);
        childInstance2 = children.get(1);
    }

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

    // TODO: Test with nested and splits and joins to simulate the situation that there are grandparents, etc.

    private ProcessInstance initializeInstances() {

        Activity activity = mock(Activity.class);
        splitNode = mock(Node.class);
        behaviour = new BPMNTakeAllBehaviour();
        joinBehaviour = new BPMNAndJoinBehaviour();

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
