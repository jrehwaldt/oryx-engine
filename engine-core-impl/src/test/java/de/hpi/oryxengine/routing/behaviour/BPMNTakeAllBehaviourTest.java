package de.hpi.oryxengine.routing.behaviour;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.factory.RoutingBehaviourTestFactory;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.routing.behaviour.join.JoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.split.SplitBehaviour;

// TODO: Auto-generated Javadoc
/**
 * The test of the TakeAllBehaviour-activity.
 */
public class BPMNTakeAllBehaviourTest {

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

        executeSplitAndJoin(instance);

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


        Node node = new RoutingBehaviourTestFactory().createWithAndSplit();
        node.setId("1");
        Node node2 = new RoutingBehaviourTestFactory().createWithAndSplit();
        node2.setId("2");
        node.transitionTo(node2);

        return new ProcessInstanceImpl(node);
    }
    
    /**
     * Execute split and join.
     *
     * @param instance the instance
     * @return the list
     */
    private List<ProcessInstance> executeSplitAndJoin(ProcessInstance instance) {
        Node node = instance.getCurrentNode();
        JoinBehaviour incomingBehaviour = node.getIncomingBehaviour();
        SplitBehaviour outgoingBehaviour = node.getOutgoingBehaviour();
        
        List<ProcessInstance> joinedInstances = incomingBehaviour.join(instance);
        
        return outgoingBehaviour.split(joinedInstances);
    }
    
}
