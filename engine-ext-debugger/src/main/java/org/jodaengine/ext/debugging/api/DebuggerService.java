package org.jodaengine.ext.debugging.api;

import javax.annotation.Nonnull;

import org.jodaengine.bootstrap.Service;
import org.jodaengine.ext.debugging.Breakpoint;
import org.jodaengine.process.instance.AbstractProcessInstance;


/**
 * The {@link DebuggerService} is a debugging extension, which provides several plugin implementations 
 * for debugging business processes.
 * 
 * This is the debugging service part, which provides control over paused {@link AbstractProcessInstance}s.
 * {@link Breakpoint} service methods are defined within {@link BreakpointService}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-17
 */
public interface DebuggerService extends BreakpointService, Service {
    
    String DEBUGGER_SERVICE_NAME = "engine-ext-debugger";
    
    /**
     * This method will release the {@link AbstractProcessInstance} and step over to the next process' state.
     * 
     * @param instance the instance, to release
     */
    void stepOverInstance(@Nonnull AbstractProcessInstance instance);
    
    /**
     * This method will terminate the {@link AbstractProcessInstance}. The instance will be removed.
     * 
     * @param instance the instance, to release
     */
    void termianteInstance(@Nonnull AbstractProcessInstance instance);
    
    /**
     * This method will release the {@link AbstractProcessInstance} and continues until it reaches the process' end.
     * 
     * Further {@link Breakpoint}s will not be taken into consideration.
     * 
     * @param instance the instance, to release
     */
    void resumeInstance(@Nonnull AbstractProcessInstance instance);
    
    /**
     * This method will release the {@link AbstractProcessInstance} and continues until it
     *   a) will reach a {@link Breakpoint}
     * or
     *   b) reaches the process' end.
     * 
     * @param instance the instance, to release
     */
    void continueInstance(@Nonnull AbstractProcessInstance instance);
}
