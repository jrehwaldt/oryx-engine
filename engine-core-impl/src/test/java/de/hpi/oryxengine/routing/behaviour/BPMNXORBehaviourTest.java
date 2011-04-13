package de.hpi.oryxengine.routing.behaviour;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.process.definition.NodeParameter;
import de.hpi.oryxengine.process.definition.NodeParameterImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilder;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.instance.ProcessInstanceContext;
import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.ConditionImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.XORSplitBehaviour;

/**
 * The test of the TakeAllBehaviour-activity.
 */
public class BPMNXORBehaviourTest {

    /** The process instance. */
    private Token token;

    /**
     * Set up. An instance is build.
     */
    @BeforeMethod
    public void setUp() {
        token = simpleToken();
    }

    /**
     * Test the true next destination node.
     * 
     */
    @Test
    public void testTrueNextNode() {

        Node node = token.getCurrentNode();
        Node nextNode = node.getOutgoingTransitions().get(1).getDestination();

        try {
            executeSplitAndJoin(token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(token.getCurrentNode(), nextNode);
    }

    /**
     * Test true condition.
     */
    @Test
    public void testTrueConditionNode() {
        ProcessInstanceContext context = token.getInstance().getContext();
        context.setVariable("a", 1);
        Node node = token.getCurrentNode();
        Node nextNode = node.getOutgoingTransitions().get(0).getDestination();

        try {
            executeSplitAndJoin(token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(token.getCurrentNode(), nextNode);
    }
    
    /**
     * Test false destination node.
     */
    @Test
    public void testFalseDestinationNode() {
        
        Node node = token.getCurrentNode();
        Node nextNode = node.getOutgoingTransitions().get(0).getDestination();
        
        try {
            executeSplitAndJoin(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert token.getCurrentNode() != nextNode;
    }

    /**
     * Tear down.
     */
    @AfterMethod
    public void tearDown() {
        token = null;
    }

    /**
     * Simple token. An activity is set up, it gets a behavior and a transition to a second and third node.
     * The first transition gets a false condition, which has to be not taken.
     * 
     * @return the process token that was created within the method
     */
    private TokenImpl simpleToken() {

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("a", 1);

        Condition c = new ConditionImpl(map);
        ProcessBuilder builder = new ProcessBuilderImpl();
        NodeParameter param = new NodeParameterImpl(NullActivity.class, new SimpleJoinBehaviour(),
            new XORSplitBehaviour());

        Node node = builder.createNode(param);
        Node node2 = builder.createNode(param);
        Node node3 = builder.createNode(param);
        builder.createTransition(node, node2, c).createTransition(node, node3);

        return new TokenImpl(node);
    }
    
    /**
     * Execute split and join.
     *
     * @param token the processToken
     * @return the list
     * @throws Exception the exception
     */
    private List<Token> executeSplitAndJoin(Token token) throws Exception {
        Node node = token.getCurrentNode();
        IncomingBehaviour incomingBehaviour = node.getIncomingBehaviour();
        OutgoingBehaviour outgoingBehaviour = node.getOutgoingBehaviour();
        
        List<Token> joinedInstances = incomingBehaviour.join(token);
        
        return outgoingBehaviour.split(joinedInstances);
    }
}
