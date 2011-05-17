package org.jodaengine.eventmanagement.adapter.error;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.adapter.AbstractEvent;


/**
 * An error event.
 * 
 * @author Jan Rehwaldt
 */
public class ErrorAdapterEvent
extends AbstractEvent {
    
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
