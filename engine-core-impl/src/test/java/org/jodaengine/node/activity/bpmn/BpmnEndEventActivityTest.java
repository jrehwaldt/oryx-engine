package org.jodaengine.node.activity.bpmn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.navigator.NavigatorImplMock;
import org.jodaengine.node.factory.ControlFlowFactory;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import org.jodaengine.process.definition.BpmnProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenBuilder;
import org.jodaengine.process.token.builder.BpmnTokenBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * The Class EndActivityTest.
 */
public class BpmnEndEventActivityTest {
    private AbstractProcessInstance instance = null;
    private Node startNode = null;
    private NavigatorImplMock nav;
    private TokenBuilder tokenBuilder;

    /**
     * Process instance finalization test.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void processInstanceFinalizationTest()
    throws Exception {

        // this is done for test purposes. Usually the startProcessInstance methods of the navigator would do this, but
        // we do not actually want to start the navigator here.
        nav.getRunningInstances().add(instance);
        Token token = instance.createToken(startNode);

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
     * This method prepares a process for this test.
     * 
     * @throws IllegalStarteventException test fails
     */
    @BeforeClass
    public void prepareProcessForTest()
    throws IllegalStarteventException {

        BpmnProcessDefinitionBuilder builder = BpmnProcessDefinitionBuilder.newBuilder();

        startNode = BpmnNodeFactory.createBpmnStartEventNode(builder);
        
        int[] ints = {1, 1};
        Node forkNode1 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result", ints);

        int[] anotherInts = {2, 2};
        Node forkNode2 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result2", anotherInts);

        Node endNode1 = BpmnNodeFactory.createBpmnEndEventNode(builder); 
        Node endNode2 = BpmnNodeFactory.createBpmnEndEventNode(builder);

        ControlFlowFactory.createControlFlowFromTo(builder, startNode, forkNode1);
        ControlFlowFactory.createControlFlowFromTo(builder, startNode, forkNode2);
        ControlFlowFactory.createControlFlowFromTo(builder, forkNode1, endNode1);
        ControlFlowFactory.createControlFlowFromTo(builder, forkNode2, endNode2);

        BpmnProcessDefinitionModifier.decorateWithDefaultBpmnInstantiationPattern(builder);
        ProcessDefinition definition = builder.buildDefinition();
        
        nav = new NavigatorImplMock();
        tokenBuilder = new BpmnTokenBuilder(nav, null);
        instance = new ProcessInstance(definition, tokenBuilder);
    }

}
