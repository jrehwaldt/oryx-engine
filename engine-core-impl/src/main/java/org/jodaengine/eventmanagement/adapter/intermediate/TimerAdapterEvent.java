package org.jodaengine.eventmanagement.adapter.intermediate;

import org.jodaengine.eventmanagement.adapter.AbstractAdapterEvent;
import org.jodaengine.eventmanagement.adapter.AdapterConfiguration;


/**
 * The Class TimerAdapterEvent.
 */
public class TimerAdapterEvent extends AbstractAdapterEvent {

    /**
     * Instantiates a new timer adapter event.
     *
     * @param configuration the configuration
     */
    public TimerAdapterEvent(AdapterConfiguration configuration) {

        super(configuration);
    }

}
