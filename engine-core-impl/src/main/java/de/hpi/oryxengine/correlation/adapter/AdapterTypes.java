package de.hpi.oryxengine.correlation.adapter;

import java.util.UUID;

import javax.annotation.Nonnull;


/**
 * Adapter types.
 * 
 * @author Jan Rehwaldt
 */
public enum AdapterTypes
implements AdapterType {
    Mail,
    Error;
    
    private final @Nonnull UUID id;
    
    /**
     * Default constructor.
     */
    private AdapterTypes() {
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
