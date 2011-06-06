package org.jodaengine.node.factory;

import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.petri.PetriProcessDefinitionBuilder;
import org.jodaengine.process.structure.Condition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;

//TODO: This class is nonsense. Gerardo go 4 refactoring
/**
 * This Factory is able to create {@link Transition Transitions}.
 */
// CHECKSTYLE:OFF
public class PetriTransitionFactory {
// CHECKSTYLE:ON

    /**
     * Creates a {@link Transition} and connects two {@link Nodes} with the created {@link Transition}.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @param source
     *            - the {@link Node SourceNode}
     * @param destination
     *            - the {@link Node DestinationNode}
     * @return a {@link Transition}
     */
    public static Transition createTransitionFromTo(PetriProcessDefinitionBuilder builder, Node source, Node destination) {

        return builder.getTransitionBuilder().transitionGoesFromTo(source, destination).buildTransition();
    }

    /**
     * Creates a {@link Transition} and connects two {@link Nodes} with the created {@link Transition}.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @param source
     *            - the {@link Node SourceNode}
     * @param destination
     *            - the {@link Node DestinationNode}
     * @param condition
     *            - the {@link Condition} that is assigned to this {@link Transition}
     * @return a {@link Transition}
     */
    public static Transition createTransitionFromTo(ProcessDefinitionBuilder builder,
                                                    Node source,
                                                    Node destination,
                                                    Condition condition) {

        return builder.getTransitionBuilder().transitionGoesFromTo(source, destination).setCondition(condition)
        .buildTransition();
    }
}
