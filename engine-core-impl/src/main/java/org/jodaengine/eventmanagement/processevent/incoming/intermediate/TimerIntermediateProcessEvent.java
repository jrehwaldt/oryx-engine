package org.jodaengine.eventmanagement.processevent.incoming.intermediate;

import org.jodaengine.eventmanagement.adapter.timer.TimerAdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.incoming.TriggeringBehaviour;
import org.jodaengine.eventmanagement.processevent.incoming.condition.simple.TrueEventCondition;
import org.jodaengine.process.token.Token;

/**
 * The Class {@link TimerIntermediateProcessEvent}.
 */
public class TimerIntermediateProcessEvent extends AbstractIncomingIntermediateProcessEvent {

    /**
     * Instantiates a new {@link TimerIntermediateProcessEvent}.
     * 
     * @param eventWaitingTime
     *            - the time the event is supposed to trigger
     * @param token
     *            - the process token
     */
    public TimerIntermediateProcessEvent(long eventWaitingTime, Token token) {

        super(new TimerAdapterConfiguration(eventWaitingTime), token);
    }

    /**
     * Default Constructor for this event that belongs to a {@link AbstractIntermediateProcessEventGroup}.
     * 
     * @param eventWaitingTime
     *            - the time the event is supposed to trigger
     * @param token
     *            - the {@link Token} that registered this event.
     * @param eventGroup
     *            - if this {@link IncomingIntermediateProcessEvent} is related to other {@link IncomingIntermediateProcessEvent} then
     *            the {@link AbstractIntermediateProcessEventGroup} can be specified here
     */
    public TimerIntermediateProcessEvent(long eventWaitingTime, Token token, TriggeringBehaviour eventGroup) {

        super(new TimerAdapterConfiguration(eventWaitingTime), new TrueEventCondition(), token);
        setTriggeringBehaviour(eventGroup);
    }
}
