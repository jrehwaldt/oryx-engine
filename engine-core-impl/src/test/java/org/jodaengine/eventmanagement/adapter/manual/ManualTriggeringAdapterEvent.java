package org.jodaengine.eventmanagement.adapter.manual;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.adapter.AbstractAdapterEvent;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;

/**
 * An {@link AdapterEvent} that is triggered manually.
 * 
 * @author Jan Rehwaldt
 */
public class ManualTriggeringAdapterEvent extends AbstractAdapterEvent {

    /**
     * Default hidden constructor.
     * 
     * @param configuration
     *            - the firing adapter's {@link ManualTriggeringAdapterConfiguration}
     */
    public ManualTriggeringAdapterEvent(@Nonnull AdapterConfiguration configuration) {

        super(configuration);
    }
}
