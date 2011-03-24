package de.hpi.oryxengine.repository;

import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.process.definition.ProcessDefinition;

/**
 * The Interface ProcessRepository. This is intended to be a singleton. It holds the process definitions that are
 * currently deployed identified by their id.
 */
public interface ProcessRepository {

    /**
     * Gets the definition.
     * 
     * @param id
     *            the id
     * @return the definition
     * @throws DefinitionNotFoundException
     *             thrown, if key id does not exist
     */
    ProcessDefinition getDefinition(UUID id)
    throws DefinitionNotFoundException;

    /**
     * Adds the definition.
     * 
     * @param definition
     *            the definition
     */
    void addDefinition(ProcessDefinition definition);

    /**
     * Delete definition from repository.
     * 
     * @param id
     *            the id
     */
    void deleteDefinition(UUID id);

    /**
     * Gets the definitions.
     * 
     * @return the definitions
     */
    Map<UUID, ProcessDefinition> getDefinitions();

    /**
     * Returns, whether a certain definition is available.
     * 
     * @param id
     *            the definition's id
     * @return true, if available
     */
    boolean containsDefinition(@Nonnull UUID id);
}
