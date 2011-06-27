package org.jodaengine.node.behaviour;

import static org.testng.Assert.assertEquals;

import java.util.Collection;
import java.util.HashMap;

import org.jodaengine.node.activity.NullActivity;
import org.jodaengine.node.incomingbehaviour.IncomingBehaviour;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.OutgoingBehaviour;
import org.jodaengine.node.outgoingbehaviour.XORSplitBehaviour;
import org.jodaengine.process.definition.bpmn.BpmnProcessDefinitionBuilder;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.structure.Condition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.condition.HashMapCondition;
import org.jodaengine.process.token.BpmnToken;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.builder.BpmnTokenBuilder;
import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The test of the TakeAllBehaviour-activity.
 */
public class BpmnXorBehaviourTest {

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
    public void testDefaultFlowNode() {

        Node node = token.getCurrentNode();
        Node defaultFlowNode = node.getOutgoingControlFlows().get(1).getDestination();

        try {
            executeSplitAndJoin(token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(token.getCurrentNode(), defaultFlowNode);
    }

    /**
     * Test true condition.
     */
    @Test
    public void testTrueConditionNode() {

        ProcessInstanceContext context = token.getInstance().getContext();
        context.setVariable("a", 1);
        Node node = token.getCurrentNode();
        Node nextNode = node.getOutgoingControlFlows().get(0).getDestination();

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
        Node nextNode = node.getOutgoingControlFlows().get(0).getDestination();

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
     * Simple token. An activity is set up, it gets a behavior and a {@link ControlFlow} to a second and third node.
     * The first {@link ControlFlow} gets a false condition, which has to be not taken.
     * 
     * @return the process token that was created within the method
     */
    private Token simpleToken() {

        String defaultFlowId = "id";

        BpmnProcessDefinitionBuilder builder = BpmnProcessDefinitionBuilder.newBuilder();

        Node node = builder.getNodeBuilder().setIncomingBehaviour(new SimpleJoinBehaviour())
        .setOutgoingBehaviour(new XORSplitBehaviour(defaultFlowId)).setActivityBehavior(new NullActivity()).buildNode();

        Node node2 = builder.getNodeBuilder().setIncomingBehaviour(new SimpleJoinBehaviour())
        .setOutgoingBehaviour(new XORSplitBehaviour()).setActivityBehavior(new NullActivity()).buildNode();

        Node node3 = builder.getNodeBuilder().setIncomingBehaviour(new SimpleJoinBehaviour())
        .setOutgoingBehaviour(new XORSplitBehaviour()).setActivityBehavior(new NullActivity()).buildNode();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("a", 1);
        Condition c = new HashMapCondition(map, "==");

        builder.getControlFlowBuilder().controlFlowGoesFromTo(node, node2).setCondition(c).buildControlFlow();
        
        builder.getControlFlowBuilder().controlFlowGoesFromTo(node, node3).setId(defaultFlowId)
        .buildControlFlow();

        return new BpmnToken(node, new ProcessInstance(null, Mockito.mock(BpmnTokenBuilder.class)), null);
    }

    /**
     * Execute split and join.
     * 
     * @param token
     *            the processToken
     * @return the list
     * @throws Exception
     *             the exception
     */
    private Collection<Token> executeSplitAndJoin(Token token) throws Exception {
        Node node = token.getCurrentNode();
        IncomingBehaviour incomingBehaviour = node.getIncomingBehaviour();
        OutgoingBehaviour outgoingBehaviour = node.getOutgoingBehaviour();

        Collection<Token> joinedInstances = incomingBehaviour.join(token);

        return outgoingBehaviour.split(joinedInstances);
    }
}
