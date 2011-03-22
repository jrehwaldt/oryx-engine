package de.hpi.oryxengine.process.structure;

import de.hpi.oryxengine.correlation.registration.StartEvent;

/**
 * The Interface StartNode. This is used to have start nodes associated with events, for example for a BPMN Message
 * Start Event
 * 
 * @author thorben
 */
public interface StartNode extends Node {

    /**
     * Gets the start event.
     * 
     * @return the start event
     */
    StartEvent getStartEvent();
}
