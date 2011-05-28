package org.jodaengine.eventmanagement.subscription;

import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.timer.TimerAdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.condition.simple.TrueEventCondition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;

/**
 * The Class {@link TimerEventImpl}.
 */
public class TimerEventImpl extends ProcessIntermediateEventBase {

    /**
     * Instantiates a new {@link TimerEventImpl}.
     * 
     * @param eventWaitingTime
     *            - the time the event is supposed to trigger
     * @param token
     *            - the process token
     */
    public TimerEventImpl(long eventWaitingTime, Token token) {

        super(EventTypes.Timer, new TimerAdapterConfiguration(eventWaitingTime), new TrueEventCondition(), token);
    }
}
