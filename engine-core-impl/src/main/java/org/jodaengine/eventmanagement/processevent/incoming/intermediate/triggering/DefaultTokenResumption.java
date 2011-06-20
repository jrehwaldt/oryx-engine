package org.jodaengine.eventmanagement.processevent.incoming.intermediate.triggering;

import org.jodaengine.eventmanagement.processevent.incoming.TriggeringBehavior;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.AbstractIncomingIntermediateProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IncomingIntermediateProcessEvent;
import org.jodaengine.eventmanagement.subscription.IncomingProcessEvent;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the default {@link TriggeringBehavior} for an {@link AbstractIncomingIntermediateProcessEvent}. It calls the
 * {@link Token} in order to resume it start.
 */
public class DefaultTokenResumption implements TriggeringBehavior {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void trigger(IncomingProcessEvent processEvent) {

        if (!(processEvent instanceof IncomingIntermediateProcessEvent)) {
            String errorMessage = "The triggeringBehavior of the processEvent '" + processEvent
                + "'is supposed to be attached only to incomingIntermediateProcessEvent.";
            logger.error(errorMessage);
            throw new JodaEngineRuntimeException(errorMessage);
        }

        doingTokenResumption((IncomingIntermediateProcessEvent) processEvent);
    }

    /**
     * Encapsulates the resumption of the {@link Token}.
     * 
     * @param incomingIntermediateProcessEvent
     *            - the {@link IncomingIntermediateProcessEvent} that triggers the token resumption
     */
    private void doingTokenResumption(IncomingIntermediateProcessEvent incomingIntermediateProcessEvent) {

        logger.debug("Starting the resumption of the token for event {}", incomingIntermediateProcessEvent);
        
        // Resuming the token with myself
        incomingIntermediateProcessEvent.getToken().resume(incomingIntermediateProcessEvent);
        
        logger.info("Token has been resumed for event {}", incomingIntermediateProcessEvent);
    }
}
