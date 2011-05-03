package de.hpi.oryxengine.correlation.adapter.error;

import de.hpi.oryxengine.correlation.AdapterRegistrar;
import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.correlation.adapter.AbstractAdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.CorrelationAdapter;
import de.hpi.oryxengine.correlation.adapter.EventTypes;
import de.hpi.oryxengine.correlation.adapter.InboundAdapter;

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
    
    private InboundAdapter createAdapter(CorrelationManager c) {
        ErrorAdapter e = new ErrorAdapter(c, this);
        return e;
    }

    @Override
    public CorrelationAdapter registerAdapter(AdapterRegistrar adapterRegistrar) {
        
        InboundAdapter adapter = createAdapter(adapterRegistrar);
        adapterRegistrar.registerAdapter(adapter);
        
        return adapter;
    }


}
