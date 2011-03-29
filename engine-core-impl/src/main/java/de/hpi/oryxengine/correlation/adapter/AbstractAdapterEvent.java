package de.hpi.oryxengine.correlation.adapter;

import java.util.Date;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.AdapterEvent;

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
    public AdapterType getAdapterType() {
        return this.configuration.getAdapterType();
    }
}
