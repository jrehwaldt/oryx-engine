package org.jodaengine.node.factory.petri;

import org.jodaengine.node.activity.bpmn.BpmnEndEventActivity;
import org.jodaengine.node.activity.bpmn.BpmnStartEvent;
import org.jodaengine.node.factory.TransitionFactory;
import org.jodaengine.node.incomingbehaviour.AndJoinBehaviour;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.EmptyOutgoingBehaviour;
import org.jodaengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeBuilder;

/**
 * This Factory is able to create {@link Node Nodes} for specific BPMN constructs like an BPMN-XOR-Gateway or ...
 */
public final class PetriNodeFactory extends TransitionFactory {

    /**
     * Hidden Constructor.
     */
    private PetriNodeFactory() {

    }

    /**
     * Creates a {@link Node StartNode} that represents the {@link BpmnStartEvent}.
     * 
     * It has the default BPMN Incoming- and OutgoingBehaviour as specified
     * {@link PetriNodeFactory#decorateBpmnDefaultRouting(NodeBuilder) here}.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @return a {@link Node} representing an {@link BpmnStartEvent}
     */
    public static Node createPlaceNode(ProcessDefinitionBuilder builder) {

        NodeBuilder nodeBuilder = builder.getStartNodeBuilder();
        return decorateBpmnDefaultRouting(nodeBuilder).buildNode();
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
