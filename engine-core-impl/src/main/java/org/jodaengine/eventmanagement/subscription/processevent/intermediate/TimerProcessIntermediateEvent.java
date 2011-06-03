package org.jodaengine.eventmanagement.subscription.processevent.intermediate;

import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.timer.TimerAdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.ProcessEventGroup;
import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.eventmanagement.subscription.condition.simple.TrueEventCondition;
import org.jodaengine.process.token.Token;

/**
 * The Class {@link TimerProcessIntermediateEvent}.
 */
public class TimerProcessIntermediateEvent extends AbstractProcessIntermediateEvent {

    /**
     * Instantiates a new {@link TimerProcessIntermediateEvent}.
     * 
     * @param eventWaitingTime
     *            - the time the event is supposed to trigger
     * @param token
     *            - the process token
     */
    public TimerProcessIntermediateEvent(long eventWaitingTime, Token token) {

        this(eventWaitingTime, token, null);
    }

    /**
     * Default Constructor for this event that belongs to a {@link ProcessEventGroup}.
     * 
     * @param eventWaitingTime
     *            - the time the event is supposed to trigger
     * @param token
     *            - the {@link Token} that registered this event.
     * @param eventGroup
     *            - if this {@link ProcessIntermediateEvent} is related to other {@link ProcessIntermediateEvent} then
     *            the {@link ProcessEventGroup} can be specified here
     */
    public TimerProcessIntermediateEvent(long eventWaitingTime, Token token, ProcessEventGroup eventGroup) {

        super(EventTypes.Timer, new TimerAdapterConfiguration(eventWaitingTime), new TrueEventCondition(), token,
            eventGroup);
    }
}
