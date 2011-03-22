package de.hpi.oryxengine.correlation.registration;

import java.util.UUID;

/**
 * The Interface StartEvent.
 */
public interface StartEvent extends ProcessEvent {

    /**
     * Gets the assigned process definition that is to be instantiated upon event occurence.
     * 
     * @return the definition id
     */
    UUID getDefinitionID();
}
