package org.jodaengine.process.definition;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.node.activity.NullActivity;
import org.jodaengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.process.token.BPMNToken;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * The Class ProcessBuilderTest.
 * 
 * @author thorben
 */
public class ProcessBuilderTest {
    private ProcessDefinitionBuilder builder = null;
    private Node<BPMNToken> startNode, endNode;

    /**
     * Test simple build process.
     * 
     * @throws IllegalStarteventException
     */
    @Test
    public void testSimpleBuildProcess()
    throws IllegalStarteventException {

        startNode = builder.getStartNodeBuilder().setActivityBehavior(new NullActivity()).buildNode();

        endNode = builder.getNodeBuilder().setActivityBehavior(new NullActivity()).buildNode();

        builder.getTransitionBuilder().transitionGoesFromTo(startNode, endNode).buildTransition();
        
        BpmnProcessDefinitionModifier.decorateWithDefaultBpmnInstantiationPattern(builder);
        ProcessDefinition definition = builder.buildDefinition();

        List<Node<BPMNToken>> startNodes = definition.getStartNodes();
        assertEquals(startNodes.size(), 1, "There should be one start node");

        Node<BPMNToken> node = startNodes.get(0);
        assertEquals(node, startNode, "This node should be the defined startNode");

        List<Transition<BPMNToken>> outgoingTransitions = node.getOutgoingTransitions();
        assertEquals(outgoingTransitions.size(), 1, "There should be one outgoing transition");

        Transition<BPMNToken> transition = outgoingTransitions.get(0);
        assertEquals(transition.getDestination(), endNode, "startNode should be connected to endNode");

    }

    /**
     * Sets the up.
     */
    @BeforeClass
    public void setUp() {

        builder = new ProcessDefinitionBuilderImpl();
    }

}
