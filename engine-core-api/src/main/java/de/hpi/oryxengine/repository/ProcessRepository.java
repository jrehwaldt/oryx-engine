package de.hpi.oryxengine.repository;

import java.util.Map;
import java.util.UUID;

import de.hpi.oryxengine.process.definition.ProcessDefinition;

/**
 * The Interface ProcessRepository. This is intended to be a singleton.
 */
public interface ProcessRepository {

    /**
     * Gets the definition.
     *
     * @param id the id
     * @return the definition
     * @throws Exception thrown, if key id does not exist
     */
    ProcessDefinition getDefinition(UUID id)
    throws Exception;

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
}
