package de.hpi.oryxengine.process.definition;

import de.hpi.oryxengine.correlation.registration.StartEvent;

/**
 * The Interface StartNodeParameter.
 */
public interface StartNodeParameter extends NodeParameter {

    /**
     * Sets the start event.
     *
     * @param event the new start event
     */
    void setStartEvent(StartEvent event);
    
    /**
     * Gets the start event.
     *
     * @return the start event
     */
    StartEvent getStartEvent();
}
