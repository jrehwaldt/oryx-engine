package org.jodaengine.eventmanagement.adapter.error;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.adapter.AbstractCorrelatingEventAdapter;
import org.jodaengine.eventmanagement.adapter.incoming.IncomingAdapter;

/**
 * This adapter is responsible for exception handling within our engine.
 */
public class ErrorAdapter extends AbstractCorrelatingEventAdapter<ErrorAdapterConfiguration> implements IncomingAdapter {

    /**
     * Default constructor.
     * 
     * @param configuration
     *            - the configuration of this adapter
     */
    public ErrorAdapter(@Nonnull ErrorAdapterConfiguration configuration) {

        super(configuration);
    }

    /**
     * This method is internally called if an {@link Exception} occurred,
     * which may be correlated with the process.
     * 
     * @param message
     *            a message describing the error
     * @param exception
     *            the {@link Throwable}, which occurred
     */
    public void exceptionOccured(@Nonnull String message, @Nonnull Throwable exception) {

        logger.error(message, exception);
        correlate(new ErrorAdapterEvent(this.configuration, exception));
    }
}
