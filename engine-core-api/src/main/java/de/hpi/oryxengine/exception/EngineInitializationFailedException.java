package de.hpi.oryxengine.exception;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This is a runtime exception stating that the initialization of
 * an engine part failed. Check proper logging if you use this exception.
 * 
 * The error should be severe / fatal and a proper working of the engine not be possible.
 * 
 * Do <b>NOT</b> use for general purposes, because this exception must not be declared as thrown.
 * 
 * @author Jan Rehwaldt
 */
public final class EngineInitializationFailedException extends JodaEngineRuntimeException {
    private static final long serialVersionUID = 6061861852349667735L;
    
    /**
     * Default constructor for a message only exception.
     * 
     * @param message the error message
     */
    public EngineInitializationFailedException(@Nonnull String message) {
        this(message, null);
    }
    
    /**
     * Default constructor for stack trace and message exception.
     * 
     * @param message the error message
     * @param exception the underlying exception
     */
    public EngineInitializationFailedException(@Nonnull String message,
                                               @Nullable Throwable exception) {
        super(message, exception);
    }
}
