package de.hpi.oryxengine.processInstance;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.*;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.NodeImpl;
import de.hpi.oryxengine.processstructure.Transition;

/**
 * The test for the process instance.
 */
public class ProcessInstanceImplTest {

    /** The process instance. */
    private ProcessInstance instance;

    /** Different Nodes. */
    private NodeImpl node, node2, node3;

    /** The transition to be taken. */
    private Transition transitionToTake;

    /**
     * Set up.     
     * An instance is build.
     */
    @BeforeTest
    public void setUp() {

        instance = simpleInstance();
    }

    /**
     * Test for taking all transitions.
     * TODO see if description is correct
     * Two new instances shall be ready for execution if the parent instance goes along all edges.
     * Both of them should have the same parent instance.
     * The new instances should then point to the succeeding nodes of the parent instances' node.
     */
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

        Node[] expectedCurrentNodes = {node2, node3};
        assertEqualsNoOrder(currentNodes, expectedCurrentNodes,
            "The new instances should point to the following nodes.");
    }

    /**
     * Test take single transition.
     * TODO JavaDoc for the test
     */
    @Test
    public void testTakeSingleTransition() {

        List<ProcessInstance> newInstances = instance.takeSingleTransition(transitionToTake);
        assertEquals(newInstances.size(), 1, "You should have a single process instance.");

        ProcessInstance newInstance = newInstances.get(0);
        assertEquals(newInstance, instance,
            "The instance should be the same, no child instance or something like that.");
        assertEquals(newInstance.getCurrentNode(), node2, "The instance should have moved on.");
    }

    /**
     * Simple instance.
     * TODO more JavaDoc description
     * 
     * @return the process instance impl
     */
    private ProcessInstanceImpl simpleInstance() {

        Activity activity = mock(Activity.class);

        node = new NodeImpl(activity);
        node.setId("1");
        node2 = new NodeImpl(activity);
        node2.setId("2");
        node3 = new NodeImpl(activity);
        node3.setId("3");
        node.transitionTo(node2);

        transitionToTake = node.getTransitions().get(0);

        node.transitionTo(node3);

        return new ProcessInstanceImpl(node);
    }
}
