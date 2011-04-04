package de.hpi.oryxengine.correlation.adapter.error;

import de.hpi.oryxengine.correlation.adapter.AbstractAdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.AdapterTypes;

/**
 * The error adapter configuration.
 * 
 * @author Jan Rehwaldt
 */
public class ErrorAdapterConfiguration
extends AbstractAdapterConfiguration {
    
    /**
     * Default constructor.
     */
    public ErrorAdapterConfiguration() {
        super(AdapterTypes.Error);
    }

    @Override
    public String getUniqueName() {
        return "Error";
    }


}
