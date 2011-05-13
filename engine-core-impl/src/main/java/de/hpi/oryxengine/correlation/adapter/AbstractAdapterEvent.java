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
    
    /**
	 * @uml.property  name="timestamp"
	 */
    private final Date timestamp;
    /**
	 * @uml.property  name="configuration"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
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
    
    /**
	 * @return
	 * @uml.property  name="timestamp"
	 */
    @Override
    public final Date getTimestamp() {
        return this.timestamp;
    }
    
    @Override
    public EventType getAdapterType() {
        return this.configuration.getEventType();
    }
}
