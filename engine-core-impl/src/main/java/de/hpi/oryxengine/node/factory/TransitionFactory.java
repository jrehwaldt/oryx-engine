package de.hpi.oryxengine.node.factory;

import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;

/**
 * This Factory is able to create {@link Transition Transitions}.
 */
public class TransitionFactory {


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
    public static Transition createTransitionFromTo(ProcessDefinitionBuilder builder, Node source, Node destination) {

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
