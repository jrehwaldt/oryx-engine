package org.jodaengine.eventmanagement.adapter.phidget;

import org.jodaengine.eventmanagement.adapter.AbstractCorrelatingEventAdapter;
import org.jodaengine.eventmanagement.adapter.incoming.IncomingAdapter;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.SensorChangeEvent;

/**
 * The Class IncomingOpenIFKitAdapter.
 */
public class IncomingOpenIFKitAdapter extends AbstractCorrelatingEventAdapter<IncomingOpenIFKitAdapterConfiguration>
implements IncomingAdapter {

    /** The interface kit phidget. */
    private InterfaceKitPhidget interfaceKitPhidget;

    /**
     * Instantiates a new incoming open if kit adapter.
     * 
     * @param configuration
     *            the configuration
     */
    public IncomingOpenIFKitAdapter(IncomingOpenIFKitAdapterConfiguration configuration) {

        super(configuration);
        try {
            interfaceKitPhidget = new InterfaceKitPhidget();
        } catch (PhidgetException e) {
            logger.error("Couldn't instatniate a new Phidget class");
        }
        prepareKit();
    }

    /**
     * Prepare kit.
     */
    private void prepareKit() {

        interfaceKitPhidget.addSensorChangeListener(new JodaSensorChangeListener(this));
        try {
            interfaceKitPhidget.openAny();
        } catch (PhidgetException e) {
            logger.error("Could not open the connection to the OpenIFKit for some reason, is it really attached?");
        }
        try {
            interfaceKitPhidget.openAny();
        } catch (PhidgetException e) {
            logger.error("wwwwwwwwwwwwooooooooaaaaaaahhhhhhhhhhhhhhhhhhhhh no open any bitch!");
        }

    }

    /**
     * Update from sensor.
     * 
     * @param event
     *            the event
     */
    public void updateFromSensor(SensorChangeEvent event) {

        OpenIFKitAdapterEvent adapterEvent = new OpenIFKitAdapterEvent(event.getValue(), event.getIndex(),
            this.getConfiguration());
        this.correlate(adapterEvent);
    }

}
