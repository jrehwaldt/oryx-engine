package org.jodaengine.process.activation;

import org.jodaengine.process.definition.ProcessDefinitionInside;
import org.jodaengine.util.ServiceContext;

/**
 * Extends the {@link ServiceContext} with new methods especially for the {@link ProcessDefinitionActivationPattern}
 * -Chain.
 */
public interface ProcessDefinitionActivationPatternContext extends ServiceContext {

    /**
     * Gets the {@link ProcessDefinitionInside processDefinition}.
     * 
     * @return the {@link ProcessDefinitionInside}
     */
    ProcessDefinitionInside getProcessDefinition();
}
