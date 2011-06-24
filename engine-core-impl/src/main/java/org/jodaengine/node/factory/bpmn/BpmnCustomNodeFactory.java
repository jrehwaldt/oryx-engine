package org.jodaengine.node.factory.bpmn;

import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.activity.NullActivity;
import org.jodaengine.node.activity.custom.AddContextNumbersAndStoreActivity;
import org.jodaengine.node.activity.custom.AddNumbersAndStoreActivity;
import org.jodaengine.node.activity.custom.AutomatedDummyActivity;
import org.jodaengine.node.activity.custom.HashComputationActivity;
import org.jodaengine.node.activity.custom.PrintingVariableActivity;
import org.jodaengine.node.activity.custom.TweetActivity;
import org.jodaengine.node.activity.custom.TweetEndEventActivity;
import org.jodaengine.node.factory.ControlFlowFactory;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.EmptyOutgoingBehaviour;
import org.jodaengine.process.definition.bpmn.BpmnProcessDefinitionBuilder;
import org.jodaengine.process.structure.Node;

/**
 * This Factory is able to create {@link Node} for specific Activity.
 */
public final class BpmnCustomNodeFactory extends ControlFlowFactory {

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
    public static Node createBpmnAddNumbersAndStoreNode(BpmnProcessDefinitionBuilder builder,
                                                        String variableName,
                                                        int[] termsOfSum) {

        Activity activityBehavior = new AddNumbersAndStoreActivity(variableName, termsOfSum);

        return createDefaultBpmnNodeWith(builder, activityBehavior);
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
    public static Node createBpmnHashComputationNode(BpmnProcessDefinitionBuilder builder,
                                                     String variableName,
                                                     String toBeHashed) {

        Activity activityBehavior = new HashComputationActivity(variableName, toBeHashed);

        return createDefaultBpmnNodeWith(builder, activityBehavior);
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
    public static Node createBpmnAddContextNumbersAndStoreNode(BpmnProcessDefinitionBuilder builder,
                                                               String variableName,
                                                               String... summands) {

        Activity activityBehavior = new AddContextNumbersAndStoreActivity(variableName, summands);

        return createDefaultBpmnNodeWith(builder, activityBehavior);
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
    public static Node createBpmnPrintingNode(BpmnProcessDefinitionBuilder builder, String textToBePrinted) {

        Activity activityBehavior = new AutomatedDummyActivity(textToBePrinted);

        return createDefaultBpmnNodeWith(builder, activityBehavior);
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
    public static Node createBpmnPrintingVariableNode(BpmnProcessDefinitionBuilder builder, 
                                                      String variableToBePrinted) {

        Activity activityBehavior = new PrintingVariableActivity(variableToBePrinted);

        return createDefaultBpmnNodeWith(builder, activityBehavior);
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
    public static Node createBpmnNullStartNode(BpmnProcessDefinitionBuilder builder) {

        Activity activityBehavior = new NullActivity();

        Node bpmnNullStartNode = createDefaultBpmnNodeWith(builder, activityBehavior);
        builder.addNodeAsStartNode(bpmnNullStartNode);

        return bpmnNullStartNode;
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
    public static Node createBpmnNullNode(BpmnProcessDefinitionBuilder builder) {

        Activity activityBehavior = new NullActivity();

        return createDefaultBpmnNodeWith(builder, activityBehavior);
    }

    /**
     * Creating a freaking twitter node.
     * 
     * @param builder
     *            the builder
     * @return the node
     */
    public static Node createTwitterEndEventNode(BpmnProcessDefinitionBuilder builder) {

        Activity activityBehavior = new TweetEndEventActivity();
        return builder.getNodeBuilder().setIncomingBehaviour(new SimpleJoinBehaviour())
        .setOutgoingBehaviour(new EmptyOutgoingBehaviour()).setActivityBehavior(activityBehavior).buildNode();
    }
    
    
    /**
     * Creates a new Tweetnode.
     *
     * @param builder the BPMN Process Definition Builder
     * @param message the message to tweet
     * @param pathToProperties the path to the properties file, this must contain the oauth security tokens
     * @return the node
     */
    public static Node createTweetNode(BpmnProcessDefinitionBuilder builder, 
                                       String message, 
                                       String pathToProperties) {      
        Activity activityBehavior = new TweetActivity(message, pathToProperties);
        
        
        return createDefaultBpmnNodeWith(builder, activityBehavior);
   }
}
