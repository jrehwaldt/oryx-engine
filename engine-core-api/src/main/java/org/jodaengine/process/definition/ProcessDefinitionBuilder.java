package org.jodaengine.process.definition;

import java.util.List;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.adapter.EventType;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.instantiation.InstantiationPattern;
import org.jodaengine.process.instantiation.StartInstantiationPattern;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeBuilder;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.process.structure.TransitionBuilder;
import org.jodaengine.process.token.BPMNToken;


/**
 * The Interface ProcessBuilder. The process builder is a comfortable way to construct a process definition.
 */
public interface ProcessDefinitionBuilder {

    /**
     * Gets the definition as the result of the building process.
     * 
     * @return the process definition
     * @throws IllegalStarteventException
     *             the exception for an illegal start event
     */
    @Nonnull
    ProcessDefinition buildDefinition()
    throws IllegalStarteventException;

    /**
     * Creates a {@link NodeBuilder} in order to customize and build a {@link Node}.
     * 
     * @return a {@link NodeBuilder}
     */
    @Nonnull
    NodeBuilder getNodeBuilder();

    /**
     * Creates a {@link NodeBuilder} in order to customize and build a {@link Node StartNode}. So a {@link Node} that is
     * created by this {@link NodeBuilder Builder} is a {@link Node StartNode}.
     * 
     * @return a {@link NodeBuilder}
     */
    @Nonnull
    NodeBuilder getStartNodeBuilder();

    /**
     * Creates a {@link TransitionBuilder} in order to customize and build a {@link Transition}.
     * 
     * @return a {@link TransitionBuilder}
     */
    @Nonnull
    TransitionBuilder getTransitionBuilder();

    /**
     * Sets the description.
     * 
     * @param processDescription
     *            - the description of the {@link ProcessDefinition}
     * @return the {@link ProcessDefinitionBuilder} in order to keep on building the {@link ProcessDefinition}
     */
    @Nonnull
    ProcessDefinitionBuilder setDescription(String processDescription);

    /**
     * Sets the name of the {@link ProcessDefinition} to build.
     * 
     * @param processName
     *            - the name of the {@link ProcessDefinition}
     * @return the {@link ProcessDefinitionBuilder} in order to keep on building the {@link ProcessDefinition}
     */
    @Nonnull
    ProcessDefinitionBuilder setName(String processName);

    /**
     * This will create a start trigger for the {@link ProcessDefinition}.
     * 
     * @param eventType
     *            - the {@link EventType}
     * @param adapterConfig
     *            - the {@link AdapterConfiguration}
     * @param eventConditions
     *            - the {@link EventCondition}
     * @param startNode
     *            - the {@link Node startNode}
     * @return the {@link ProcessDefinitionBuilder} in order to keep on building the {@link ProcessDefinition}
     * @throws JodaEngineRuntimeException
     *             thrown if the provided node isn't a startNode.
     */
    @Nonnull
    ProcessDefinitionBuilder createStartTrigger(@Nonnull EventType eventType,
                                                @Nonnull AdapterConfiguration adapterConfig,
                                                @Nonnull List<EventCondition> eventConditions,
                                                @Nonnull Node<BPMNToken> startNode);

    /**
     * In order to store dynamic attributes to the {@link ProcessDefinition}.
     * 
     * @param attributeId
     *            - the id of the attribute
     * @param attibuteValue
     *            - value of the attribute
     * @return the {@link ProcessDefinitionBuilder} in order to keep on building the {@link ProcessDefinition}
     */
    @Nonnull
    ProcessDefinitionBuilder setAttribute(String attributeId, Object attibuteValue);

    // TODO @Gerardo Comminting!!!!
    ProcessDefinitionBuilder addInstantiationPattern(InstantiationPattern instantiationPattern);

    ProcessDefinitionBuilder addStartInstantiationPattern(StartInstantiationPattern startInstantiationPattern);
}
