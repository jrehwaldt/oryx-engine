package org.jodaengine.eventmanagement.adapter.error;

import org.jodaengine.eventmanagement.AdapterRegistrar;
import org.jodaengine.eventmanagement.CorrelationManager;
import org.jodaengine.eventmanagement.adapter.AbstractAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.CorrelationAdapter;
import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.InboundAdapter;
import org.jodaengine.exception.AdapterSchedulingException;


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
     * @param correlationService the correlation service needed to correlate events from the adapters 
     * @return the inbound adapter
     */
    private InboundAdapter createAdapter(CorrelationManager correlationService) {
        ErrorAdapter e = new ErrorAdapter(correlationService, this);
        return e;
    }


    @Override
    public CorrelationAdapter registerAdapter(AdapterRegistrar adapterRegistrar, CorrelationManager correlationService)
    throws AdapterSchedulingException {

        InboundAdapter adapter = createAdapter(correlationService);
        adapterRegistrar.registerAdapter(adapter);
        
        return adapter;
    }


}
