package de.hpi.oryxengine.process.definition;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.structure.Node;

/**
 * The Interface ProcessBuilder. The process builder is a comfortable way to construct a process definition.
 * 
 * @author Thorben
 */
public interface ProcessDefinitionBuilder {

    @Nonnull
    ProcessDefinition buildDefinition()
    throws IllegalStarteventException;

    @Nonnull
    NodeBuilder getNodeBuilder();

    @Nonnull
    NodeBuilder getStartNodeBuilder();

    @Nonnull
    TransitionBuilder getTransitionBuilder();

    @Nonnull
    ProcessDefinitionBuilder setDescription(String processDescription);

    @Nonnull
    ProcessDefinitionBuilder setName(String processName);

    @Nonnull
    ProcessDefinitionBuilder createStartTrigger(@Nonnull StartEvent event, @Nonnull Node startNode);

    @Nonnull
    ProcessDefinitionBuilder setAttribute(String attributeId, Object attibuteValue);
}
