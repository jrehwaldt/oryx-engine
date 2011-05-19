package org.jodaengine.exception;

import javax.annotation.Nonnull;

/**
 * Exception implementation for scheduling errors in the {@link TimingManager}.
 * 
 * @author Jan Rehwaldt
 */
public class AdapterSchedulingException extends JodaEngineException {
    private static final long serialVersionUID = 1357635984944880234L;

    /**
     * Default Constructor.
     * 
     * @param exception
     *            the underlying exception
     */
    public AdapterSchedulingException(@Nonnull String errorMessage, @Nonnull Throwable exception) {

        super(errorMessage, exception);
    }
}
