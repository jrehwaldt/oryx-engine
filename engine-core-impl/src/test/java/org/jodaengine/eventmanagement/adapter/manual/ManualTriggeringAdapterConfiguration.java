package org.jodaengine.eventmanagement.adapter.manual;

import org.jodaengine.eventmanagement.AdapterManagement;
import org.jodaengine.eventmanagement.adapter.AbstractAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.EventAdapter;
import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.incoming.InboundAdapter;

/**
 * The {@link AdapterConfiguration} for an adapter that can be triggered manually.
 */
public class ManualTriggeringAdapterConfiguration extends AbstractAdapterConfiguration {

    private static final int HASH_CODE = 12345;
    
    /**
     * Default constructor.
     */
    public ManualTriggeringAdapterConfiguration() {

        super(EventTypes.ManualTriggered);
    }

    /**
     * Creates an adapter out of this configuration.
     * 
     * @return an {@link ManualTriggeringAdapter}
     */
    private InboundAdapter createAdapter() {

        ManualTriggeringAdapter manualTriggeringAdapter = new ManualTriggeringAdapter(this);
        return manualTriggeringAdapter;
    }

    @Override
    public EventAdapter registerAdapter(AdapterManagement adapterRegistrar) {

        InboundAdapter adapter = createAdapter();
        adapterRegistrar.registerInboundAdapter(adapter);

        return adapter;
    }
    
//    @Override
//    public int hashCode() {
//
//        return HASH_CODE;
//    }
//    
//    @Override
//    public boolean equals(Object object) {
//
//        if (object == null) {
//            return false;
//        }
//        
//        if (!(object instanceof ManualTriggeringAdapterConfiguration)) {
//            return false;
//        }
//        
//        return true;
//    }
}
