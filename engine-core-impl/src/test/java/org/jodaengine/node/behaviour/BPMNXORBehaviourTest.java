package org.jodaengine.node.behaviour;

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;

import org.jodaengine.node.activity.NullActivity;
import org.jodaengine.node.incomingbehaviour.IncomingBehaviour;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.OutgoingBehaviour;
import org.jodaengine.node.outgoingbehaviour.XORSplitBehaviour;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.structure.Condition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.condition.HashMapCondition;
import org.jodaengine.process.token.BPMNToken;
import org.jodaengine.process.token.BPMNTokenImpl;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * The test of the TakeAllBehaviour-activity.
 */
public class BPMNXORBehaviourTest {

    /** The process instance. */
    private BPMNToken bPMNToken;

    /**
     * Set up. An instance is build.
     */
    @BeforeMethod
    public void setUp() {
        bPMNToken = simpleToken();
    }

    /**
     * Test the true next destination node.
     * 
     */
    @Test
    public void testTrueNextNode() {

        Node<BPMNToken> node = bPMNToken.getCurrentNode();
        Node<BPMNToken> nextNode = node.getOutgoingTransitions().get(1).getDestination();

        try {
            executeSplitAndJoin(bPMNToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(bPMNToken.getCurrentNode(), nextNode);
    }

    /**
     * Test true condition.
     */
    @Test
    public void testTrueConditionNode() {
        ProcessInstanceContext context = bPMNToken.getInstance().getContext();
        context.setVariable("a", 1);
        Node<BPMNToken> node = bPMNToken.getCurrentNode();
        Node<BPMNToken> nextNode = node.getOutgoingTransitions().get(0).getDestination();

        try {
            executeSplitAndJoin(bPMNToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(bPMNToken.getCurrentNode(), nextNode);
    }
    
    /**
     * Test false destination node.
     */
    @Test
    public void testFalseDestinationNode() {
        
        Node<BPMNToken> node = bPMNToken.getCurrentNode();
        Node<BPMNToken> nextNode = node.getOutgoingTransitions().get(0).getDestination();
        
        try {
            executeSplitAndJoin(bPMNToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert bPMNToken.getCurrentNode() != nextNode;
    }

    /**
     * Tear down.
     */
    @AfterMethod
    public void tearDown() {
        bPMNToken = null;
    }

    /**
     * Simple token. An activity is set up, it gets a behavior and a transition to a second and third node.
     * The first transition gets a false condition, which has to be not taken.
     * 
     * @return the process token that was created within the method
     */
    private BPMNTokenImpl simpleToken() {


        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();
        
        Node<BPMNToken> node = builder.getNodeBuilder().setIncomingBehaviour(new SimpleJoinBehaviour())
        .setOutgoingBehaviour(new XORSplitBehaviour()).setActivityBehavior(new NullActivity()).buildNode();
        
        Node<BPMNToken> node2 = builder.getNodeBuilder().setIncomingBehaviour(new SimpleJoinBehaviour())
        .setOutgoingBehaviour(new XORSplitBehaviour()).setActivityBehavior(new NullActivity()).buildNode();
        
        Node<BPMNToken> node3 = builder.getNodeBuilder().setIncomingBehaviour(new SimpleJoinBehaviour())
        .setOutgoingBehaviour(new XORSplitBehaviour()).setActivityBehavior(new NullActivity()).buildNode();
        
        
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("a", 1);
        Condition c = new HashMapCondition(map, "==");

        builder.getTransitionBuilder().transitionGoesFromTo(node, node2).setCondition(c).buildTransition();
        builder.getTransitionBuilder().transitionGoesFromTo(node, node3).buildTransition();

        return new BPMNTokenImpl(node);
    }
    
    /**
     * Execute split and join.
     *
     * @param bPMNToken the processToken
     * @return the list
     * @throws Exception the exception
     */
    private List<BPMNToken> executeSplitAndJoin(BPMNToken bPMNToken) throws Exception {
        Node<BPMNToken> node = bPMNToken.getCurrentNode();
        IncomingBehaviour<BPMNToken> incomingBehaviour = node.getIncomingBehaviour();
        OutgoingBehaviour outgoingBehaviour = node.getOutgoingBehaviour();
        
        List<BPMNToken> joinedInstances = incomingBehaviour.join(bPMNToken);
        
        return outgoingBehaviour.split(joinedInstances);
    }
}
