package org.jodaengine.deployment;

import org.jodaengine.process.definition.ProcessDefinition;

/**
 * A {@link ProcessDefinitionImporter} is capable of importing a {@link ProcessDefinition} from any source.
 */
public interface ProcessDefinitionImporter {

    /**
     * This method creates a {@link ProcessDefinition}.
     * 
     * @return the translated {@link ProcessDefinition}
     */
    ProcessDefinition createProcessDefinition();
}
