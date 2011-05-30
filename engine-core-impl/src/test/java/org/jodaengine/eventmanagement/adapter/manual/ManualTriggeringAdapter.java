package org.jodaengine.eventmanagement.adapter.manual;

import java.util.HashMap;
import java.util.Map;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.adapter.AbstractCorrelatingEventAdapter;
import org.jodaengine.eventmanagement.adapter.incoming.InboundAdapter;

/**
 * This adapter is responsible for exception handling within our engine.
 */
public class ManualTriggeringAdapter extends AbstractCorrelatingEventAdapter<ManualTriggeringAdapterConfiguration>
implements InboundAdapter {

    private static Map<String, ManualTriggeringAdapter> manualTriggeringAdapters =
        new HashMap<String, ManualTriggeringAdapter>();

    /**
     * Default Constructor.
     * 
     * @param configuration
     *            - the configuration of this adapter
     */
    public ManualTriggeringAdapter(ManualTriggeringAdapterConfiguration configuration) {

        super(configuration);
        manualTriggeringAdapters.put(configuration.getName(), this);
    }

    /**
     * This method is called in order to trigger manually.
     */
    private void triggerManually() {

        AdapterEvent adapterEvent = new ManualTriggeringAdapterEvent(configuration);
        correlateAdapterEvent(adapterEvent);
    }

    /**
     * This method is called in order to trigger manually {@link ManualTriggeringAdapter this adapter}. Afterwards the
     * {@link ManualTriggeringAdapterEvent} is correlated.
     * 
     * @param manualTriggeringAdapterName
     *            - the name of the {@link ManualTriggeringAdapter} that was stored previously
     */
    public static void triggerManually(String manualTriggeringAdapterName) {

        ManualTriggeringAdapter manualTriggeringAdapter = manualTriggeringAdapters.get(manualTriggeringAdapterName);
        manualTriggeringAdapter.triggerManually();
    }

    /**
     * Resets the static variables of the {@link ManualTriggeringAdapter this adapter}.
     */
    public static void resetManualTriggeringAdapter() {

        manualTriggeringAdapters.clear();
    }
}
