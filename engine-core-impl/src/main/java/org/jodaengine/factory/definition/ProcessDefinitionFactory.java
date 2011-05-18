package org.jodaengine.factory.definition;

import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;


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
    ProcessDefinition create(ProcessDefinitionID definitionID);
}
