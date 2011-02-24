package de.hpi.oryxengine.routingBehaviour;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.*;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processInstanceImpl.ProcessInstanceImpl;
import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.impl.NodeImpl;
import de.hpi.oryxengine.routingBehaviour.impl.BPMNTakeAllBehaviour;

public class BPMNTakeAllBehaviourTest {

    private RoutingBehaviour behaviour;
    private ProcessInstance instance;

    @BeforeClass
    public void setUp() {

        instance = simpleInstance();

    }

    @Test
    public void testClass() {

        Node node = instance.getCurrentNode();
        Node nextNode = node.getTransitions().get(0).getDestination();

        behaviour.execute(instance);

        assertEquals(instance.getCurrentNode(), nextNode);
    }

    @AfterClass
    public void tearDown() {

    }

    private ProcessInstanceImpl simpleInstance() {

        Activity activity = mock(Activity.class);
        behaviour = new BPMNTakeAllBehaviour();

        NodeImpl node = new NodeImpl(activity, behaviour);
        node.setId("1");
        NodeImpl node2 = new NodeImpl(activity, behaviour);
        node2.setId("2");
        node.transitionTo(node2);
        
        return new ProcessInstanceImpl(node);
    }
}
