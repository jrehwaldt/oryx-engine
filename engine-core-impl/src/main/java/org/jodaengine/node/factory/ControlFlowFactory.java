package org.jodaengine.node.factory;

import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.bpmn.BpmnProcessDefinitionBuilder;
import org.jodaengine.process.structure.Condition;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeBuilder;

/**
 * This Factory is able to create {@link ControlFlow}s.
 */
// CHECKSTYLE:OFF
public class ControlFlowFactory {
// CHECKSTYLE:ON
    /**
     * Creates a {@link ControlFlow} and connects two {@link Nodes} with the created {@link ControlFlow}.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @param source
     *            - the {@link Node SourceNode}
     * @param destination
     *            - the {@link Node DestinationNode}
     * @return a {@link ControlFlow}
     */
    public static ControlFlow createControlFlowFromTo(ProcessDefinitionBuilder builder, Node source, Node destination) {

        return builder.getControlFlowBuilder().controlFlowGoesFromTo(source, destination).buildControlFlow();
    }

    /**
     * Creates a {@link ControlFlow} and connects two {@link Nodes} with the created {@link ControlFlow}.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @param source
     *            - the {@link Node SourceNode}
     * @param destination
     *            - the {@link Node DestinationNode}
     * @param condition
     *            - the {@link Condition} that is assigned to this {@link ControlFlow}
     * @return a {@link ControlFlow}
     */
    public static ControlFlow createControlFlowFromTo(ProcessDefinitionBuilder builder,
                                                    Node source,
                                                    Node destination,
                                                    Condition condition) {

        return builder.getControlFlowBuilder().controlFlowGoesFromTo(source, destination).setCondition(condition)
        .buildControlFlow();
    }

    /**
     * A helper that just takes the activity and creates a BPMN Node with default routing behaviour.
     *
     * @param builder the builder
     * @param activityBehavior the activity behavior
     * @return the node
     */
    protected static Node createDefaultBpmnNodeWith(BpmnProcessDefinitionBuilder builder, Activity activityBehavior) {
    
        NodeBuilder nodeBuilder = builder.getNodeBuilder();
        
        return BpmnNodeFactory.decorateBpmnDefaultRouting(nodeBuilder).setActivityBehavior(activityBehavior)
        .buildNode();
    }
}
