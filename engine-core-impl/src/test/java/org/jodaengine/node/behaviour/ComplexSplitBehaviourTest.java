package org.jodaengine.node.behaviour;

import static org.mockito.Mockito.mock;

import java.util.List;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.navigator.NavigatorImplMock;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.ComplexSplitBehaviour;
import org.jodaengine.node.outgoingbehaviour.OutgoingBehaviour;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeBuilder;
import org.jodaengine.process.structure.NodeBuilderImpl;
import org.jodaengine.process.structure.condition.JuelExpressionCondition;
import org.jodaengine.process.token.BpmnToken;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.builder.BpmnTokenBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the inclusive split behaviour.
 */
public class ComplexSplitBehaviourTest {

    private Node splitNode, nextNode1, nextNode2, nextNode3;

    /**
     * Creates a node with complex gateway split behaviour and sequence flow to three nodes annotated with conditions.
     */
    @BeforeMethod
    public void setUp() {

        NodeBuilder builder = new NodeBuilderImpl();
        builder.setIncomingBehaviour(new SimpleJoinBehaviour());
        builder.setOutgoingBehaviour(new ComplexSplitBehaviour());
        builder.setActivityBehavior(mock(Activity.class));

        splitNode = builder.buildNode();

        builder.setOutgoingBehaviour(mock(OutgoingBehaviour.class));

        nextNode1 = builder.buildNode();
        nextNode2 = builder.buildNode();
        nextNode3 = builder.buildNode();

        splitNode.transitionToWithCondition(nextNode1, new JuelExpressionCondition("#{variable > 1}"));
        splitNode.transitionToWithCondition(nextNode2, new JuelExpressionCondition("#{variable > 2}"));
        splitNode.transitionToWithCondition(nextNode3, new JuelExpressionCondition("#{variable > 3}"));
    }

    /**
     * Tests the inclusive split behaviour, as specified in the BPMN 2.0 spec.
     *
     * @throws JodaEngineException thrown during execution
     */
    @Test
    public void testInclusiveSplitBehaviour()
    throws JodaEngineException {

        NavigatorImplMock nav = new NavigatorImplMock();
        ProcessInstance instance = new ProcessInstance(mock(ProcessDefinition.class), new BpmnTokenBuilder(nav, null,
            null));

        Token token = new BpmnToken(splitNode, instance, nav);
        instance.getContext().setVariable("variable", 3);

        // two conditions should evaluate to true, so we expect two newly created tokens on nextNode1 and nextNode2.
        token.executeStep();

        List<Token> newTokens = nav.getWorkQueue();
        Assert.assertEquals(newTokens.size(), 2, "There should be two new tokens");

        Token newToken1 = newTokens.get(0);
        Token newToken2 = newTokens.get(1);

        // we do not know, which token is placed on which node so we check both possibilities.
        if (newToken1.getCurrentNode() == nextNode1) {
            // although this is guaranteed by the if-clause, we add an assert for clarification.
            Assert.assertEquals(newToken1.getCurrentNode(), nextNode1, "The token should be on nextNode1");
            Assert.assertEquals(newToken2.getCurrentNode(), nextNode2, "The other token should be on nextNode2 then");
        } else {
            Assert.assertEquals(newToken1.getCurrentNode(), nextNode2, "The token should be on nextNode2");
            Assert.assertEquals(newToken2.getCurrentNode(), nextNode1, "The other token should be on nextNode1 then");
        }

    }
}
