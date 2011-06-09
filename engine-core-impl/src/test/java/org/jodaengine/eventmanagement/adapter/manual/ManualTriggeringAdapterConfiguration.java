package org.jodaengine.eventmanagement.adapter.manual;

import org.jodaengine.eventmanagement.AdapterManagement;
import org.jodaengine.eventmanagement.adapter.AbstractAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.EventAdapter;
import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.incoming.IncomingAdapter;

/**
 * The {@link AdapterConfiguration} for an adapter that can be triggered manually.
 */
public class ManualTriggeringAdapterConfiguration extends AbstractAdapterConfiguration {

    private String name;

    /**
     * Default constructor.
     * 
     * @param name
     *            the name of the {@link ManualTriggeringAdapter}
     */
    public ManualTriggeringAdapterConfiguration(String name) {

        super(EventTypes.ManualTriggered);
        this.name = name;
    }

    /**
     * Gets the name of the {@link ManualTriggeringAdapter}.
     * 
     * @return a {@link String} representing the name of the {@link ManualTriggeringAdapter}
     */
    public String getName() {

        return name;
    }

    /**
     * Creates an adapter out of this configuration.
     * 
     * @return an {@link ManualTriggeringAdapter}
     */
    private IncomingAdapter createAdapter() {

        ManualTriggeringAdapter manualTriggeringAdapter = new ManualTriggeringAdapter(this);
        return manualTriggeringAdapter;
    }

    @Override
    public EventAdapter registerAdapter(AdapterManagement adapterRegistrar) {

        IncomingAdapter adapter = createAdapter();
        adapterRegistrar.registerInboundAdapter(adapter);

        return adapter;
    }

    @Override
    public int hashCode() {

        return name.hashCode();
    }

    @Override
    public boolean equals(Object object) {

        if (object == null) {
            return false;
        }

        if (!(object instanceof ManualTriggeringAdapterConfiguration)) {
            return false;
        }

        ManualTriggeringAdapterConfiguration manualTriggeringAdapterConfigToCompare = 
            (ManualTriggeringAdapterConfiguration) object;
        return name.equals(manualTriggeringAdapterConfigToCompare.getName());
    }
}
