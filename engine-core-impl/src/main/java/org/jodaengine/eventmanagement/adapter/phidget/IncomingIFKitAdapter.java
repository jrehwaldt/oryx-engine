package org.jodaengine.eventmanagement.adapter.phidget;

import org.jodaengine.eventmanagement.adapter.AbstractCorrelatingEventAdapter;
import org.jodaengine.eventmanagement.adapter.incoming.IncomingAdapter;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.SensorChangeEvent;

/**
 * The Class IncomingOpenIFKitAdapter.
 */
public class IncomingIFKitAdapter extends AbstractCorrelatingEventAdapter<IncomingIFKitAdapterConfiguration>
implements IncomingAdapter {

    /** The interface kit phidget. */
    private InterfaceKitPhidget interfaceKitPhidget;

    /**
     * Instantiates a new incoming open if kit adapter.
     * 
     * @param configuration
     *            the configuration
     */
    public IncomingIFKitAdapter(IncomingIFKitAdapterConfiguration configuration) {

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
    }

    /**
     * Update from sensor.
     * 
     * @param event
     *            the event
     */
    public void updateFromSensor(SensorChangeEvent event) {
        IFKitAdapterEvent adapterEvent = new IFKitAdapterEvent(event.getValue(), event.getIndex(),
            this.getConfiguration());
        this.correlate(adapterEvent);
    }

}
