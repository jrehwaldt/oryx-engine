package org.jodaengine.eventmanagement.processevent.incoming.intermediate;

import org.jodaengine.eventmanagement.adapter.manual.ManualTriggeringAdapter;
import org.jodaengine.eventmanagement.adapter.manual.ManualTriggeringAdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.incoming.TriggeringBehavior;
import org.jodaengine.eventmanagement.processevent.incoming.condition.simple.TrueEventCondition;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.AbstractIncomingIntermediateProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IncomingIntermediateProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.triggering.AbstractProcessEventGroup;
import org.jodaengine.eventmanagement.subscription.IncomingProcessEvent;
import org.jodaengine.process.token.Token;

/**
 * It is a {@link IncomingProcessEvent} that can only be triggered manually and not automatically.
 */
public class ProcessIntermediateManualTriggeringEvent extends AbstractIncomingIntermediateProcessEvent {

    /**
     * Default Constructor.
     * 
     * @param name
     *            - the name of the {@link ManualTriggeringAdapter}
     * @param token
     *            - the {@link Token} that registered this event.
     */
    public ProcessIntermediateManualTriggeringEvent(String name, Token token) {

        super(new ManualTriggeringAdapterConfiguration(name), token);
    }

    /**
     * Default Constructor for this event that belongs to a {@link AbstractProcessEventGroup}.
     * 
     * @param name
     *            - the name of the {@link ManualTriggeringAdapter}
     * @param token
     *            - the {@link Token} that registered this event.
     * @param eventGroup
     *            - if this {@link IncomingIntermediateProcessEvent} is related to other
     *            {@link IncomingIntermediateProcessEvent} then
     *            the {@link AbstractProcessEventGroup} can be specified here
     */
    public ProcessIntermediateManualTriggeringEvent(String name, Token token, TriggeringBehavior eventGroup) {

        super(new ManualTriggeringAdapterConfiguration(name), new TrueEventCondition(), token);
        setTriggeringBehaviour(eventGroup);
    }
}
