package org.jodaengine.eventmanagement.subscription;

import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.manual.ManualTriggeringAdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.condition.simple.TrueEventCondition;
import org.jodaengine.process.token.Token;

/**
 * It is a {@link ProcessEvent} that can only be triggered manually and not automatically.
 */
public class ProcessIntermediateManualTriggeringEvent extends ProcessIntermediateEventBase {

    /**
     * Default Constructor.
     * 
     * @param token
     *            - the {@link Token} that registered this event.
     */
    public ProcessIntermediateManualTriggeringEvent(Token token) {

        super(EventTypes.ManualTriggered, new ManualTriggeringAdapterConfiguration(), new TrueEventCondition(), token);
    }
}
