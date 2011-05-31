package org.jodaengine.ext.debugging.api;

import javax.annotation.Nonnull;

import org.jodaengine.bootstrap.Service;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;

/**
 * This {@link Service} helps to resolve dereferenced instances,
 * such as a {@link ProcessDefinition} or a {@link Node}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-31
 */
public interface ReferenceResolverService extends Service {
    
    String RESOLVER_SERVICE_NAME = "engine-ext-resolver";
    
    /**
     * Searches for a reference of a {@link Node} and returns it, if available.
     * 
     * @param definition the {@link ProcessDefinition}, this node belongs to
     * @param dereferencedNode the dereferenced {@link Node}
     * @return the referenced {@link Node}
     */
    @Nonnull Node resolveNode(@Nonnull ProcessDefinition definition,
                              @Nonnull Node dereferencedNode);
    
    /**
     * Searches for a reference of a {@link ProcessDefinition} and returns it, if available.
     * 
     * @param dereferencedDefinition the dereferenced {@link ProcessDefinition}
     * @return the referenced {@link ProcessDefinition}
     * 
     * @throws DefinitionNotFoundException if the {@link ProcessDefinition} could not be found
     */
    @Nonnull ProcessDefinition resolveDefinition(ProcessDefinition dereferencedDefinition)
    throws DefinitionNotFoundException;

    
    /**
     * Searches for a reference of a {@link Breakpoint} and returns it, if available.
     * 
     * @param dereferencedBreakpoint the dereferenced {@link Breakpoint}
     * @return the referenced {@link Breakpoint}
     */
    @Nonnull Breakpoint resolveBreakpoint(@Nonnull Breakpoint dereferencedBreakpoint);
    
}
