package de.hpi.oryxengine.node.factory.bpmn;

import de.hpi.oryxengine.node.activity.NullActivity;
import de.hpi.oryxengine.node.activity.fun.AddContextNumbersAndStoreActivity;
import de.hpi.oryxengine.node.activity.fun.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.node.activity.fun.HashComputationActivity;
import de.hpi.oryxengine.node.activity.fun.PrintingVariableActivity;
import de.hpi.oryxengine.node.factory.TransitionFactory;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeBuilder;

/**
 * This Factory is able to create {@link Node} for specific Activity.
 */
public final class BpmnFunNodeFactory extends TransitionFactory {

    /**
     * Hidden Constructor.
     */
    private BpmnFunNodeFactory() {

    }

    /**
     * Creates a {@link Node} that represents the {@link AddNumbersAndStoreActivity}. It has the default BPMN Incoming-
     * and OutgoingBehaviour as specified {@link BpmnNodeFactory#decorateBpmnDefaultRouting(NodeBuilder) here}.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @param variableName
     *            - the name of the variable for accessing the result
     * @param termsOfSum
     *            - the summands that are summed up
     * @return a {@link Node} representing an {@link AddNumbersAndStoreActivity}
     */
    public static Node createBpmnAddNumbersAndStoreNode(ProcessDefinitionBuilder builder,
                                                        String variableName,
                                                        int[] termsOfSum) {

        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        return BpmnNodeFactory.decorateBpmnDefaultRouting(nodeBuilder)
        .setActivityBlueprintFor(AddNumbersAndStoreActivity.class).addConstructorParameter(String.class, variableName)
        .addConstructorParameter(int[].class, termsOfSum).buildNode();
    }

    /**
     * Creates a {@link Node} that represents the {@link HashComputationActivity}. It has the default BPMN Incoming-
     * and OutgoingBehaviour as specified {@link BpmnNodeFactory#decorateBpmnDefaultRouting(NodeBuilder) here}.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @param variableName
     *            - the name of the variable it will be stored in in the process instance
     * @param toBeHashed
     *            - the String to be hashed (maybe a password)
     * @return a {@link Node} representing an {@link HashComputationActivity}
     */
    public static Node createBpmnHashComputationNode(ProcessDefinitionBuilder builder,
                                                     String variableName,
                                                     String toBeHashed) {

        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        return BpmnNodeFactory.decorateBpmnDefaultRouting(nodeBuilder)
        .setActivityBlueprintFor(HashComputationActivity.class).addConstructorParameter(String.class, variableName)
        .addConstructorParameter(String.class, toBeHashed).buildNode();
    }

    /**
     * Creates a {@link Node} that represents the {@link AddContextNumbersAndStoreActivity}.
     * 
     * It has the default BPMN Incoming- and OutgoingBehaviour as specified
     * {@link BpmnNodeFactory#decorateBpmnDefaultRouting(NodeBuilder) here}.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @param variableName
     *            - the name of the variable for accessing the result
     * @param summands
     *            - the summands that are summed up
     * @return a {@link Node} representing an {@link AddContextNumbersAndStoreActivity}
     */
    public static Node createBpmnAddContextNumbersAndStoreNode(ProcessDefinitionBuilder builder,
                                                               String variableName,
                                                               String... summands) {

        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        return BpmnNodeFactory.decorateBpmnDefaultRouting(nodeBuilder)
        .setActivityBlueprintFor(AddContextNumbersAndStoreActivity.class)
        .addConstructorParameter(String.class, variableName).addConstructorParameter(String[].class, summands)
        .buildNode();
    }

    /**
     * Creates a {@link Node} that represents the {@link PrintingVariableActivity}.
     * 
     * It has the default BPMN Incoming- and OutgoingBehaviour as specified
     * {@link BpmnNodeFactory#decorateBpmnDefaultRouting(NodeBuilder) here}.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @param variableToBePrinted
     *            - the variable to be printed
     * @return a {@link Node} representing an {@link PrintingVariableActivity}
     */
    public static Node createBpmnPrintingVariableNode(ProcessDefinitionBuilder builder, String variableToBePrinted) {

        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        return BpmnNodeFactory.decorateBpmnDefaultRouting(nodeBuilder)
        .setActivityBlueprintFor(PrintingVariableActivity.class)
        .addConstructorParameter(String.class, variableToBePrinted).buildNode();
    }

    /**
     * Creates a {@link Node StartNode} that represents the {@link NullActivity}. It actually does nothing ;-D.
     * 
     * It has the default BPMN Incoming- and OutgoingBehaviour as specified
     * {@link BpmnNodeFactory#decorateBpmnDefaultRouting(NodeBuilder) here}.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @return a {@link Node} representing an {@link NullActivity}
     */
    public static Node createBpmnNullStartNode(ProcessDefinitionBuilder builder) {

        NodeBuilder nodeBuilder = builder.getStartNodeBuilder();
        return BpmnNodeFactory.decorateBpmnDefaultRouting(nodeBuilder).setActivityBlueprintFor(NullActivity.class)
        .buildNode();
    }

    /**
     * Creates a {@link Node} that represents the {@link NullActivity}. It actually does nothing ;-D.
     * 
     * It has the default BPMN Incoming- and OutgoingBehaviour as specified
     * {@link BpmnNodeFactory#decorateBpmnDefaultRouting(NodeBuilder) here}.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @return a {@link Node} representing an {@link NullActivity}
     */
    public static Node createBpmnNullNode(ProcessDefinitionBuilder builder) {

        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        return BpmnNodeFactory.decorateBpmnDefaultRouting(nodeBuilder).setActivityBlueprintFor(NullActivity.class)
        .buildNode();
    }
}
