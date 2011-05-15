package org.jodaengine.deployment.importer;

import org.jodaengine.deployment.ProcessDefinitionImporter;
import org.jodaengine.process.definition.ProcessDefinition;

import javax.annotation.Nonnull;


/**
 * The {@link RawProcessDefintionImporter} is capable of deploying a raw {@link ProcessDefinition}. The
 * {@link ProcessDefinition} object is not created in this class.
 */
public class RawProcessDefintionImporter implements ProcessDefinitionImporter {

    private ProcessDefinition processDefinition;

    /**
     * Instantiates the {@link RawProcessDefintionImporter}.
     * 
     * @param processDefinition
     *            - the {@link ProcessDefinition} that needs to be imported
     */
    public RawProcessDefintionImporter(@Nonnull ProcessDefinition processDefinition) {

        this.processDefinition = processDefinition;
    }

    @Override
    public ProcessDefinition createProcessDefinition() {

        return processDefinition;
    }
}
