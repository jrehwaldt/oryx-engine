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
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BPMNToken;
import org.jodaengine.process.token.BPMNTokenImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * The test of the TakeAllBehaviour-activity.
 */
public class BPMNTakeAllBehaviourTest {

    /** The process token. */
    private BPMNToken bPMNToken = null;

    /**
     * Set up. A token is built.
     */
    @BeforeClass
    public void setUp() {

        bPMNToken = simpleToken();
    }

    /**
     * Test class.
     * 
     */
    @Test
    public void testClass() {

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
     * Tear down.
     */
    @AfterClass
    public void tearDown() {

    }

    /**
     * Simple token. An activity is set up, it gets a behavior and a transition to a second node.
     * 
     * @return the process instance that was created within the method
     */
    private BPMNTokenImpl simpleToken() {

        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();
        
        Node<BPMNToken> node = builder.getNodeBuilder().setActivityBehavior(new NullActivity())
        .setIncomingBehaviour(new SimpleJoinBehaviour()).setOutgoingBehaviour(new TakeAllSplitBehaviour()).buildNode();

        Node<BPMNToken> node2 = builder.getNodeBuilder().setActivityBehavior(new NullActivity())
        .setIncomingBehaviour(new SimpleJoinBehaviour()).setOutgoingBehaviour(new TakeAllSplitBehaviour()).buildNode();

        builder.getTransitionBuilder().transitionGoesFromTo(node, node2).buildTransition();

        return new BPMNTokenImpl(node);
    }

    /**
     * Execute split and join.
     * 
     * @param bPMNToken
     *            the token
     * @return the list
     * @throws Exception
     *             the exception
     */
    private List<BPMNToken> executeSplitAndJoin(BPMNToken bPMNToken)
    throws Exception {

        Node<BPMNToken> node = bPMNToken.getCurrentNode();
        IncomingBehaviour<BPMNToken> incomingBehaviour = node.getIncomingBehaviour();
        OutgoingBehaviour outgoingBehaviour = node.getOutgoingBehaviour();

        List<BPMNToken> joinedTokens = incomingBehaviour.join(bPMNToken);

        return outgoingBehaviour.split(joinedTokens);
    }

}
