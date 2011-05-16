package org.jodaengine.factory.definition;

import java.util.UUID;

import org.jodaengine.process.definition.ProcessDefinition;


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
