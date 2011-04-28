package de.hpi.oryxengine.activity.impl;

import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.process.definition.NodeBuilder;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.AndJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.EmptyOutgoingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.XORSplitBehaviour;

// TODO @Gerardo aus BPMN Bpmn machen
public class BPMNActivityFactory {

    public static Node createBPMNStartEventNode(ProcessDefinitionBuilder builder) {

        NodeBuilder nodeBuilder = builder.getStartNodeBuilder();
        return decorateBPMNDefaultRouting(nodeBuilder).setActivityBlueprintFor(BpmnStartEvent.class).buildNode();
    }

    public static Node createBPMNAddNumbersAndStoreNode(ProcessDefinitionBuilder builder,
                                                        String storeID,
                                                        int[] termsOfSum) {

        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        return decorateBPMNDefaultRouting(nodeBuilder).setActivityBlueprintFor(AddNumbersAndStoreActivity.class)
        .addConstructorParameter(String.class, storeID).addConstructorParameter(int[].class, termsOfSum).buildNode();
    }

    public static Transition createTransitionFromTo(ProcessDefinitionBuilder builder, Node source, Node destination) {

        return builder.getTransitionBuilder().transitionGoesFromTo(source, destination).buildTransition();
    }

    public static Transition createTransitionFor(ProcessDefinitionBuilder builder,
                                                 Node source,
                                                 Node destination,
                                                 Condition condition) {

        return builder.getTransitionBuilder().transitionGoesFromTo(source, destination).setCondition(condition)
        .buildTransition();
    }

    public static Node createBPMNEndEventNode(ProcessDefinitionBuilder builder) {

        return builder.getNodeBuilder().setIncomingBehaviour(new SimpleJoinBehaviour())
        .setOutgoingBehaviour(new EmptyOutgoingBehaviour()).setActivityBlueprintFor(EndActivity.class).buildNode();
    }

    public static Node createBPMNIntermediateTimerEventNode(ProcessDefinitionBuilder builder, long waitingTime) {

        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        return decorateBPMNDefaultRouting(nodeBuilder).setActivityBlueprintFor(IntermediateTimer.class)
        .addConstructorParameter(long.class, waitingTime).buildNode();
    }

    public static Node createBPMNNullStartNode(ProcessDefinitionBuilder builder) {

        NodeBuilder nodeBuilder = builder.getStartNodeBuilder();
        return decorateBPMNDefaultRouting(nodeBuilder).setActivityBlueprintFor(NullActivity.class).buildNode();
    }

    public static Node createBPMNNullNode(ProcessDefinitionBuilder builder) {
        
        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        return decorateBPMNDefaultRouting(nodeBuilder).setActivityBlueprintFor(NullActivity.class).buildNode();
    }

    private static NodeBuilder decorateBPMNDefaultRouting(NodeBuilder nodeBuilder) {

        return nodeBuilder.setIncomingBehaviour(new SimpleJoinBehaviour()).setOutgoingBehaviour(
            new TakeAllSplitBehaviour());
    }

    public static Node createBPMNUserTaskNode(ProcessDefinitionBuilder builder, Task task) {

        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        return decorateBPMNDefaultRouting(nodeBuilder).setActivityBlueprintFor(HumanTaskActivity.class)
        .addConstructorParameter(Task.class, task).buildNode();
    }

    public static Node createBPMNTerminatingEndEventNode(ProcessDefinitionBuilder builder) {

        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        return decorateBPMNDefaultRouting(nodeBuilder).setActivityBlueprintFor(TerminatingEndActivity.class)
        .buildNode();
    }

    public static Node createBPMNHashComputationNode(ProcessDefinitionBuilder builder,
                                                     String variableName,
                                                     String toBeHashed) {

        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        return decorateBPMNDefaultRouting(nodeBuilder).setActivityBlueprintFor(HashComputationActivity.class)
        .addConstructorParameter(String.class, variableName).addConstructorParameter(String.class, toBeHashed)
        .buildNode();
    }

    public static Node createBPMNXorGatewayNode(ProcessDefinitionBuilder builder) {

        return builder.getNodeBuilder().setIncomingBehaviour(new SimpleJoinBehaviour())
        .setOutgoingBehaviour(new XORSplitBehaviour()).setActivityBlueprintFor(NullActivity.class).buildNode();
    }

    public static Node createBPMNAndGatewayNode(ProcessDefinitionBuilder builder) {

        return builder.getNodeBuilder().setIncomingBehaviour(new AndJoinBehaviour())
        .setOutgoingBehaviour(new TakeAllSplitBehaviour()).setActivityBlueprintFor(NullActivity.class).buildNode();
    }

    public static Node createBPMNAddContextNumbersAndStoreNode(ProcessDefinitionBuilder builder,
                                                               String variableName,
                                                               String... summands) {

        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        return decorateBPMNDefaultRouting(nodeBuilder).setActivityBlueprintFor(AddContextNumbersAndStoreActivity.class)
        .addConstructorParameter(String.class, variableName).addConstructorParameter(String[].class, summands)
        .buildNode();
    }

    public static Node createBPMNPrintingVariableNode(ProcessDefinitionBuilder builder,
                                                               String variableToBePrinted) {
        
        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        return decorateBPMNDefaultRouting(nodeBuilder).setActivityBlueprintFor(PrintingVariableActivity.class)
        .addConstructorParameter(String.class, variableToBePrinted)
        .buildNode();
    }
}
