package de.hpi.oryxengine.correlation.adapter;

import java.util.UUID;

import javax.annotation.Nonnull;


/**
 * Adapter types.
 * 
 * @author Jan Rehwaldt
 */
public enum EventTypes
implements EventType {
    Mail,
    Error,
    Timer;
    
    /**
	 * @uml.property  name="id"
	 */
    private final @Nonnull UUID id;
    
    /**
     * Default constructor.
     */
    private EventTypes() {
        this.id = UUID.randomUUID();
    }
    
    @Override
    public @Nonnull UUID getID() {
        return this.id;
    }
    
    @Override
    public @Nonnull String getName() {
        return name();
    }
}
