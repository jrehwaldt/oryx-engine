package org.jodaengine.eventmanagement.processevent.incoming.start;

import org.jodaengine.eventmanagement.adapter.mail.IncomingMailAdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.incoming.IncomingStartProcessEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.process.definition.ProcessDefinitionID;

/**
 * It is a {@link IncomingStartProcessEvent} for receiving email from an IMAP account.
 */
public class ImapEmailProcessStartEvent extends BaseIncomingStartProcessEvent {

    /**
     * Default constructor for an {@link ImapEmailProcessStartEvent}.
     * 
     * @param condition
     *            - the condition that specifies what the incoming email should look like
     * @param definitionID
     *            - the id to the {@link ProcessDefinitionID} in order to start the process when this
     *            {@link BaseIncomingStartProcessEvent} triggers
     */
    public ImapEmailProcessStartEvent(EventCondition condition, ProcessDefinitionID definitionID) {

        super(IncomingMailAdapterConfiguration.jodaGoogleConfiguration(), condition, definitionID);
    }
}
