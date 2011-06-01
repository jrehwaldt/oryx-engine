package org.jodaengine.ext.debugging.api;

import javax.annotation.Nonnull;

/**
 * This interface provides basic methods for signaling
 * an interruption and it's continuing.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-01
 */
public interface Interrupter {
    
    /**
     * This method interrupts underlying instance.
     * 
     * @return the {@link DebuggerCommand}, how the process instance should be continued
     */
    @Nonnull DebuggerCommand interrupt();
    
    /**
     * This method resumes the underlying instance.
     * 
     * @param command the {@link DebuggerCommand}, how the process instance should be continued
     */
    void continueInstance(@Nonnull DebuggerCommand command);
}
