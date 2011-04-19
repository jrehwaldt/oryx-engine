package de.hpi.oryxengine.activity;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.impl.HashComputationActivity;
import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.activity.impl.TerminatingEndActivity;
import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.process.definition.NodeParameter;
import de.hpi.oryxengine.process.definition.NodeParameterBuilder;
import de.hpi.oryxengine.process.definition.NodeParameterBuilderImpl;
import de.hpi.oryxengine.process.definition.NodeParameterImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.ActivityBlueprint;
import de.hpi.oryxengine.process.structure.ActivityBlueprintImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The Class TerminatingEndActivityTest.
 */
public class TerminatingEndActivityTest {
    ProcessDefinition definition;
    Node startNode, xorJoinNode;

    @Test
    public void f()
    throws DalmatinaException {

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
     */
    @BeforeClass
    public void setupProcessModel()
    throws IllegalStarteventException {

        ProcessDefinitionBuilder builder = new ProcessBuilderImpl();

        ActivityBlueprint blueprint = new ActivityBlueprintImpl(NullActivity.class);
        NodeParameter param = new NodeParameterImpl(blueprint, new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());

        startNode = builder.createStartNode(param);

        Node andSplitNode = builder.createNode(param);

        xorJoinNode = builder.createNode(param);

        // blueprint = new ActivityBlueprintImpl(TerminatingEndActivity.class);
        param = new NodeParameterImpl(TerminatingEndActivity.class, new SimpleJoinBehaviour(),
            new TakeAllSplitBehaviour());

        NodeParameterBuilder paramBuilder = new NodeParameterBuilderImpl(new SimpleJoinBehaviour(),
            new TakeAllSplitBehaviour());

        Node terminatingEnd = builder.createNode(param);

        
        param = paramBuilder.setActivityBlueprintFor(HashComputationActivity.class)
        .addConstructorParameter(String.class, "result").addConstructorParameter(String.class, "meinlieblingspasswort")
        .buildNodeParameter();

        Node computationNode = builder.createNode(param);

        builder.createTransition(startNode, andSplitNode).createTransition(andSplitNode, xorJoinNode)
        .createTransition(andSplitNode, terminatingEnd).createTransition(xorJoinNode, computationNode)
        .createTransition(computationNode, xorJoinNode);

        definition = builder.buildDefinition();
    }
}
