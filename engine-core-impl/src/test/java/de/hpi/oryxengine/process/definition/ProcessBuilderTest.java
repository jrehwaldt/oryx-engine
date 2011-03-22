package de.hpi.oryxengine.process.definition;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.impl.StartActivity;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.StartNode;
import de.hpi.oryxengine.process.structure.Transition;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The Class ProcessBuilderTest.
 * @author thorben
 */
public class ProcessBuilderTest {
    private ProcessBuilder builder;
    private Node startNode, endNode;

    /**
     * Test simple build process.
     */
    @Test
    public void testSimpleBuildProcess() {

        StartNodeParameter param = new StartNodeParameterImpl();

        param.setActivity(new StartActivity());
        param.setIncomingBehaviour(new SimpleJoinBehaviour());
        param.setOutgoingBehaviour(new TakeAllSplitBehaviour());
        param.setStartEvent(null);
        startNode = builder.createStartNode(param);

        endNode = builder.createNode(param);

        ProcessDefinition definition = builder.createTransition(startNode, endNode).buildDefinition();

        List<StartNode> startNodes = definition.getStartNodes();
        assertEquals(startNodes.size(), 1, "There should be one start node");

        Node node = startNodes.get(0);
        assertEquals(node, startNode, "This node should be the defined startNode");

        List<Transition> outgoingTransitions = node.getOutgoingTransitions();
        assertEquals(outgoingTransitions.size(), 1, "There should be one outgoing transition");

        Transition transition = outgoingTransitions.get(0);
        assertEquals(transition.getDestination(), endNode, "startNode should be connected to endNode");

    }

    /**
     * Sets the up.
     */
    @BeforeClass
    public void setUp() {
        builder = new ProcessBuilderImpl();
    }

}
