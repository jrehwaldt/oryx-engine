package org.jodaengine.process.token;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertEqualsNoOrder;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.activity.NullActivity;
import org.jodaengine.node.incomingbehaviour.AndJoinBehaviour;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.structure.ControlFlowBuilder;
import org.jodaengine.process.structure.ControlFlowBuilderImpl;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeBuilder;
import org.jodaengine.process.structure.NodeBuilderImpl;
import org.jodaengine.process.structure.NodeImpl;
import org.jodaengine.process.token.builder.BpmnTokenBuilder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The test for the process instance.
 */
public class ProcessTokenImplTest {

    /** The process instance. */
    private Token token = null;

    /** Different Nodes. */
    private NodeImpl node = null, node2 = null, node3 = null;

    /** The {@link ControlFlow} to be taken. */
    private ControlFlow controlFlowToTake = null;

    /**
     * Set up.
     * An instance is build.
     */
    @BeforeMethod
    public void setUp() {

        token = simpleToken();
    }

    /**
     * Test for taking all {@link ControlFlow}s.
     * Two new tokens shall be ready for execution if the parent token goes along all edges.
     * The new tokens should then point to the succeeding nodes of the initial token's node.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testTakeAllControlFlows()
    throws Exception {

        Node currentNode = token.getCurrentNode();

        List<Token> newTokens = token.navigateTo(currentNode.getOutgoingControlFlows());
        assertEquals(newTokens.size(), 2, "You should have two new process tokens");

        Node[] currentNodes = new Node[2];
        for (int i = 0; i < 2; i++) {
            currentNodes[i] = newTokens.get(i).getCurrentNode();
        }

        Node[] expectedCurrentNodes = {node2, node3};
        assertEqualsNoOrder(currentNodes, expectedCurrentNodes, "The new tokens should point to the following nodes.");
    }

    /**
     * Test the taking of a single {@link ControlFlow}.
     * 
     * @throws Exception
     *             if it fails
     */
    @Test
    public void testTakeSingleTransition()
    throws Exception {

        List<ControlFlow> controlFlowList = new ArrayList<ControlFlow>();
        controlFlowList.add(controlFlowToTake);
        List<Token> newTokens = token.navigateTo(controlFlowList);
        assertEquals(newTokens.size(), 1, "You should have a single process token.");

        Token newToken = newTokens.get(0);
        assertEquals(newToken, token, "The token should be the same, no child instance or something like that.");
        assertEquals(newToken.getCurrentNode(), node2, "The token should have moved on.");
    }

    /**
     * Tests that the activity behaviour is only executed, if the join was successful.
     * @throws JodaEngineException 
     */
    @Test
    public void testActivityBehaviourExecution() throws JodaEngineException {

        Navigator nav = mock(Navigator.class);
        
        NodeBuilder builder = new NodeBuilderImpl();
        Activity mockBehaviour = mock(Activity.class);

        // create nodes
        Node joinNode = builder.setActivityBehavior(mockBehaviour).setIncomingBehaviour(new AndJoinBehaviour())
        .setOutgoingBehaviour(new TakeAllSplitBehaviour()).buildNode();

        Node beforeNode1 = builder.setIncomingBehaviour(new SimpleJoinBehaviour())
        .setActivityBehavior(new NullActivity()).buildNode();
        Node beforeNode2 = builder.buildNode();
        
        // create Control flow
        ControlFlowBuilder cfBuilder = new ControlFlowBuilderImpl();
        cfBuilder.controlFlowGoesFromTo(beforeNode1, joinNode).buildControlFlow();
        cfBuilder.controlFlowGoesFromTo(beforeNode2, joinNode).buildControlFlow();
        
        TokenBuilder tokenBuilder = new BpmnTokenBuilder(nav, null);
        
        ProcessInstance instance = new ProcessInstance(null, tokenBuilder);
        Token token1 = new BpmnToken(beforeNode1, instance, nav);
        Token token2 = new BpmnToken(beforeNode2, instance, nav);
        
        token1.executeStep();
        // Token1 is on the and-join now
        
        assertEquals(token1.getCurrentNode(), joinNode, "Token1 should be on the join node");
        token1.executeStep();        
        verify(mockBehaviour, never()).execute(any(Token.class));
        
        token2.executeStep();
        assertEquals(token2.getCurrentNode(), joinNode, "Token2 should be on the join node");
        token2.executeStep();
        verify(mockBehaviour).execute(any(Token.class));
    }

    /**
     * Creates a simple token.
     * 
     * @return the process instance impl
     */
    private Token simpleToken() {

        node = new NodeImpl(new NullActivity(), new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
        node2 = new NodeImpl(new NullActivity(), new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
        node3 = new NodeImpl(new NullActivity(), new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
        node.controlFlowTo(node2);

        controlFlowToTake = node.getOutgoingControlFlows().get(0);

        node.controlFlowTo(node3);

        TokenBuilder tokenBuilder = new BpmnTokenBuilder(null, null);
        return new BpmnToken(node, new ProcessInstance(null, tokenBuilder), null);
    }
}
