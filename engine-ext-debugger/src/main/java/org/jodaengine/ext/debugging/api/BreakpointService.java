package org.jodaengine.ext.debugging.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jodaengine.ext.debugging.shared.BreakpointImpl;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;

/**
 * The {@link BreakpointService} is a part of the {@link DebuggerService}
 * and allows to manage the list of {@link BreakpointImpl}s.
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
    @Nonnull BreakpointImpl addBreakpoint(@Nonnull Node node);
    
    /**
     * This method allows to add a breakpoint to a certain node and process instance.
     * Providing no instance is equals to calling <code>addBreakpoint(Node node)</code>.
     * 
     * @param node the node, this breakpoint is bound to
     * @param instance the instance, this breakpoint is bound to
     * @return the breakpoint
     */
    @Nonnull BreakpointImpl addBreakpoint(@Nonnull Node node,
                                      @Nullable AbstractProcessInstance instance);
    
    /**
     * Removes the specified breakpoint.
     * 
     * @param breakpoint the breakpoint to remove
     */
    void removeBreakpoint(@Nonnull BreakpointImpl breakpoint);
    
    /**
     * Enables the specified breakpoint.
     * 
     * @param breakpoint the breakpoint to enable
     */
    void enableBreakpoint(@Nonnull BreakpointImpl breakpoint);
    
    /**
     * Disables the specified breakpoint.
     * 
     * @param breakpoint the breakpoint to disable
     */
    void disableBreakpoint(@Nonnull BreakpointImpl breakpoint);
    
    
}
