package org.jodaengine.ext.debugging.util;

import java.util.UUID;

import org.jodaengine.ext.debugging.api.DebuggerCommand;
import org.jodaengine.ext.debugging.api.Interrupter;

/**
 * This {@link Interrupter} interrupts itself.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-01
 */
public class DirectlyInterruptingInterrupter implements Interrupter {
    
    @Override
    public UUID getID() {
        return null;
    }

    @Override
    public DebuggerCommand interruptInstance()
    throws InterruptedException {
        throw new InterruptedException("Ätsch.");
    }

    @Override
    public void releaseInstance(DebuggerCommand command) {
        
    }
    
}
