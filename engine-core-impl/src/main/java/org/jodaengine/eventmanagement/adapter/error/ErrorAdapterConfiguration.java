package org.jodaengine.eventmanagement.adapter.error;

import org.jodaengine.eventmanagement.AdapterRegistrar;
import org.jodaengine.eventmanagement.adapter.AbstractAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.CorrelationAdapter;
import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.InboundAdapter;

/**
 * The error adapter configuration.
 * 
 * @author Jan Rehwaldt
 */
public class ErrorAdapterConfiguration extends AbstractAdapterConfiguration {

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
     * @return the inbound adapter
     */
    private InboundAdapter createAdapter() {

        ErrorAdapter e = new ErrorAdapter(this);
        return e;
    }

    @Override
    public CorrelationAdapter registerAdapter(AdapterRegistrar adapterRegistrar) {

        InboundAdapter adapter = createAdapter();
        adapterRegistrar.registerInboundAdapter(adapter);

        return adapter;
    }
}
