package de.hpi.oryxengine.correlation;

import de.hpi.oryxengine.util.Identifiable;

/**
 * The Event Type which are supported by our adapters,
 * such as E-Mail or Telnet.
 */
public interface EventType extends Identifiable {

    /**
     * Gets a human readable name of the Event (such as "E-Mail").
     * 
     * @return the event type's name
     */
    String getName();
}
