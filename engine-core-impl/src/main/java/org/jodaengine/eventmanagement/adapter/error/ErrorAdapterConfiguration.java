package org.jodaengine.eventmanagement.adapter.error;

import org.jodaengine.eventmanagement.AdapterManagement;
import org.jodaengine.eventmanagement.adapter.AbstractAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.EventAdapter;
import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.incoming.InboundAdapter;

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
    public EventAdapter registerAdapter(AdapterManagement adapterRegistrar) {

        InboundAdapter adapter = createAdapter();
        adapterRegistrar.registerInboundAdapter(adapter);

        return adapter;
    }
}
