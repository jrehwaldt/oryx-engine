package de.hpi.oryxengine.node.activity;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.exception.JodaEngineException;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.node.factory.bpmn.BpmnCustomNodeFactory;
import de.hpi.oryxengine.node.factory.bpmn.BpmnNodeFactory;
import de.hpi.oryxengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilderImpl;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Class TerminatingEndActivityTest.
 */
public class TerminatingEndActivityTest {
    private ProcessDefinition definition;
    private Node startNode, xorJoinNode;

    @Test
    public void testingTerminatingEndActivity()
    throws JodaEngineException {

        NavigatorImplMock nav = new NavigatorImplMock();
        AbstractProcessInstance instance = new ProcessInstanceImpl(definition);
        Token startToken = instance.createToken(startNode, nav);

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

        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();

        startNode = BpmnCustomNodeFactory.createBpmnNullNode(builder);

        Node andSplitNode = BpmnCustomNodeFactory.createBpmnNullNode(builder);

        xorJoinNode = BpmnCustomNodeFactory.createBpmnNullNode(builder);

        Node terminatingEnd = BpmnNodeFactory.createBpmnTerminatingEndEventNode(builder);

        Node computationNode = BpmnCustomNodeFactory.createBpmnHashComputationNode(builder, "result",
            "meinlieblingspasswort");

        BpmnNodeFactory.createTransitionFromTo(builder, startNode, andSplitNode);
        BpmnNodeFactory.createTransitionFromTo(builder, andSplitNode, xorJoinNode);
        BpmnNodeFactory.createTransitionFromTo(builder, andSplitNode, terminatingEnd);
        BpmnNodeFactory.createTransitionFromTo(builder, xorJoinNode, computationNode);
        BpmnNodeFactory.createTransitionFromTo(builder, computationNode, xorJoinNode);
        
        BpmnProcessDefinitionModifier.decorateWithNormalBpmnProcessInstantiation(builder);
        definition = builder.buildDefinition();
    }
}
