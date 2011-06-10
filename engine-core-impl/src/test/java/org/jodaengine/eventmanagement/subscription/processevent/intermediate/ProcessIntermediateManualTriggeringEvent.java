package org.jodaengine.eventmanagement.subscription.processevent.intermediate;

import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.manual.ManualTriggeringAdapter;
import org.jodaengine.eventmanagement.adapter.manual.ManualTriggeringAdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.ProcessEvent;
import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.eventmanagement.subscription.TriggeringBehaviour;
import org.jodaengine.eventmanagement.subscription.condition.simple.TrueEventCondition;
import org.jodaengine.eventmanagement.subscription.processeventgroup.intermediate.AbstractProcessIntermediateEventGroup;
import org.jodaengine.process.token.Token;

/**
 * It is a {@link ProcessEvent} that can only be triggered manually and not automatically.
 */
public class ProcessIntermediateManualTriggeringEvent extends AbstractProcessIntermediateEvent {

    /**
     * Default Constructor.
     * 
     * @param name
     *            - the name of the {@link ManualTriggeringAdapter}
     * @param token
     *            - the {@link Token} that registered this event.
     */
    public ProcessIntermediateManualTriggeringEvent(String name, Token token) {

        super(EventTypes.ManualTriggered, new ManualTriggeringAdapterConfiguration(name), token);
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

        super(EventTypes.ManualTriggered, new ManualTriggeringAdapterConfiguration(name), new TrueEventCondition(),
            token, eventGroup);
    }
}
