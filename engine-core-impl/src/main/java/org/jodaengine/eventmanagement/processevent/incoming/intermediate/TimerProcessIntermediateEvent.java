package org.jodaengine.eventmanagement.processevent.incoming.intermediate;

import org.jodaengine.eventmanagement.adapter.timer.TimerAdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.incoming.TriggeringBehaviour;
import org.jodaengine.eventmanagement.processevent.incoming.condition.simple.TrueEventCondition;
import org.jodaengine.process.token.Token;

/**
 * The Class {@link TimerProcessIntermediateEvent}.
 */
public class TimerProcessIntermediateEvent extends AbstractIncomingProcessIntermediateEvent {

    /**
     * Instantiates a new {@link TimerProcessIntermediateEvent}.
     * 
     * @param eventWaitingTime
     *            - the time the event is supposed to trigger
     * @param token
     *            - the process token
     */
    public TimerProcessIntermediateEvent(long eventWaitingTime, Token token) {

        super(new TimerAdapterConfiguration(eventWaitingTime), token);
    }

    /**
     * Default Constructor for this event that belongs to a {@link AbstractProcessIntermediateEventGroup}.
     * 
     * @param eventWaitingTime
     *            - the time the event is supposed to trigger
     * @param token
     *            - the {@link Token} that registered this event.
     * @param eventGroup
     *            - if this {@link ProcessIntermediateEvent} is related to other {@link ProcessIntermediateEvent} then
     *            the {@link AbstractProcessIntermediateEventGroup} can be specified here
     */
    public TimerProcessIntermediateEvent(long eventWaitingTime, Token token, TriggeringBehaviour eventGroup) {

        super(new TimerAdapterConfiguration(eventWaitingTime), new TrueEventCondition(), token, eventGroup);
    }
}
