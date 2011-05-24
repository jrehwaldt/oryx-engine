package org.jodaengine.process.activation;

import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionInside;

/**
 * The {@link ProcessDefinitionDeactivationPattern} is responsible for deactivation a {@link ProcessDefinition}. This
 * Pattern is designed to part of a linked list of {@link ProcessDefinitionDeactivationPattern activationPattern}.
 * 
 * The Patterns are initiated when the method
 * {@link ProcessDefinitionInside#deactivate(org.jodaengine.eventmanagement.EventSubscriptionManager)
 * processDefinition.activate(...)} is called.
 */
public interface ProcessDefinitionDeactivationPattern {

    /**
     * Dectivates the {@link ProcessDefinition}.
     * 
     * @param patternContext
     *            - in order to provide further information for this {@link ProcessDefinitionDeactivationPattern
     *            activationPattern}.
     */
    void deactivateProcessDefinition(ProcessDefinitionActivationPatternContext patternContext);
}
