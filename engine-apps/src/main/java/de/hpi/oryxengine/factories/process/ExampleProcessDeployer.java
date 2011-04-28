package de.hpi.oryxengine.factories.process;

import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.activity.impl.BPMNActivityFactory;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.EmptyOutgoingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * A factory for creating ExampleProcessToken objects. These objects just have 2 add Number activities.
 */
public class ExampleProcessDeployer extends AbstractProcessDeployer {

    /** The node1. */
    private Node node1;

    /** The node2. */
    private Node node2;

    /** The start node. */
    private Node startNode;

    /**
     * Instantiates a new example process token factory.
     */
    public ExampleProcessDeployer() {

        builder = new ProcessBuilderImpl();
    }

    /**
     * Initializes the nodes.
     */
    public void initializeNodes() {

        startNode = BPMNActivityFactory.createBPMNNullStartNode(builder);

        int[] ints = {1, 1};
        node1 = BPMNActivityFactory.createBPMNAddNumbersAndStoreNode(builder, "result", ints);
        node2 = BPMNActivityFactory.createBPMNAddNumbersAndStoreNode(builder, "result", ints);
        
        Node endNode = BPMNActivityFactory.createBPMNEndEventNode(builder);
        
        BPMNActivityFactory.createTransitionFromTo(builder, startNode, node1);
        BPMNActivityFactory.createTransitionFromTo(builder, node1, node2);
        BPMNActivityFactory.createTransitionFromTo(builder, node2, endNode);
    }

}
