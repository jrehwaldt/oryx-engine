package de.hpi.oryxengine.exception;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * General engine exception superclass.
 */
public class DalmatinaException
extends Exception {
    private static final long serialVersionUID = 1570011344525950859L;
    
    /**
     * Default constructor.
     */
    public DalmatinaException() {
        super();
    }
    
    /**
     * Default constructor for a message only exception.
     * 
     * @param message the error message
     */
    public DalmatinaException(@Nonnull String message) {
        super(message);
    }
    
    /**
     * Default constructor for stack trace and message exception.
     * 
     * @param message the error message
     * @param exception the underlying exception
     */
    public DalmatinaException(@Nonnull String message,
                              @Nullable Throwable exception) {
        super(message, exception);
    }
}
