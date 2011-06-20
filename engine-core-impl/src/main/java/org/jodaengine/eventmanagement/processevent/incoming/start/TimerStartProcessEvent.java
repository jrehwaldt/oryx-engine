package org.jodaengine.eventmanagement.processevent.incoming.start;

import org.jodaengine.eventmanagement.adapter.timer.TimerAdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.incoming.IncomingStartProcessEvent;
import org.jodaengine.process.definition.ProcessDefinitionID;

/**
 * It is a {@link IncomingStartProcessEvent} that represents a time triggered start event.
 */
public class TimerStartProcessEvent extends BaseIncomingStartProcessEvent {

    /**
     * Default constructor for an {@link TimerStartProcessEvent}.
     * 
     * @param eventWaitingTime
     *            - the time the event is supposed to trigger
     * 
     * @param definitionID
     *            - the id to the {@link ProcessDefinitionID} in order to start the process when this
     *            {@link BaseIncomingStartProcessEvent} triggers
     */
    public TimerStartProcessEvent(long eventWaitingTime, ProcessDefinitionID definitionID) {

        super(new TimerAdapterConfiguration(eventWaitingTime, true), definitionID);
    }
}
