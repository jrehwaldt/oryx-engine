package org.jodaengine.eventmanagement.adapter;

import java.util.Date;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.adapter.AdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.EventType;


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
