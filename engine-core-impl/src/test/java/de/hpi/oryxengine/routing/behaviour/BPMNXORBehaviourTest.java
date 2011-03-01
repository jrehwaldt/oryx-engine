package de.hpi.oryxengine.routing.behaviour;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

import java.util.HashMap;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.Activity;

import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.ConditionImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.routing.behaviour.impl.XORBehaviour;

/**
 * The test of the TakeAllBehaviour-activity.
 */
public class BPMNXORBehaviourTest {

    /** The routing behavior. */
    private RoutingBehaviour behaviour;

    /** The process instance. */
    private ProcessInstance instance;

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
        behaviour.execute(instance);
        assertEquals(instance.getChildInstances().size(), 0);
    }

    /**
     * Test the true next destination node.
     * 
     */
    @Test
    public void testTrueNextNode() {

        Node node = instance.getCurrentNode();
        Node nextNode = node.getTransitions().get(1).getDestination();

        behaviour.execute(instance);

        assertEquals(instance.getCurrentNode(), nextNode);
    }

    /**
     * Test true condition.
     */
    @Test
    public void testTrueConditionNode(){
        instance.setVariable("a", 1);
        Node node = instance.getCurrentNode();
        Node nextNode = node.getTransitions().get(0).getDestination();

        behaviour.execute(instance);

        assertEquals(instance.getCurrentNode(), nextNode);
    }
    
    /**
     * Test false destination node.
     */
    @Test
    public void testFalseDestinationNode() {
        
        Node node = instance.getCurrentNode();
        Node nextNode = node.getTransitions().get(0).getDestination();
        
        behaviour.execute(instance);
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
    private ProcessInstanceImpl simpleInstance() {

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("a", 1);

        Activity activity = mock(Activity.class);
        behaviour = new XORBehaviour();
        Condition c = new ConditionImpl(map);

        NodeImpl node = new NodeImpl(activity, behaviour);
        node.setId("1");
        NodeImpl node2 = new NodeImpl(activity, behaviour);
        node2.setId("2");
        NodeImpl node3 = new NodeImpl(activity, behaviour);
        node2.setId("3");
        node.transitionToWithCondition(node2, c);
        node.transitionTo(node3);



        return new ProcessInstanceImpl(node);
    }
}
