package org.jodaengine.eventmanagement.processevent.incoming.intermediate;

import org.jodaengine.eventmanagement.adapter.timer.TimerAdapterConfiguration;
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
}
