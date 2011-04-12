package de.hpi.oryxengine.activity;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.process.definition.NodeParameter;
import de.hpi.oryxengine.process.definition.NodeParameterImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilder;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The Class ConcurrentActivityStateTest.
 */
public class ConcurrentActivityStateTest {
    private ProcessDefinition definition;
    private Activity startActivity;
    private Node startNode;

    /**
     * One definition, two instances, two tokens that work on the same activities. This test ensures that these
     * activities do not share their state.
     * 
     * @throws DalmatinaException
     *             the dalmatina exception
     */
    @Test
    public void testConcurrentAcitivityUse()
    throws DalmatinaException {

        // Create two process instances
        NavigatorImplMock nav = new NavigatorImplMock();
        ProcessInstance instance1 = new ProcessInstanceImpl(definition);
        Token token1 = instance1.createToken(startNode, nav);

        ProcessInstance instance2 = new ProcessInstanceImpl(definition);
        Token token2 = instance2.createToken(startNode, nav);

        assertEquals(token1.getCurrentNode().getActivity().getState(), ActivityState.INIT);
        assertEquals(token2.getCurrentNode().getActivity().getState(), ActivityState.INIT);

        // Execute a step with token1 on instance1. We expect, that the activity state changes for instance1, but not
        // for instance2/token2.

        token1.executeStep();
        assertEquals(startNode.getActivity().getState(), ActivityState.COMPLETED);
        assertEquals(token2.getCurrentNode().getActivity().getState(), ActivityState.INIT);

    }

    /**
     * Sets the up process.
     *
     * @throws IllegalStarteventException the illegal startevent exception
     */
    @BeforeClass
    public void setUpProcess()
    throws IllegalStarteventException {

        ProcessBuilder builder = new ProcessBuilderImpl();
        NodeParameter param = new NodeParameterImpl();
        startActivity = new NullActivity();
        param.setActivity(startActivity);
        param.setIncomingBehaviour(new SimpleJoinBehaviour());
        param.setOutgoingBehaviour(new TakeAllSplitBehaviour());
        param.makeStartNode(true);

        startNode = builder.createNode(param);

        param.setActivity(new NullActivity());
        param.makeStartNode(false);
        Node endNode = builder.createNode(param);

        definition = builder.createTransition(startNode, endNode).buildDefinition();
    }
}
