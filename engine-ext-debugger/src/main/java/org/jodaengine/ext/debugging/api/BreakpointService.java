package org.jodaengine.ext.debugging.api;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;

/**
 * The {@link BreakpointService} is a part of the {@link DebuggerService}
 * and allows to manage the list of {@link Breakpoint}s.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-17
 */
public interface BreakpointService {
    
    /**
     * This method allows to add a {@link Breakpoint} to a certain {@link Node}.
     * 
     * Optionally a condition may be specified
     * 
     * @param targetDefinition the definition, this breakpoint is bound to
     * @param targetNode the node this breakpoint is bound to
     * @param targetActivityState the activity state this breakpoint is bound to
     * @param juelCondition an optional juel-based condition
     * @return the breakpoint
     * 
     * @throws DefinitionNotFoundException thrown if the {@link ProcessDefinition} or {@link Node} could not be found
     */
    @Nonnull Breakpoint createBreakpoint(@Nonnull ProcessDefinition targetDefinition,
                                         @Nonnull Node targetNode,
                                         @Nonnull ActivityState targetActivityState,
                                         @Nullable String juelCondition)
    throws DefinitionNotFoundException;
    
    /**
     * Removes the specified {@link Breakpoint}.
     * 
     * @param breakpoint the breakpoint to remove
     * @return true, if the breakpoint was found and removed
     */
    boolean removeBreakpoint(@Nonnull Breakpoint breakpoint);
    
    /**
     * Enables the specified {@link Breakpoint}.
     * 
     * @param breakpoint the breakpoint to enable
     * @return the enabled breakpoint
     */
    Breakpoint enableBreakpoint(@Nonnull Breakpoint breakpoint);
    
    /**
     * Disables the specified {@link Breakpoint}.
     * 
     * @param breakpoint the breakpoint to disable
     * @return the enabled breakpoint
     */
    Breakpoint disableBreakpoint(@Nonnull Breakpoint breakpoint);
    
    /**
     * Returns a list of all known {@link Breakpoint}s.
     * 
     * @return a list of all known breakpoints
     */
    @Nonnull Collection<Breakpoint> getAllBreakpoints();
}
