package de.hpi.oryxengine.process.definition;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;

/**
 * The Class ProcessBuilderTest.
 * @author thorben
 */
public class ProcessBuilderTest {
    private ProcessDefinitionBuilder builder = null;
    private Node startNode, endNode;

    /**
     * Test simple build process.
     * @throws IllegalStarteventException 
     */
    @Test
    public void testSimpleBuildProcess() throws IllegalStarteventException {

        NodeParameterBuilder nodeParamBuilder = new NodeParameterBuilderImpl();
        nodeParamBuilder.setActivityBlueprintFor(NullActivity.class);
        startNode = builder.createStartNode(nodeParamBuilder.buildNodeParameter());

        endNode = builder.createNode(nodeParamBuilder.buildNodeParameter());

        ProcessDefinition definition = builder.createTransition(startNode, endNode).buildDefinition();

        List<Node> startNodes = definition.getStartNodes();
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
