package org.jodaengine.eventmanagement.processevent.incoming.intermediate;

import org.jodaengine.eventmanagement.adapter.mail.IncomingMailAdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.process.token.Token;

/**
 * The intermeidate Incoming Imap-Email process event which wiats for a specific email to arrive.
 */
public class IntermediateIncomingImapEmailProcessEvent extends AbstractIncomingIntermediateProcessEvent {

    /**
     * Instantiates a new intermediate incoming imap email process event.
     * 
     * This event is used to wait for a specific email specified by the EventCondition and the AdapterConfiguration.
     * 
     * @param config
     *            the configuration of the mailaccount (username, password etc..)
     *            {@link IncomingMailAdapterConfiguration}
     * @param condition
     *            the condition specifying the properties an incoming has to statisfy in order for this event to fire.
     * @param token
     *            the Token for process execution
     */
    public IntermediateIncomingImapEmailProcessEvent(IncomingMailAdapterConfiguration config, 
                                                     EventCondition condition, 
                                                     Token token) {

        super(config, condition, token);
    }
}
