package org.jodaengine.node.factory.bpmn;

import org.jodaengine.node.activity.NullActivity;
import org.jodaengine.node.activity.bpmn.BpmnEndEventActivity;
import org.jodaengine.node.activity.bpmn.BpmnEventBasedXorGateway;
import org.jodaengine.node.activity.bpmn.BpmnHumanTaskActivity;
import org.jodaengine.node.activity.bpmn.BpmnJavaClassScriptingActivity;
import org.jodaengine.node.activity.bpmn.BpmnStartEvent;
import org.jodaengine.node.activity.bpmn.BpmnTerminatingEndEventActivity;
import org.jodaengine.node.activity.bpmn.BpmnTimerIntermediateEventActivity;
import org.jodaengine.node.factory.TransitionFactory;
import org.jodaengine.node.incomingbehaviour.AndJoinBehaviour;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.EmptyOutgoingBehaviour;
import org.jodaengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import org.jodaengine.node.outgoingbehaviour.XORSplitBehaviour;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeBuilder;
import org.jodaengine.resource.allocation.CreationPattern;

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
        BpmnStartEvent activityBehavior = new BpmnStartEvent();
        return decorateBpmnDefaultRouting(nodeBuilder).setActivityBehavior(activityBehavior).buildNode();
    }

    /**
     * Creates a {@link Node EndNode} that represents the {@link BpmnEndEventActivity}.
     * 
     * The IncomingBehaviour is a {@link AndJoinBehaviour} and the OutgoingBehaviour is a {@link EmptyOutgoingBehaviour}
     * .
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @return a {@link Node} representing an {@link BpmnEndEventActivity}
     */
    public static Node createBpmnEndEventNode(ProcessDefinitionBuilder builder) {

        BpmnEndEventActivity activityBehavior = new BpmnEndEventActivity();
        return builder.getNodeBuilder().setIncomingBehaviour(new SimpleJoinBehaviour())
        .setOutgoingBehaviour(new EmptyOutgoingBehaviour()).setActivityBehavior(activityBehavior).buildNode();
    }

    /**
     * Creates a {@link Node} that represents the {@link BpmnTimerIntermediateEventActivity}.
     * 
     * It has the default BPMN Incoming- and OutgoingBehaviour as specified
     * {@link BpmnNodeFactory#decorateBpmnDefaultRouting(NodeBuilder) here}.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @param waitingTime
     *            - the time (in milliseconds) to wait for
     * @return a {@link Node} representing an {@link BpmnTimerIntermediateEventActivity}
     */
    public static Node createBpmnIntermediateTimerEventNode(ProcessDefinitionBuilder builder, long waitingTime) {

        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        BpmnTimerIntermediateEventActivity activityBehavior = new BpmnTimerIntermediateEventActivity(waitingTime);
        return decorateBpmnDefaultRouting(nodeBuilder).setActivityBehavior(activityBehavior).buildNode();
    }

    /**
     * Creates a {@link Node} that represents the {@link BpmnHumanTaskActivity}.
     * 
     * It has the default BPMN Incoming- and OutgoingBehaviour as specified
     * {@link BpmnNodeFactory#decorateBpmnDefaultRouting(NodeBuilder) here}.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @param creationPattern
     *            - the creation pattern to distribute an item
     * @return a {@link Node} representing an {@link BpmnHumanTaskActivity}
     */
    public static Node createBpmnUserTaskNode(ProcessDefinitionBuilder builder, CreationPattern creationPattern) {

        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        BpmnHumanTaskActivity activityBehavior = new BpmnHumanTaskActivity(creationPattern);
        return decorateBpmnDefaultRouting(nodeBuilder).setActivityBehavior(activityBehavior).buildNode();
    }
    
    public static Node createBpmnJavaClassScriptTaskNode(ProcessDefinitionBuilder builder, String className) {
        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        BpmnJavaClassScriptingActivity activityBehavior = new BpmnJavaClassScriptingActivity(className);
        return decorateBpmnDefaultRouting(nodeBuilder).setActivityBehavior(activityBehavior).buildNode();
    }

    /**
     * Creates a {@link Node EndNode} that represents the {@link BpmnTerminatingEndEventActivity}.
     * 
     * The IncomingBehaviour is a {@link AndJoinBehaviour} and the OutgoingBehaviour is a {@link EmptyOutgoingBehaviour}
     * .
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @return a {@link Node} representing an {@link BpmnEndEventActivity}
     */
    public static Node createBpmnTerminatingEndEventNode(ProcessDefinitionBuilder builder) {

        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        BpmnTerminatingEndEventActivity activityBehavior = new BpmnTerminatingEndEventActivity();
        return decorateBpmnDefaultRouting(nodeBuilder).setActivityBehavior(activityBehavior).buildNode();
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

        NullActivity activityBehavior = new NullActivity();
        return builder.getNodeBuilder().setIncomingBehaviour(new SimpleJoinBehaviour())
        .setOutgoingBehaviour(new XORSplitBehaviour()).setActivityBehavior(activityBehavior).buildNode();
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

        NullActivity activityBehavior = new NullActivity();
        return builder.getNodeBuilder().setIncomingBehaviour(new AndJoinBehaviour())
        .setOutgoingBehaviour(new TakeAllSplitBehaviour()).setActivityBehavior(activityBehavior).buildNode();
    }

    /**
     * Creates a {@link Node} that represents the BPMN-Event-Based-Xor-Gateway.
     * 
     * The IncomingBehaviour is a {@link SimpleJoinBehaviour} and the OutgoingBehaviour is a
     * {@link TakeAllSplitBehaviour}.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @return a {@link Node} representing a BPMN-Event-Based-Xor-Gateway
     */
    public static Node createBpmnEventBasedXorGatewayNode(ProcessDefinitionBuilder builder) {

        BpmnEventBasedXorGateway activityBehavior = new BpmnEventBasedXorGateway();
        return builder.getNodeBuilder().setIncomingBehaviour(new SimpleJoinBehaviour())
        .setOutgoingBehaviour(new TakeAllSplitBehaviour()).setActivityBehavior(activityBehavior).buildNode();
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
