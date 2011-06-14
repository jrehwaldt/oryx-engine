package org.jodaengine.eventmanagement.subscription.processevent.intermediate;

import org.jodaengine.eventmanagement.adapter.manual.ManualTriggeringAdapter;
import org.jodaengine.eventmanagement.adapter.manual.ManualTriggeringAdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.incoming.TriggeringBehaviour;
import org.jodaengine.eventmanagement.processevent.incoming.condition.simple.TrueEventCondition;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.AbstractIncomingProcessIntermediateEvent;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.processeventgroup.AbstractProcessIntermediateEventGroup;
import org.jodaengine.eventmanagement.subscription.IntermediateProcessEvent;
import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.process.token.Token;

/**
 * It is a {@link IntermediateProcessEvent} that can only be triggered manually and not automatically.
 */
public class ProcessIntermediateManualTriggeringEvent extends AbstractIncomingProcessIntermediateEvent {

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
     * Default Constructor for this event that belongs to a {@link AbstractProcessIntermediateEventGroup}.
     * 
     * @param name
     *            - the name of the {@link ManualTriggeringAdapter}
     * @param token
     *            - the {@link Token} that registered this event.
     * @param eventGroup
     *            - if this {@link ProcessIntermediateEvent} is related to other {@link ProcessIntermediateEvent} then
     *            the {@link AbstractProcessIntermediateEventGroup} can be specified here
     */
    public ProcessIntermediateManualTriggeringEvent(String name, Token token, TriggeringBehaviour eventGroup) {

        super(new ManualTriggeringAdapterConfiguration(name), new TrueEventCondition(), token,
            eventGroup);
    }
}
