package de.hpi.oryxengine.correlation;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.adapter.EventType;


/**
 * The Interface Configuration. The most generic Configuration of an Event.
 */
public interface EventConfiguration {
    
    /**
     * Provides a unique event name. Can be human readable,
     * but cool if done so. Construct through configuration parameters.
     * 
     * @return an unique adapter name
     */
    @Nonnull String getUniqueName();
    
    /**
     * Gets the adapter type.
     * 
     * @return the adapter type
     */
    EventType getEventType();

}
