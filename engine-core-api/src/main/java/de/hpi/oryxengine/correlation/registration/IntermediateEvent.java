package de.hpi.oryxengine.correlation.registration;

import java.util.UUID;

/**
 * The Interface IntermediateEvent. An intermediate event, in contrast to the startEvent will not trigger the
 * instantiation of a new process, but a notification to a registered process instance will be sent.
 */
public interface IntermediateEvent extends ProcessEvent {
    
    /**
     * Gets the assigned token that has be notified if timer is complete.
     * 
     * @return the definition id
     */
    UUID getTokenID();

}
