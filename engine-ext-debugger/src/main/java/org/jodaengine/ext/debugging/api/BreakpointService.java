package org.jodaengine.ext.debugging.api;

import javax.annotation.Nonnull;

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
     * This method allows to add a breakpoint to a certain node.
     * 
     * @param node the node, this breakpoint is bound to
     * @return the breakpoint
     */
    @Nonnull Breakpoint createBreakpoint(@Nonnull Node node);
    
    /**
     * Removes the specified breakpoint.
     * 
     * @param breakpoint the breakpoint to remove
     */
    void removeBreakpoint(@Nonnull Breakpoint breakpoint);
    
    /**
     * Enables the specified breakpoint.
     * 
     * @param breakpoint the breakpoint to enable
     */
    void enableBreakpoint(@Nonnull Breakpoint breakpoint);
    
    /**
     * Disables the specified breakpoint.
     * 
     * @param breakpoint the breakpoint to disable
     */
    void disableBreakpoint(@Nonnull Breakpoint breakpoint);
    
    
}
