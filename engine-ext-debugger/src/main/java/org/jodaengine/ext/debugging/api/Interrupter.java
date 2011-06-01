package org.jodaengine.ext.debugging.api;

import java.util.UUID;

import javax.annotation.Nonnull;

import org.jodaengine.util.Identifiable;

/**
 * This interface provides basic methods for signaling
 * an interruption and it's continuing.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-01
 */
public interface Interrupter extends Identifiable<UUID> {
    
    /**
     * This method interrupts underlying instance.
     * 
     * @return the {@link DebuggerCommand}, how the process instance should be continued
     * @throws InterruptedException cause through external interruption of this {@link Thread}
     */
    @Nonnull DebuggerCommand interrupt() throws InterruptedException;
    
    /**
     * This method resumes the underlying instance.
     * 
     * @param command the {@link DebuggerCommand}, how the process instance should be continued
     */
    void continueInstance(@Nonnull DebuggerCommand command);
}
