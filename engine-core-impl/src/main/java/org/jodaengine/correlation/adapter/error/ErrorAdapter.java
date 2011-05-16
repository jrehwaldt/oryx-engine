package org.jodaengine.correlation.adapter.error;

import javax.annotation.Nonnull;

import org.jodaengine.correlation.CorrelationManager;
import org.jodaengine.correlation.adapter.AbstractCorrelationAdapter;
import org.jodaengine.correlation.adapter.InboundAdapter;


/**
 * This adapter is responsible for exception handling within our engine.
 * 
 * @author Jan Rehwaldt
 */
public class ErrorAdapter
extends AbstractCorrelationAdapter<ErrorAdapterConfiguration>
implements InboundAdapter {
    
    /**
     * Default constructor.
     * 
     * @param correlation the correlation manager
     * @param configuration the configuration
     */
    public ErrorAdapter(@Nonnull CorrelationManager correlation,
                        @Nonnull ErrorAdapterConfiguration configuration) {
        super(correlation, configuration);
    }
    
    /**
     * This method is internally called if an {@link Exception} occurred,
     * which may be correlated with the process.
     * 
     * @param message a message describing the error
     * @param exception the {@link Throwable}, which occurred
     */
    public void exceptionOccured(@Nonnull String message,
                                 @Nonnull Throwable exception) {
        logger.error(message, exception);
        correlate(new ErrorAdapterEvent(this.configuration, exception));
    }
}
