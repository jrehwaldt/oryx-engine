package de.hpi.oryxengine.correlation.adapter.error;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.adapter.AbstractAdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.AdapterType;

/**
 * The error adapter configuration.
 * 
 * @author Jan Rehwaldt
 */
public class ErrorAdapterConfiguration
extends AbstractAdapterConfiguration {
    
    /**
     * Default constructor.
     * 
     * @param type the adapter's type.
     */
    public ErrorAdapterConfiguration(@Nonnull AdapterType type) {
        super(type);
    }
}
