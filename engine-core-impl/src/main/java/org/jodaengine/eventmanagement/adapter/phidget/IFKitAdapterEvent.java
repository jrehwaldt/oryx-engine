package org.jodaengine.eventmanagement.adapter.phidget;

import org.jodaengine.eventmanagement.adapter.AbstractAdapterEvent;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;

/**
 * The Class OpenIFKitAdapterEvent.
 */
public class IFKitAdapterEvent extends AbstractAdapterEvent {

    private int value;
    private int port;
    
    /**
     * Instantiates a new openIFKit adapter event.
     *
     * @param value the value of the sensor, ranges from 0 to 1000
     * @param port the port of the InterfaceKit the event occured on
     * @param configuration the configuration
     */
    public IFKitAdapterEvent(int value, int port, AdapterConfiguration configuration) {

        super(configuration);
        this.value = value;
        this.port = port;
    }

    public int getValue() {
    
        return value;
    }

    public int getPort() {
    
        return port;
    }

}
