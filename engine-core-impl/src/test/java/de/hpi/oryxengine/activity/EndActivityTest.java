package de.hpi.oryxengine.activity;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.activity.impl.BPMNActivityFactory;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.EmptyOutgoingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The Class EndActivityTest.
 */
public class EndActivityTest {
    private AbstractProcessInstance instance = null;
    private Node startNode = null;

    /**
     * Process instance finalization test.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void processInstanceFinalizationTest()
    throws Exception {

        NavigatorImplMock nav = new NavigatorImplMock();

        // this is done for test purposes. Usually the startProcessInstance methods of the navigator would do this, but
        // we do not actually want to start the navigator here.
        nav.getRunningInstances().add(instance);
        Token token = instance.createToken(startNode, nav);

        // perform fist step, there should be two tokens on forkNode1 and forkNode2 respectively
        token.executeStep();

        List<Token> newTokens = nav.getWorkQueue();
        assertEquals(newTokens.size(), 2);

        // don't reference the token that was split, which remains on the splitNode
        assertEquals(instance.getAssignedTokens().size(), 2,
            "should not be 3. if 3, the token on the splitNode might still be referenced.");

        // we need to explicitly reference these two tokens, as newTokens will probably change upon the next
        // executeStep() on one of the tokens.
        Token forkNodeToken1 = newTokens.get(0);
        Token forkNodeToken2 = newTokens.get(1);

        // move the two new tokens to the endNodes
        forkNodeToken1.executeStep();
        forkNodeToken2.executeStep();

        // as there was no fork executed, we assume that the two tokens are now on the endNodes.
        forkNodeToken1.executeStep();

        assertTrue(nav.getRunningInstances().contains(instance), "The instance should be still marked as running.");
        assertFalse(nav.getEndedInstances().contains(instance), "The instance should not be marked as finished yet.");

        // now, no process instance cleanup should have happened, as forkNodeToken2 is still on the way.

        forkNodeToken2.executeStep();

        // now, the process instance cleanup should have happened.
        assertTrue(nav.getEndedInstances().contains(instance), "The instance should be now marked as finished.");
        assertFalse(nav.getRunningInstances().contains(instance), "The instance should not be marked as running.");

        assertFalse(instance.hasAssignedTokens(),
            "no tokens should be assigned to this instance anymore, as it has finished execution.");

    }

    /**
     * Persistence of instance variables test.
     */
    @Test
    public void persistenceOfInstanceVariablesTest() {

        // TODO implement this, as soon as we have persistence in the engine.
    }

    /**
     * This method prepares a process for this test.
     */
    @BeforeClass
    public void prepareProcessForTest()
    throws IllegalStarteventException {

        ProcessDefinitionBuilder builder = new ProcessBuilderImpl();

        startNode = BPMNActivityFactory.createBPMNStartEventNode(builder);
        
        int[] ints = {1, 1};
        Node forkNode1 = BPMNActivityFactory.createBPMNAddNumbersAndStoreNode(builder, "result", ints);

        int[] anotherInts = {2, 2};
        Node forkNode2 = BPMNActivityFactory.createBPMNAddNumbersAndStoreNode(builder, "result2", anotherInts);

        BPMNActivityFactory.createTransitionFromTo(builder, startNode, forkNode1);
        BPMNActivityFactory.createTransitionFromTo(builder, startNode, forkNode2);
        

        Node endNode1 = BPMNActivityFactory.createBPMNEndEventNode(builder); 
        Node endNode2 = BPMNActivityFactory.createBPMNEndEventNode(builder);

        BPMNActivityFactory.createTransitionFromTo(builder, forkNode1, endNode1);
        BPMNActivityFactory.createTransitionFromTo(builder, forkNode2, endNode2);

        ProcessDefinition definition = builder.buildDefinition();

        instance = new ProcessInstanceImpl(definition);
    }

}
