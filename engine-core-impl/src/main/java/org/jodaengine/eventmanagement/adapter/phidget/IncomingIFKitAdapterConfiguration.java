package org.jodaengine.eventmanagement.adapter.phidget;

import org.jodaengine.eventmanagement.AdapterManagement;
import org.jodaengine.eventmanagement.adapter.AbstractAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.EventAdapter;
import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.incoming.IncomingAdapter;

/**
 * The Class IncomingOpenIFKitAdapterConfiguration.
 */
public class IncomingIFKitAdapterConfiguration extends AbstractAdapterConfiguration {
    
    /**
     * Instantiates a new incoming open if kit adapter configuration.   *
     */
    public IncomingIFKitAdapterConfiguration() {

        super(EventTypes.Phidget);
    }
    
    @Override
    public EventAdapter registerAdapter(AdapterManagement adapterRegistrar) {

        IncomingAdapter adapter = createAdapter();
        return adapter;
    }
    
    /**
     * Creates the adapter suitable for this configuration.
     *
     * @return the incoming adapter
     */
    private IncomingAdapter createAdapter() {
        return new IncomingIFKitAdapter(this);
    }

}
