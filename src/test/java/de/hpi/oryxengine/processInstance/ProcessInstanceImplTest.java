package de.hpi.oryxengine.processInstance;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.processInstanceImpl.ProcessInstanceImpl;
import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.impl.NodeImpl;
import de.hpi.oryxengine.routingBehaviour.impl.BPMNTakeAllBehaviour;

public class ProcessInstanceImplTest {

    ProcessInstance instance;
    NodeImpl node, node2, node3;

    @BeforeClass
    public void setUp() {

        instance = simpleInstance();
    }

    @Test
    public void testTakeAllTransitions() {

        List<ProcessInstance> newInstances = instance.takeAllTransitions();
        assertEquals(newInstances.size(), 2, "You should have two new process instances");

        for (ProcessInstance newInstance : newInstances) {
            assertEquals(newInstance.getParentInstance(), instance, "The new instances "
                + "should have the instance that reached the split node as a parent");
        }

        Node[] currentNodes = new Node[2];
        for (int i = 0; i < 2; i++) {
            currentNodes[i] = newInstances.get(i).getCurrentNode();
        }

        Node[] expectedCurrentNodes = { node2, node3 };
        assertEqualsNoOrder(currentNodes, expectedCurrentNodes, "The new instances should point to the following nodes");
    }

    private ProcessInstanceImpl simpleInstance() {

        Activity activity = mock(Activity.class);

        node = new NodeImpl(activity);
        node.setId("1");
        node2 = new NodeImpl(activity);
        node2.setId("2");
        node3 = new NodeImpl(activity);
        node3.setId("3");
        node.transitionTo(node2);
        node.transitionTo(node3);

        ArrayList<Node> startNodes = new ArrayList<Node>();
        startNodes.add(node);
        return new ProcessInstanceImpl(startNodes);
    }
}
