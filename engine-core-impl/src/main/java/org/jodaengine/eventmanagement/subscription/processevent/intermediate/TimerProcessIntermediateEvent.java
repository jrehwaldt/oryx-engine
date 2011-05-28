package org.jodaengine.eventmanagement.subscription.processevent.intermediate;

import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.timer.TimerAdapterConfiguration;
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

        super(EventTypes.Timer, new TimerAdapterConfiguration(eventWaitingTime), new TrueEventCondition(), token);
    }
}
