package de.hpi.oryxengine.factories.process;

import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.process.definition.NodeParameterBuilder;
import de.hpi.oryxengine.process.definition.NodeParameterBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * A factory for creating ExampleProcessToken objects. These objects just have 2
 * add Number activities.
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

	NodeParameterBuilder nodeParamBuilder = new NodeParameterBuilderImpl(
		new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
	nodeParamBuilder.setActivityBlueprintFor(NullActivity.class);
	startNode = builder.createStartNode(nodeParamBuilder
		.buildNodeParameter());

	nodeParamBuilder = new NodeParameterBuilderImpl(
		new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
	int[] ints = { 1, 1 };
	nodeParamBuilder
		.setActivityBlueprintFor(
			AddNumbersAndStoreActivity.class)
		.addConstructorParameter(String.class, "result")
		.addConstructorParameter(int[].class, ints);
	node1 = builder.createNode(nodeParamBuilder.buildNodeParameter());
	node2 = builder.createNode(nodeParamBuilder.buildNodeParameter());
	builder.createTransition(startNode, node1).createTransition(node1,
		node2);

	nodeParamBuilder = new NodeParameterBuilderImpl();
	nodeParamBuilder.setActivityBlueprintFor(EndActivity.class);
	Node endNode = builder.createNode(nodeParamBuilder.buildNodeParameter());
	builder.createTransition(node2, endNode);
    }

}
