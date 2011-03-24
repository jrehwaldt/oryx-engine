package de.hpi.oryxengine.exception;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * General engine runtime exception superclass.
 */
public class DalmatinaRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -9013077025674998578L;

    /**
     * Default constructor.
     */
    public DalmatinaRuntimeException() {

        super();
    }

    /**
     * Default constructor for a message only exception.
     * 
     * @param message
     *            the error message
     */
    public DalmatinaRuntimeException(@Nonnull String message) {

        this(message, null);
    }

    /**
     * Default constructor for stack trace and message exception.
     * 
     * @param message
     *            the error message
     * @param exception
     *            the underlying exception
     */
    public DalmatinaRuntimeException(@Nonnull String message, @Nullable Throwable exception) {

        super(message, exception);
    }
}
