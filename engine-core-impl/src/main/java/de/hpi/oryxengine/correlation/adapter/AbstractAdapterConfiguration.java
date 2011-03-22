package de.hpi.oryxengine.correlation.adapter;

import javax.annotation.Nonnull;


/**
 * Abstract adapter configuration.
 * 
 * @author Jan Rehwaldt
 */
public abstract class AbstractAdapterConfiguration
implements AdapterConfiguration {
    
    private final AdapterType type;
    
    /**
     * Default constructor.
     * 
     * @param type the adapter's type.
     */
    public AbstractAdapterConfiguration(@Nonnull AdapterType type) {
        this.type = type;
    }
    
    @Override
    public final @Nonnull AdapterType getAdapterType() {
        return type;
    }
}
