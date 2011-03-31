package de.hpi.oryxengine.exception;

import javax.annotation.Nonnull;

/**
 * Exception implementation for scheduling errors in the {@link TimingManager}.
 * 
 * @author Jan Rehwaldt
 */
public class AdapterSchedulingException
extends DalmatinaException {
    private static final long serialVersionUID = 1357635984944880234L;
    
    private static final String DEFAULT_EXCEPTION_MESSAGE = "Unable to schedule the pulling adapter.";
 
    /**
     * Default Constructor.
     * 
     * @param exception the underlying exception
     */
    public AdapterSchedulingException(@Nonnull Throwable exception) {
        super(DEFAULT_EXCEPTION_MESSAGE, exception);
    }
}
