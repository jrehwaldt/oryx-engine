package org.jodaengine.eventmanagement.adapter.manual;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.adapter.AbstractCorrelatingEventAdapter;
import org.jodaengine.eventmanagement.adapter.incoming.IncomingAdapter;
import org.jodaengine.exception.JodaEngineRuntimeException;

/**
 * This adapter is responsible for exception handling within our engine.
 */
public class ManualTriggeringAdapter extends AbstractCorrelatingEventAdapter<ManualTriggeringAdapterConfiguration>
implements IncomingAdapter {

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

        getManualTriggeringAdapter(manualTriggeringAdapterName).triggerManually();
    }

    /**
     * Gets the {@link ManualTriggeringAdapter} that is associated to the name.
     * 
     * @param manualTriggeringAdapterName
     *            - the name of the {@link ManualTriggeringAdapter} that was stored previously
     * @return the {@link ManualTriggeringAdapter} that is associated to the name
     */
    @Nonnull
    public static ManualTriggeringAdapter getManualTriggeringAdapter(@Nonnull String manualTriggeringAdapterName) {

        ManualTriggeringAdapter triggeringAdapter = manualTriggeringAdapters.get(manualTriggeringAdapterName);

        if (triggeringAdapter == null) {
            String errorMessage = "A ManualTriggeringAdapter '" + manualTriggeringAdapterName + "' does not exists.";
            throw new JodaEngineRuntimeException(errorMessage);
        }

        return triggeringAdapter;
    }

    /**
     * Resets the static variables of the {@link ManualTriggeringAdapter this adapter}.
     */
    public static void resetManualTriggeringAdapter() {

        manualTriggeringAdapters.clear();
    }
}
