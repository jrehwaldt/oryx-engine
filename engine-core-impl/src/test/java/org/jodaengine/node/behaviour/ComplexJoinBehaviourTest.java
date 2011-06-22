package org.jodaengine.node.behaviour;

import static org.mockito.Mockito.mock;

import java.util.List;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.navigator.NavigatorImplMock;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.incomingbehaviour.ComplexJoinBehaviour;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.ComplexGatewayState;
import org.jodaengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeBuilder;
import org.jodaengine.process.structure.NodeBuilderImpl;
import org.jodaengine.process.token.BpmnToken;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.builder.BpmnTokenBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the complex join behaviour.
 */
public class ComplexJoinBehaviourTest {

    private Node beforeNode1, beforeNode2, joinNode, nextNode;
    private ControlFlow incomingControlFlow1, incomingControlFlow2;

    private NavigatorImplMock nav;
    private Token token;
    private ProcessInstance instance;

    /**
     * Creates a complex gateway node with two incoming {@link ControlFlow} and one following node.
     */
    @BeforeClass
    public void setUpStructure() {

        NodeBuilder builder = new NodeBuilderImpl();
        builder.setIncomingBehaviour(new ComplexJoinBehaviour(1));
        builder.setOutgoingBehaviour(new TakeAllSplitBehaviour());
        builder.setActivityBehavior(mock(Activity.class));
        joinNode = builder.buildNode();

        builder.setIncomingBehaviour(new SimpleJoinBehaviour());
        nextNode = builder.buildNode();

        builder.setOutgoingBehaviour(new TakeAllSplitBehaviour());
        beforeNode1 = builder.buildNode();
        beforeNode2 = builder.buildNode();

        incomingControlFlow1 = beforeNode1.controlFlowTo(joinNode);
        incomingControlFlow2 = beforeNode2.controlFlowTo(joinNode);
        joinNode.controlFlowTo(nextNode);

    }

    /**
     * Creates a token on the join node.
     */
    @BeforeMethod
    public void createTokenOnJoinNode() {

        nav = new NavigatorImplMock();
        instance = new ProcessInstance(mock(ProcessDefinition.class), new BpmnTokenBuilder(nav, null));
        token = new BpmnToken(joinNode, instance, nav);
    }

    /**
     * Creates a token + process instance + context and executes the joinNode-Behaviour without having set any Gateway
     * state or incoming {@link ControlFlow}s in the context.
     * 
     * @throws JodaEngineException
     *             the joda engine exception
     */
    @Test
    public void testGatewayTriggering()
    throws JodaEngineException {

        token.setLastTakenControlFlow(incomingControlFlow1);
        token.executeStep();

        List<Token> createdTokens = nav.getWorkQueue();
        Assert.assertEquals(createdTokens.size(), 1, "There should be one created token");

        Token newToken = createdTokens.get(0);
        Assert.assertEquals(newToken.getCurrentNode(), nextNode);

        ProcessInstanceContext context = instance.getContext();
        Assert.assertEquals(context.getSignaledControlFlow(joinNode).size(), 1,
            "There should still one {@link ControlFlow} marked as waiting for the joinNode, as it is not reset yet.");

    }

    /**
     * Let the gateway fire once and then have another token come to the gateway on the same {@link ControlFlow}. The gateway
     * should not fire again.
     * 
     * @throws JodaEngineException
     *             the joda engine exception
     */
    @Test
    public void testGatewayReset()
    throws JodaEngineException {

        token.setLastTakenControlFlow(incomingControlFlow1);
        token.executeStep();

        // remove the created token from the work queue as it is not of interest here.
        nav.flushWorkQueue();

        Token anotherToken = new BpmnToken(joinNode, instance, nav);
        anotherToken.setLastTakenControlFlow(incomingControlFlow1);
        anotherToken.executeStep();

        List<Token> newTokens = nav.getWorkQueue();
        Assert.assertEquals(newTokens.size(), 0,
            "There should be no new tokens, the joinBehaviour should not have triggered.");

        ProcessInstanceContext context = instance.getContext();
        Assert.assertEquals(context.getSignaledControlFlow(joinNode).size(), 2,
            "There should two waiting incoming {@link ControlFlow}s, as the gateway has not reset yet.");

        // the next token should not trigger the outgoing behaviour, but reset the complex join behaviour and thus
        // remove the waiting {@link ControlFlow}s
        anotherToken = new BpmnToken(joinNode, instance, nav);
        anotherToken.setLastTakenControlFlow(incomingControlFlow2);
        anotherToken.executeStep();

        Assert.assertEquals(context.getSignaledControlFlow(joinNode).size(), 1,
            "The reset should have removed both {@link ControlFlow}s from the waiting {@link ControlFlow}s, ");

    }

    /**
     * Tests the following case: {@link ControlFlow}1 is signaled, gateway triggers. {@link ControlFlow}1 is signaled again, gateway does
     * not trigger. {@link ControlFlow}2 is signaled. gateway should reset itself AND trigger again, as the triggering conditions
     * is already fulfilled.
     * 
     * @throws JodaEngineException
     */
    @Test
    public void testMultipleStartResetCycle()
    throws JodaEngineException {

        setComplexGatewayState(ComplexGatewayState.WAITING_FOR_RESET);

        // {@link ControlFlow} has been signaled twice.
        instance.getContext().setSignaledControlFlow(incomingControlFlow1);
        instance.getContext().setSignaledControlFlow(incomingControlFlow1);

        // now the discriminator should reset and trigger, as the triggering condition is already met.
        token.setLastTakenControlFlow(incomingControlFlow2);
        token.executeStep();

        List<Token> newTokens = nav.getWorkQueue();
        Assert.assertEquals(newTokens.size(), 1,
            "There should be one new token, that has been created as the joinBehaviour just triggered another time.");

        ProcessInstanceContext context = instance.getContext();
        Assert.assertEquals(context.getSignaledControlFlow(joinNode).size(), 1,
            "There should be the one waiting execution");

        Assert.assertEquals(getComplexGatewayState(), ComplexGatewayState.WAITING_FOR_RESET,
            "The complex gateway should, again, be waiting for the reset operation.");
    }

    /**
     * Gets the state of the complex gateway.
     * 
     * @return the complex gateway state
     */
    private ComplexGatewayState getComplexGatewayState() {

        return (ComplexGatewayState) instance.getContext().getNodeVariable(joinNode,
            ComplexJoinBehaviour.STATE_VARIABLE_NAME);
    }

    /**
     * Sets the state of the complex gateway. 
     * 
     * @param state
     *            the new complex gateway state
     */
    private void setComplexGatewayState(ComplexGatewayState state) {

        instance.getContext().setNodeVariable(joinNode, ComplexJoinBehaviour.STATE_VARIABLE_NAME, state);
    }
}
