package org.jodaengine.eventmanagement.processevent.incoming.intermediate;

import org.jodaengine.eventmanagement.adapter.phidget.IncomingIFKitAdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.process.token.Token;

/**
 * The Class IntermediateIncomingOpenIFKitProcessEvent.
 */
public class IntermediateIncomingOpenIFKitProcessEvent extends AbstractIncomingIntermediateProcessEvent {

    /**
     * Instantiates a new intermediate incoming open if kit process event.
     *
     * @param config the config
     * @param condition the condition
     * @param token the token
     */
    public IntermediateIncomingOpenIFKitProcessEvent(IncomingIFKitAdapterConfiguration config,
                                                        EventCondition condition,
                                                        Token token) {

        super(config, condition, token);
    }

}
