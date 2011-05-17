package org.jodaengine.node.factory.bpmn;

import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.activity.NullActivity;
import org.jodaengine.node.activity.custom.AddContextNumbersAndStoreActivity;
import org.jodaengine.node.activity.custom.AddNumbersAndStoreActivity;
import org.jodaengine.node.activity.custom.AutomatedDummyActivity;
import org.jodaengine.node.activity.custom.HashComputationActivity;
import org.jodaengine.node.activity.custom.PrintingVariableActivity;
import org.jodaengine.node.factory.TransitionFactory;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeBuilder;


/**
 * This Factory is able to create {@link Node} for specific Activity.
 */
public final class BpmnCustomNodeFactory extends TransitionFactory {

    /**
     * Hidden Constructor.
     */
    private BpmnCustomNodeFactory() {

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

        Activity activityBehavior = new AddNumbersAndStoreActivity(variableName, termsOfSum);

        return BpmnNodeFactory.decorateBpmnDefaultRouting(nodeBuilder).setActivityBehavior(activityBehavior)
        .buildNode();
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

        Activity activityBehavior = new HashComputationActivity(variableName, toBeHashed);

        return BpmnNodeFactory.decorateBpmnDefaultRouting(nodeBuilder).setActivityBehavior(activityBehavior)
        .buildNode();
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

        Activity activityBehavior = new AddContextNumbersAndStoreActivity(variableName,
            summands);

        return BpmnNodeFactory.decorateBpmnDefaultRouting(nodeBuilder).setActivityBehavior(activityBehavior)
        .buildNode();
    }

    /**
     * Creates a {@link Node} that represents the {@link AutomatedDummyActivity}.
     * 
     * It has the default BPMN Incoming- and OutgoingBehaviour as specified
     * {@link BpmnNodeFactory#decorateBpmnDefaultRouting(NodeBuilder) here}.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @param textToBePrinted
     *            - the text that should be printed
     * @return a {@link Node} representing an {@link AutomatedDummyActivity}
     */
    public static Node createBpmnPrintingNode(ProcessDefinitionBuilder builder, String textToBePrinted) {

        NodeBuilder nodeBuilder = builder.getNodeBuilder();

        Activity activityBehavior = new AutomatedDummyActivity(textToBePrinted);

        return BpmnNodeFactory.decorateBpmnDefaultRouting(nodeBuilder).setActivityBehavior(activityBehavior)
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

        Activity activityBehavior = new PrintingVariableActivity(variableToBePrinted);

        return BpmnNodeFactory.decorateBpmnDefaultRouting(nodeBuilder).setActivityBehavior(activityBehavior)
        .buildNode();
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

        Activity activityBehavior = new NullActivity();

        return BpmnNodeFactory.decorateBpmnDefaultRouting(nodeBuilder).setActivityBehavior(activityBehavior)
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

        Activity activityBehavior = new NullActivity();

        return BpmnNodeFactory.decorateBpmnDefaultRouting(nodeBuilder).setActivityBehavior(activityBehavior)
        .buildNode();
    }
}
