package org.jodaengine.process.activation;

import org.jodaengine.util.PatternAppendable;

/**
 * The {@link ProcessDefinitionActivationPattern} is responsible for activation a {@link ProcessDefinition}. This
 * Pattern is designed to part of a linked list of {@link ProcessDefinitionActivationPattern activationPattern}.
 * 
 * The Patterns are initiated when the method
 * {@link ProcessDefinitionInside#activate(org.jodaengine.eventmanagement.EventSubscriptionManager)
 * processDefinition.activate(...)} is called.
 */
public interface ProcessDefinitionActivationPattern extends PatternAppendable<ProcessDefinitionActivationPattern> {

    /**
     * Activates the {@link ProcessDefinition}.
     * 
     * @param patternContext
     *            - in order to provide further information for this {@link ProcessDefinitionActivationPattern
     *            activationPattern}.
     */
    void activateProcessDefinition(ProcessDefinitionActivationPatternContext patternContext);
}
