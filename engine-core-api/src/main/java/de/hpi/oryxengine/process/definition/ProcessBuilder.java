package de.hpi.oryxengine.process.definition;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.StartNode;

/**
 * The Interface ProcessBuilder. The process builder is a comfortable way to construct a process definition.
 * @author thorben
 */
public interface ProcessBuilder {

    /**
     * Gets the definition as the result of the building process.
     *
     * @return the definition
     */
    @Nonnull ProcessDefinition buildDefinition();
    
    /**
     * Creates a new node with the given parameters.
     *
     * @param param the param
     * @return the node
     */
    @Nonnull Node createNode(NodeParameter param);
    
    /**
     * Creates a new start node with the given parameters.
     *
     * @param param the param
     * @return the node
     */
    @Nonnull StartNode createStartNode(StartNodeParameter param);
    
    /**
     * Creates the transition.
     *
     * @param source the source
     * @param destination the destination
     * @return the process builder
     */
    @Nonnull ProcessBuilder createTransition(@Nonnull Node source,
                                             @Nonnull Node destination);
    
    /**
     * Creates the transition.
     *
     * @param source the source
     * @param destination the destination
     * @param condition the condition
     * @return the process builder
     */
    @Nonnull ProcessBuilder createTransition(@Nonnull Node source,
                                             @Nonnull Node destination,
                                             @Nonnull Condition condition);
    
    /**
     * Sets the iD.
     *
     * @param id the new ID
     * @return the process builder
     */
    @Nonnull ProcessBuilder setID(@Nonnull UUID id);
    
    /**
     * Sets the description.
     *
     * @param description the new description
     * @return the process builder
     */
    @Nonnull ProcessBuilder setDescription(@Nullable String description);
}
