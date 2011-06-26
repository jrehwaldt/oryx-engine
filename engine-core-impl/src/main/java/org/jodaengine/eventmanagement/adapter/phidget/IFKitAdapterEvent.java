package org.jodaengine.eventmanagement.adapter.phidget;

import org.jodaengine.eventmanagement.adapter.AbstractAdapterEvent;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;

/**
 * The Class OpenIFKitAdapterEvent.
 */
public class IFKitAdapterEvent extends AbstractAdapterEvent {

    private int value;
    private int channel;
    
    /**
     * Instantiates a new openIFKit adapter event.
     *
     * @param value the value of the sensor, ranges from 0 to 1000
     * @param channel the channel the event occured on
     * @param configuration the configuration
     */
    public IFKitAdapterEvent(int value, int channel, AdapterConfiguration configuration) {

        super(configuration);
        this.value = value;
        this.channel = channel;
    }

    public int getValue() {
    
        return value;
    }

    public int getChannel() {
    
        return channel;
    }

}
