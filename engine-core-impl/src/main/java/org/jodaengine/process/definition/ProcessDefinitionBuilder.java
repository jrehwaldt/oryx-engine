package org.jodaengine.process.definition;

import javax.annotation.Nonnull;

import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.structure.ControlFlowBuilder;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeBuilder;

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
     * Creates a {@link ControlFlowBuilder} in order to customize and build a {@link ControlFlow}.
     * 
     * @return a {@link ControlFlowBuilder}
     */
    @Nonnull
    ControlFlowBuilder getControlFlowBuilder();
}
