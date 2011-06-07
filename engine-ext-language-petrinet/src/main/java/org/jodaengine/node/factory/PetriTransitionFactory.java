package org.jodaengine.node.factory;


import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.petri.PetriProcessDefinitionBuilder;
import org.jodaengine.process.structure.Condition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.ControlFlow;

//TODO: This class is nonsense. Gerardo go 4 refactoring
/**
 * This Factory is able to create {@link ControlFlow}s.
 */
// CHECKSTYLE:OFF
public class PetriTransitionFactory {
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
    public static ControlFlow createControlFlowFromTo(PetriProcessDefinitionBuilder builder, Node source, Node destination) {

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
}
