package de.hpi.oryxengine.node.factory.bpmn;

import de.hpi.oryxengine.allocation.CreationPattern;
import de.hpi.oryxengine.node.activity.NullActivity;
import de.hpi.oryxengine.node.activity.bpmn.BpmnEndActivity;
import de.hpi.oryxengine.node.activity.bpmn.BpmnHumanTaskActivity;
import de.hpi.oryxengine.node.activity.bpmn.BpmnIntermediateTimerActivity;
import de.hpi.oryxengine.node.activity.bpmn.BpmnStartEvent;
import de.hpi.oryxengine.node.activity.bpmn.BpmnTerminatingEndActivity;
import de.hpi.oryxengine.node.factory.TransitionFactory;
import de.hpi.oryxengine.node.incomingbehaviour.AndJoinBehaviour;
import de.hpi.oryxengine.node.incomingbehaviour.SimpleJoinBehaviour;
import de.hpi.oryxengine.node.outgoingbehaviour.EmptyOutgoingBehaviour;
import de.hpi.oryxengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import de.hpi.oryxengine.node.outgoingbehaviour.XORSplitBehaviour;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeBuilder;

/**
 * This Factory is able to create {@link Node Nodes} for specific BPMN constructs like an BPMN-XOR-Gateway or ...
 */
public final class BpmnNodeFactory extends TransitionFactory {

    /**
     * Hidden Constructor.
     */
    private BpmnNodeFactory() {

    }

    /**
     * Creates a {@link Node StartNode} that represents the {@link BpmnStartEvent}.
     * 
     * It has the default BPMN Incoming- and OutgoingBehaviour as specified
     * {@link BpmnNodeFactory#decorateBpmnDefaultRouting(NodeBuilder) here}.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @return a {@link Node} representing an {@link BpmnStartEvent}
     */
    public static Node createBpmnStartEventNode(ProcessDefinitionBuilder builder) {

        NodeBuilder nodeBuilder = builder.getStartNodeBuilder();
        return decorateBpmnDefaultRouting(nodeBuilder).setActivityBlueprintFor(BpmnStartEvent.class).buildNode();
    }

    /**
     * Creates a {@link Node EndNode} that represents the {@link BpmnEndActivity}.
     * 
     * The IncomingBehaviour is a {@link AndJoinBehaviour} and the OutgoingBehaviour is a {@link EmptyOutgoingBehaviour}
     * .
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @return a {@link Node} representing an {@link BpmnEndActivity}
     */
    public static Node createBpmnEndEventNode(ProcessDefinitionBuilder builder) {

        return builder.getNodeBuilder().setIncomingBehaviour(new SimpleJoinBehaviour())
        .setOutgoingBehaviour(new EmptyOutgoingBehaviour()).setActivityBlueprintFor(BpmnEndActivity.class).buildNode();
    }

    /**
     * Creates a {@link Node} that represents the {@link BpmnIntermediateTimerActivity}.
     * 
     * It has the default BPMN Incoming- and OutgoingBehaviour as specified
     * {@link BpmnNodeFactory#decorateBpmnDefaultRouting(NodeBuilder) here}.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @param waitingTime
     *            - the time (in milliseconds) to wait for
     * @return a {@link Node} representing an {@link BpmnIntermediateTimerActivity}
     */
    public static Node createBpmnIntermediateTimerEventNode(ProcessDefinitionBuilder builder, long waitingTime) {

        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        return decorateBpmnDefaultRouting(nodeBuilder).setActivityBlueprintFor(BpmnIntermediateTimerActivity.class)
        .addConstructorParameter(long.class, waitingTime).buildNode();
    }

    /**
     * Creates a {@link Node} that represents the {@link BpmnHumanTaskActivity}.
     * 
     * It has the default BPMN Incoming- and OutgoingBehaviour as specified
     * {@link BpmnNodeFactory#decorateBpmnDefaultRouting(NodeBuilder) here}.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @param task
     *            - the task to distribute
     * @return a {@link Node} representing an {@link BpmnHumanTaskActivity}
     */
    public static Node createBpmnUserTaskNode(ProcessDefinitionBuilder builder, CreationPattern pattern) {

        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        return decorateBpmnDefaultRouting(nodeBuilder).setActivityBlueprintFor(BpmnHumanTaskActivity.class)
        .addConstructorParameter(CreationPattern.class, pattern).buildNode();
    }

    /**
     * Creates a {@link Node EndNode} that represents the {@link BpmnTerminatingEndActivity}.
     * 
     * The IncomingBehaviour is a {@link AndJoinBehaviour} and the OutgoingBehaviour is a {@link EmptyOutgoingBehaviour}
     * .
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @return a {@link Node} representing an {@link BpmnEndActivity}
     */
    public static Node createBpmnTerminatingEndEventNode(ProcessDefinitionBuilder builder) {

        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        return decorateBpmnDefaultRouting(nodeBuilder).setActivityBlueprintFor(BpmnTerminatingEndActivity.class)
        .buildNode();
    }

    /**
     * Creates a {@link Node} that represents the BPMN-XOR-Gateway.
     * 
     * The IncomingBehaviour is a {@link SimpleJoinBehaviour} and the OutgoingBehaviour is a {@link XORSplitBehaviour}.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @return a {@link Node} representing a BPMN-XOR-Gateway
     */
    public static Node createBpmnXorGatewayNode(ProcessDefinitionBuilder builder) {

        return builder.getNodeBuilder().setIncomingBehaviour(new SimpleJoinBehaviour())
        .setOutgoingBehaviour(new XORSplitBehaviour()).setActivityBlueprintFor(NullActivity.class).buildNode();
    }

    /**
     * Creates a {@link Node} that represents the BPMN-AND-Gateway.
     * 
     * The IncomingBehaviour is a {@link AndJoinBehaviour} and the OutgoingBehaviour is a {@link TakeAllSplitBehaviour}.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @return a {@link Node} representing a BPMN-AND-Gateway
     */
    public static Node createBpmnAndGatewayNode(ProcessDefinitionBuilder builder) {

        return builder.getNodeBuilder().setIncomingBehaviour(new AndJoinBehaviour())
        .setOutgoingBehaviour(new TakeAllSplitBehaviour()).setActivityBlueprintFor(NullActivity.class).buildNode();
    }

    /**
     * Decorates the Node configured by the given {@link NodeBuilder} with the standard BPMN routing behavior.
     * 
     * As IncomingBehaviour the {@link SimpleJoinBehaviour} is decorated. As OutgoingBehaviour the
     * {@link TakeAllSplitBehaviour} is decorated.
     * 
     * @param nodeBuilder
     *            - a {@link NodeBuilder} that configures the {@link Node}
     * @return a {@link NodeBuilder}
     */
    static NodeBuilder decorateBpmnDefaultRouting(NodeBuilder nodeBuilder) {

        return nodeBuilder.setIncomingBehaviour(new SimpleJoinBehaviour()).setOutgoingBehaviour(
            new TakeAllSplitBehaviour());
    }
}
