package org.jodaengine.eventmanagement.adapter.phidget;

import com.phidgets.event.SensorChangeEvent;
import com.phidgets.event.SensorChangeListener;

/**
 * The listener interface for receiving jodaSensorChange events.
 * The class that is interested in processing a jodaSensorChange
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addJodaSensorChangeListener</code> method. When
 * the jodaSensorChange event occurs, that object's appropriate
 * method is invoked.
 *
 * @see JodaSensorChangeEvent
 */
public class JodaSensorChangeListener implements SensorChangeListener {

    /** The adapter. */
    private IncomingOpenIFKitAdapter adapter;
    
    /**
     * Instantiates a new joda sensor change listener.
     *
     * @param adapter the adapter
     */
    public JodaSensorChangeListener(IncomingOpenIFKitAdapter adapter) {
        this.adapter = adapter;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void sensorChanged(SensorChangeEvent event) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Lol rofl lol" + event);
        adapter.updateFromSensor(event);
    }

}
