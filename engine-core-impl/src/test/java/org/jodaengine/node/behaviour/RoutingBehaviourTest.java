package org.jodaengine.node.behaviour;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.jodaengine.node.activity.NullActivity;
import org.jodaengine.node.incomingbehaviour.IncomingBehaviour;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.OutgoingBehaviour;
import org.jodaengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BpmnToken;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.builder.BpmnTokenBuilder;
import org.mockito.Mockito;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * The test of the routing behavior.
 */
public class RoutingBehaviourTest {

    /** The process token. */
    private Token token = null;

    /**
     * Set up. An instance is build.
     */
    @BeforeClass
    public void setUp() {

        token = simpleToken();

    }

    /**
     * Test class. A routing from the current node to the next node is done. The instance's current node should now be
     * this next node.
     */
    @Test
    public void testClass() {

        Node node = token.getCurrentNode();
        Node nextNode = node.getOutgoingTransitions().get(0).getDestination();

        IncomingBehaviour incomingBehaviour = node.getIncomingBehaviour();
        OutgoingBehaviour outgoingBehaviour = node.getOutgoingBehaviour();

        List<Token> joinedTokens = incomingBehaviour.join(token);

        try {
            outgoingBehaviour.split(joinedTokens);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(token.getCurrentNode(), nextNode);
    }

    /**
     * Tear down.
     */
    @AfterClass
    public void tearDown() {

    }

    /**
     * Simple token. An activity is set up, it gets a behavior and a transition to a second node.
     * 
     * @return the process token that was created within the method
     */
    private Token simpleToken() {

        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();

        Node node = builder.getNodeBuilder().setActivityBehavior(new NullActivity())
        .setIncomingBehaviour(new SimpleJoinBehaviour()).setOutgoingBehaviour(new TakeAllSplitBehaviour()).buildNode();

        Node node2 = builder.getNodeBuilder().setActivityBehavior(new NullActivity())
        .setIncomingBehaviour(new SimpleJoinBehaviour()).setOutgoingBehaviour(new TakeAllSplitBehaviour()).buildNode();

        builder.getTransitionBuilder().transitionGoesFromTo(node, node2).buildTransition();

        return new BpmnToken(node, new ProcessInstance(null, Mockito.mock(BpmnTokenBuilder.class)), null);
    }
}
