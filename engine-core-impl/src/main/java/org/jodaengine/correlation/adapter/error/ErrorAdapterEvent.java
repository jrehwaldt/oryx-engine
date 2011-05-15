package org.jodaengine.correlation.adapter.error;

import org.jodaengine.correlation.adapter.AbstractAdapterEvent;

import javax.annotation.Nonnull;


/**
 * An error event.
 * 
 * @author Jan Rehwaldt
 */
public class ErrorAdapterEvent
extends AbstractAdapterEvent {
    
    /**
     * Default hidden constructor.
     * 
     * @param configuration the firing adapter's {@link ErrorAdapterConfiguration}
     * @param exception the {@link Throwable}, which occurred
     */
    ErrorAdapterEvent(@Nonnull ErrorAdapterConfiguration configuration,
                      @Nonnull Throwable exception) {
        super(configuration);
    }
}
