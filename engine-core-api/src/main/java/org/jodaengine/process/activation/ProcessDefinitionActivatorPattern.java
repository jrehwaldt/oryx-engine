package org.jodaengine.process.activation;

import org.jodaengine.util.PatternAppendable;

/**
 * The {@link ProcessDefinitionActivatorPattern} is responsible for activation a {@link ProcessDefinition}.
 * 
 * <p>
 * This Pattern is designed to part of a linked list of {@link ProcessDefinitionActivatorPattern activationPattern}.
 * </p>
 */
public interface ProcessDefinitionActivatorPattern extends ProcessDefinitionDeactivationPattern,
ProcessDefinitionActivationPattern, PatternAppendable<ProcessDefinitionActivatorPattern> {

}
