package org.jodaengine.node.behaviour;

import static org.mockito.Mockito.mock;

import java.util.List;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.navigator.NavigatorImplMock;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.incomingbehaviour.ComplexJoinBehaviour;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.instance.ProcessInstanceContextImpl;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeBuilder;
import org.jodaengine.process.structure.NodeBuilderImpl;
import org.jodaengine.process.structure.Transition;
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
    private Transition incomingTransition1, incomingTransition2;

    private NavigatorImplMock nav;
    private Token token;
    private ProcessInstance instance;

    /**
     * Creates a complex gateway node with two incoming transition and one following node.
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

        incomingTransition1 = beforeNode1.transitionTo(joinNode);
        incomingTransition2 = beforeNode2.transitionTo(joinNode);
        joinNode.transitionTo(nextNode);

    }

    /**
     * Creates a token on the join node.
     */
    @BeforeMethod
    public void createTokenOnJoinNode() {

        nav = new NavigatorImplMock();
        instance = new ProcessInstance(mock(ProcessDefinition.class), new BpmnTokenBuilder(nav, null, null));
        token = new BpmnToken(joinNode, instance, nav);
    }

    /**
     * Creates a token + process instance + context and executes the joinNode-Behaviour without having set any Gateway
     * state or incoming transitions in the context.
     * 
     * @throws JodaEngineException
     *             the joda engine exception
     */
    @Test
    public void testGatewayTriggering()
    throws JodaEngineException {

        token.setLastTakenTransition(incomingTransition1);
        token.executeStep();

        List<Token> createdTokens = nav.getWorkQueue();
        Assert.assertEquals(createdTokens.size(), 1, "There should be one created token");

        Token newToken = createdTokens.get(0);
        Assert.assertEquals(newToken.getCurrentNode(), nextNode);

        ProcessInstanceContext context = instance.getContext();
        Assert.assertEquals(context.getWaitingExecutions(joinNode).size(), 1,
            "There should still one transition marked as waiting for the joinNode, as it is not reset yet.");

    }

    /**
     * Let the gateway fire once and then have another token come to the gateway on the same transition. The gateway
     * should not fire again.
     *
     * @throws JodaEngineException the joda engine exception
     */
    @Test
    public void testGatewayReset()
    throws JodaEngineException {

        token.setLastTakenTransition(incomingTransition1);
        token.executeStep();

        // remove the created token from the work queue as it is not of interest here.
        nav.flushWorkQueue();

        Token anotherToken = new BpmnToken(joinNode, instance, nav);
        anotherToken.setLastTakenTransition(incomingTransition1);
        anotherToken.executeStep();

        List<Token> newTokens = nav.getWorkQueue();
        Assert.assertEquals(newTokens.size(), 0,
            "There should be no new tokens, the joinBehaviour should not have triggered.");

        ProcessInstanceContext context = instance.getContext();
        Assert.assertEquals(context.getWaitingExecutions(joinNode).size(), 2,
            "There should two waiting incoming transitions, as the gateway has not reset yet.");

        // the next token should not trigger the outgoing behaviour, but reset the complex join behaviour and thus
        // remove the waiting transitions
        anotherToken = new BpmnToken(joinNode, instance, nav);
        anotherToken.setLastTakenTransition(incomingTransition2);
        anotherToken.executeStep();

        Assert.assertEquals(context.getWaitingExecutions(joinNode).size(), 1,
            "The reset should have removed both transitions from the waiting transitions, ");

    }

    /**
     * Tests the following case: transition1 is signaled, gateway triggers. transition1 is signaled again, gateway does
     * not trigger. transition2 is signaled. gateway should reset itself AND trigger again, as the triggering conditions
     * is already fulfilled.
     */
//    @Test
//    public void testMultipleStartResetCycle() {
//
//    }
}
