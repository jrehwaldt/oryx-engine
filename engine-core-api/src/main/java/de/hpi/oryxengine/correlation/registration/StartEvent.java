package de.hpi.oryxengine.correlation.registration;

import java.util.UUID;

/**
 * The Interface StartEvent. A start event, in contrast to the intermediate event, will trigger the instantiation of a
 * process.
 */
public interface StartEvent extends ProcessEvent {

    /**
     * Gets the assigned process definition that is to be instantiated upon event occurrence.
     * 
     * @return the definition id
     */
    UUID getDefinitionID();
}
