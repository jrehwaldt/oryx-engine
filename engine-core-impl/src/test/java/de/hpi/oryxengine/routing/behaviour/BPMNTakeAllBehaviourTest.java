package de.hpi.oryxengine.routing.behaviour;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The test of the TakeAllBehaviour-activity.
 */
public class BPMNTakeAllBehaviourTest {

    /** The process token. */
    private Token token = null;

    /**
     * Set up. A token is built.
     */
    @BeforeClass
    public void setUp() {

        token = simpleToken();
    }

    /**
     * Test class.
     * 
     */
    @Test
    public void testClass() {

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
    private TokenImpl simpleToken() {

        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();
        
        Node node = builder.getNodeBuilder().setActivityBlueprintFor(NullActivity.class)
        .setIncomingBehaviour(new SimpleJoinBehaviour()).setOutgoingBehaviour(new TakeAllSplitBehaviour()).buildNode();

        Node node2 = builder.getNodeBuilder().setActivityBlueprintFor(NullActivity.class)
        .setIncomingBehaviour(new SimpleJoinBehaviour()).setOutgoingBehaviour(new TakeAllSplitBehaviour()).buildNode();

        builder.getTransitionBuilder().transitionGoesFromTo(node, node2).buildTransition();

        return new TokenImpl(node);
    }

    /**
     * Execute split and join.
     * 
     * @param token
     *            the token
     * @return the list
     * @throws Exception
     *             the exception
     */
    private List<Token> executeSplitAndJoin(Token token)
    throws Exception {

        Node node = token.getCurrentNode();
        IncomingBehaviour incomingBehaviour = node.getIncomingBehaviour();
        OutgoingBehaviour outgoingBehaviour = node.getOutgoingBehaviour();

        List<Token> joinedTokens = incomingBehaviour.join(token);

        return outgoingBehaviour.split(joinedTokens);
    }

}
