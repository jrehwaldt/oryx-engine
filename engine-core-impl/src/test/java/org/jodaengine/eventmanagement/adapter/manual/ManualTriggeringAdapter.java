package org.jodaengine.eventmanagement.adapter.manual;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.adapter.AbstractCorrelatingEventAdapter;
import org.jodaengine.eventmanagement.adapter.incoming.InboundAdapter;

/**
 * This adapter is responsible for exception handling within our engine.
 */
public class ManualTriggeringAdapter extends AbstractCorrelatingEventAdapter<ManualTriggeringAdapterConfiguration>
implements InboundAdapter {

    private static List<ManualTriggeringAdapter> manualTriggeringAdapters = new ArrayList<ManualTriggeringAdapter>();

    /**
     * Default Constructor.
     * 
     * @param configuration
     *            - the configuration of this adapter
     */
    public ManualTriggeringAdapter(ManualTriggeringAdapterConfiguration configuration) {

        super(configuration);
        manualTriggeringAdapters.add(this);
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
     * @param manualTriggeringAdapterIndex
     *            - the index of the {@link ManualTriggeringAdapter} (is basically the order in which the adapter have
     *            been created); the first manualAdapter gets the index 0, the seond manualadapter gets the index 1, ...
     */
    public static void triggerManually(int manualTriggeringAdapterIndex) {

        ManualTriggeringAdapter manualTriggeringAdapter = manualTriggeringAdapters.get(manualTriggeringAdapterIndex);
        manualTriggeringAdapter.triggerManually();
    }
    
    public static void resetManualTriggeringAdapter() {
        manualTriggeringAdapters = new ArrayList<ManualTriggeringAdapter>();
    }
}
