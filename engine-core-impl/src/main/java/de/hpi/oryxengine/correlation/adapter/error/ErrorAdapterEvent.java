package de.hpi.oryxengine.correlation.adapter.error;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.adapter.AbstractAdapterEvent;

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
