package de.hpi.oryxengine.correlation.adapter.error;

import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.correlation.adapter.AbstractAdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.EventTypes;
import de.hpi.oryxengine.correlation.adapter.CorrelationAdapter;

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
        super(EventTypes.Error);
    }

    @Override
    public String getUniqueName() {
        return "Error";
    }
    
    @Override
    public CorrelationAdapter createAdapter(CorrelationManager c) {
        ErrorAdapter e = new ErrorAdapter(c, this);
        return e;
    }


}
