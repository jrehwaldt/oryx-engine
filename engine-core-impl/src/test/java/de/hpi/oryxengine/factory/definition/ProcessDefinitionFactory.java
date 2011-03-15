package de.hpi.oryxengine.factory.definition;

import java.util.UUID;

import de.hpi.oryxengine.process.definition.ProcessDefinition;

/**
 * A factory for creating ProcessDefinition objects.
 */
public interface ProcessDefinitionFactory {

    /**
     * Creates the.
     *
     * @param definitionID the definition id
     * @return the process definition
     */
    ProcessDefinition create(UUID definitionID);
}
