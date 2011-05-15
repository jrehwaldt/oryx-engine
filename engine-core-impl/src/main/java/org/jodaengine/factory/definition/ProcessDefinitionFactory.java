package org.jodaengine.factory.definition;

import org.jodaengine.process.definition.ProcessDefinition;

import java.util.UUID;


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
