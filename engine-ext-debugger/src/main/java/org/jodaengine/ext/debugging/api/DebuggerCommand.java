package org.jodaengine.ext.debugging.api;


/**
 * Those are the available debugger commands to continue an interrupted process.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-01
 */
public enum DebuggerCommand {
    
    /**
     * <i>Step over</i> will release the process and step over to the next process' state.
     */
    STEP_OVER,
    
    /**
     * <i>Terminate</i> will terminate the process. The instance will be removed.
     */
    TERMINATE,
    
    /**
     * <i>Resume</i> will release the process and continue until it reaches the process' end.
     * 
     * Further {@link Breakpoint}s will not be taken into consideration.
     */
    RESUME,
    
    /**
     * <i>Continue</i> will release the process and continue until it
     * <ul>
     * <li>
     *   a) will reach a {@link Breakpoint}
     * </li>
     * <li>
     * or
     *   b) reaches the process' end.
     * </li>
     * </ul>
     */
    CONTINUE;
}
