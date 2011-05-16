package org.jodaengine.exception;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * General engine exception superclass.
 */
public class JodaEngineException extends Exception {
    private static final long serialVersionUID = 1570011344525950859L;

    /**
     * Default constructor for a message only exception.
     * 
     * @param message
     *            the error message
     */
    public JodaEngineException(@Nonnull String message) {

        super(message);
    }

    /**
     * Default constructor for stack trace and message exception.
     * 
     * @param message
     *            the error message
     * @param exception
     *            the underlying exception
     */
    public JodaEngineException(@Nonnull String message, @Nullable Throwable exception) {

        super(message, exception);
    }
}
