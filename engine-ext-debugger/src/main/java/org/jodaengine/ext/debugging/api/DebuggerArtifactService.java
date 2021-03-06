package org.jodaengine.ext.debugging.api;

import javax.annotation.Nonnull;

import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.process.definition.ProcessDefinition;

/**
 * The {@link DebuggerArtifactService} is a part of the {@link DebuggerService}
 * and allows to get access to svg artifacts, which are internally hold as {@link AbstractProcessArtifact}
 * and bound to {@link ProcessDefinition}s.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-26
 */
public interface DebuggerArtifactService {
    
    String DEBUGGER_ARTIFACT_NAMESPACE = "debugger/";
    
    /**
     * Provides access to the svg resources bound to a certain process via {@link ProcessDefinition}.
     * 
     * @param definition the process definition
     * @return a string representation for the requested svg artifact
     * @throws ProcessArtifactNotFoundException in case the artifact is not found
     * @throws DefinitionNotFoundException in case the definition is not found
     */
    @Nonnull String getSvgArtifact(@Nonnull ProcessDefinition definition)
    throws ProcessArtifactNotFoundException, DefinitionNotFoundException;
    
    /**
     * Sets the provided svg artifact for a {@link ProcessDefinition}.
     * 
     * @param definition the process definition, the artifact belongs to
     * @param svgArtifact the svg artifact to set
     * @throws DefinitionNotFoundException in case the definition (scope) could not be found
     */
    void setSvgArtifact(@Nonnull ProcessDefinition definition,
                        @Nonnull String svgArtifact)
    throws DefinitionNotFoundException;
}
