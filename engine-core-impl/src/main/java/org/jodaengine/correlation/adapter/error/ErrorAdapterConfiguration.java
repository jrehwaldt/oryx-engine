package org.jodaengine.correlation.adapter.error;

import org.jodaengine.correlation.AdapterRegistrar;
import org.jodaengine.correlation.CorrelationManager;
import org.jodaengine.correlation.adapter.AbstractAdapterConfiguration;
import org.jodaengine.correlation.adapter.CorrelationAdapter;
import org.jodaengine.correlation.adapter.EventTypes;
import org.jodaengine.correlation.adapter.InboundAdapter;


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
    
    /**
     * Creates the error adapter.
     *
     * @param c the Correlation Manager
     * @return the inbound adapter
     */
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
