package org.jodaengine.node.activity.bpmn;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.navigator.NavigatorImplMock;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.bpmn.BpmnProcessDefinitionBuilder;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenBuilder;
import org.jodaengine.process.token.builder.BpmnTokenBuilder;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * The Class TerminatingEndActivityTest.
 */
public class BpmnTerminatingEndEventActivityTest {
    private ProcessDefinition definition;
    private Node startNode, xorJoinNode;

    @Test
    public void testingTerminatingEndActivity()
    throws JodaEngineException {

        NavigatorImplMock nav = new NavigatorImplMock();
        TokenBuilder builder = new BpmnTokenBuilder(nav, null);
        AbstractProcessInstance instance = new ProcessInstance(definition, builder);
        Token startToken = instance.createToken(startNode);

        startToken.executeStep();

        nav.consume(startToken);
        startToken.executeStep();

        assertEquals(nav.getWorkQueue().size(), 2,
            "there should be two tokens in the navigator. One on the xorJoinNode, one on the terminating end");

        Token loopToken = nav.getWorkQueue().get(0);
        Token endToken = nav.getWorkQueue().get(1);

        if (!(loopToken.getCurrentNode() == xorJoinNode)) {
            loopToken = endToken;
            endToken = nav.getWorkQueue().get(0);
        }

        // we needed the NavigatorImplMock-Object to get the two produced tokens, now we exchange it by a true mock, in
        // order to verify that a method is never called.
        Navigator anotherNav = mock(Navigator.class);

        Whitebox.setInternalState(loopToken, "navigator", anotherNav);

        // go to computation node
        loopToken.executeStep();

        // execute the computation once
        loopToken.executeStep();

        // the two executed steps with loopToken should have added the token two times to the navigator.
        verify(anotherNav, times(2)).addWorkToken(any(Token.class));

        // execute the terminating end
        endToken.executeStep();

        // this execution should not add the token to the navigator again
        loopToken.executeStep();

        // we do not expect the token to be added to the navigator, as it has been cancelled by the terminating end
        // activity.
        verify(anotherNav, times(2)).addWorkToken(any(Token.class));
    }

    /**
     * We build a process model that is not sound, as it has an infinite loop on one of the two paths after the parallel
     * gateway.
     * 
     * @throws IllegalStarteventException
     *      thrown if an illegal start event is given
     */
    @BeforeClass
    public void setupProcessModel()
    throws IllegalStarteventException {

        BpmnProcessDefinitionBuilder builder = BpmnProcessDefinitionBuilder.newBuilder();

        startNode = BpmnCustomNodeFactory.createBpmnNullNode(builder);

        Node andSplitNode = BpmnCustomNodeFactory.createBpmnNullNode(builder);

        xorJoinNode = BpmnCustomNodeFactory.createBpmnNullNode(builder);

        Node terminatingEnd = BpmnNodeFactory.createBpmnTerminatingEndEventNode(builder);

        Node computationNode = BpmnCustomNodeFactory.createBpmnHashComputationNode(builder, "result",
            "meinlieblingspasswort");

        BpmnNodeFactory.createControlFlowFromTo(builder, startNode, andSplitNode);
        BpmnNodeFactory.createControlFlowFromTo(builder, andSplitNode, xorJoinNode);
        BpmnNodeFactory.createControlFlowFromTo(builder, andSplitNode, terminatingEnd);
        BpmnNodeFactory.createControlFlowFromTo(builder, xorJoinNode, computationNode);
        BpmnNodeFactory.createControlFlowFromTo(builder, computationNode, xorJoinNode);
        
        BpmnProcessDefinitionModifier.decorateWithDefaultBpmnInstantiationPattern(builder);
        definition = builder.buildDefinition();
    }
}
