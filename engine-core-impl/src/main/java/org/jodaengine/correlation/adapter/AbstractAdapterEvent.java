package org.jodaengine.correlation.adapter;

import org.jodaengine.correlation.AdapterEvent;

import java.util.Date;

import javax.annotation.Nonnull;


/**
 * Abstract event representation.
 * 
 * @author Jan Rehwaldt
 */
public abstract class AbstractAdapterEvent
implements AdapterEvent {
    
    private final Date timestamp;

    private final AdapterConfiguration configuration;
    
    /**
     * Default constructor.
     * 
     * @param configuration the firing adapter's {@link AdapterConfiguration}
     */
    public AbstractAdapterEvent(@Nonnull AdapterConfiguration configuration) {
        this.timestamp = new Date();
        this.configuration = configuration;
    }
    
    @Override
    public AdapterConfiguration getAdapterConfiguration() {
        return this.configuration;
    }
    
    @Override
    public final Date getTimestamp() {
        return this.timestamp;
    }
    
    @Override
    public EventType getAdapterType() {
        return this.configuration.getEventType();
    }
}
