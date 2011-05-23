package org.jodaengine.eventmanagement.subscription;

import org.jodaengine.process.definition.ProcessDefinitionID;

/**
 * The Interface StartEvent. A start event, in contrast to the intermediate event, will trigger the instantiation of a
 * process.
 */
public interface ProcessStartEvent extends ProcessEvent {

    /**
     * Gets the assigned process definition that is to be instantiated upon event occurrence.
     * 
     * @return the definition id
     */
    ProcessDefinitionID getDefinitionID();
}
