package org.jodaengine.eventmanagement.processevent.incoming.intermediate.processeventgroup;

import org.jodaengine.eventmanagement.processevent.incoming.TriggeringBehaviour;
import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.process.token.Token;

/**
 * This {@link DefaultTokenResumption} is the default {@link TriggeringBehaviour} for a stand alone
 * {@link ProcessIntermediateEvent}. Don't be confused that this behavior inherits from
 * {@link AbstractProcessIntermediateEventGroup}. This class only need the utility methods provided by the
 * {@link AbstractProcessIntermediateEventGroup}.
 * <p>
 * It can be used as null object for a {@link AbstractProcessIntermediateEventGroup}.
 * </p>
 */
public class DefaultTokenResumption extends AbstractProcessIntermediateEventGroup {

    /**
     * Default Constructor this {@link DefaultTokenResumption}.
     * 
     * @param token
     *            - the {@link Token} that created this {@link DefaultTokenResumption}
     */
    public DefaultTokenResumption(Token token) {

        super(token);
    }

    @Override
    protected void triggerIntern(ProcessIntermediateEvent processIntermediateEvent) {

        // Resuming the token with myself
        token.resume(processIntermediateEvent);
    }
}
