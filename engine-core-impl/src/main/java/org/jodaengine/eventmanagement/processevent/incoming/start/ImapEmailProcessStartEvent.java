package org.jodaengine.eventmanagement.processevent.incoming.start;

import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.mail.IncomingMailAdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.incoming.StartProcessEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.process.definition.ProcessDefinitionID;

/**
 * It is a {@link StartProcessEvent} for receiving email from an IMAP account. 
 */
public class ImapEmailProcessStartEvent extends IncomingProcessStartEvent {

    public ImapEmailProcessStartEvent(EventCondition condition,
                                      ProcessDefinitionID definitionID) {

        super(IncomingMailAdapterConfiguration.jodaGoogleConfiguration(), condition, definitionID);
    }
}
