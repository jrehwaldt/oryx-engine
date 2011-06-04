package org.jodaengine.ext.debugging.api;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.jodaengine.bootstrap.Service;


/**
 * The {@link DebuggerService} is a debugging extension, which provides several plugin implementations 
 * for debugging business processes.
 * 
 * This is the debugging service part, which provides control over {@link InterruptedInstance}s.
 * Service methods for handling breakpoints are defined within {@link BreakpointService}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-17
 */
public interface DebuggerService extends BreakpointService, DebuggerArtifactService, Service {
    
    String DEBUGGER_SERVICE_NAME = "engine-ext-debugger";
    
    /**
     * This method will release the {@link InterruptedInstance} and step over to the next process' state.
     * 
     * @param instance the instance, to release
     */
    void stepOverInstance(@Nonnull InterruptedInstance instance);
    
    /**
     * This method will terminate the {@link InterruptedInstance}. The instance will be removed.
     * 
     * @param instance the instance, to release
     */
    void terminateInstance(@Nonnull InterruptedInstance instance);
    
    /**
     * This method will release the {@link InterruptedInstance} and continues until it reaches the process' end.
     * 
     * Further {@link Breakpoint}s will not be taken into consideration.
     * 
     * @param instance the instance, to release
     */
    void resumeInstance(@Nonnull InterruptedInstance instance);
    
    /**
     * This method will release the {@link InterruptedInstance} and continues until it
     * <ul>
     * <li>
     *   a) will reach a {@link Breakpoint}
     * </li>
     * <li>
     * or
     *   b) reaches the process' end.
     * </li>
     * </ul>
     * 
     * @param instance the instance, to release
     */
    void continueInstance(@Nonnull InterruptedInstance instance);
    
    /**
     * Provides a list of all {@link InterruptedInstance}, which describes the
     * interrupted {@link org.jodaengine.process.token.Token} and it's {@link Breakpoint}.
     * 
     * @return a list of all interrupted instances
     */
    @Nonnull Collection<InterruptedInstance> getInterruptedInstances();
    
}
