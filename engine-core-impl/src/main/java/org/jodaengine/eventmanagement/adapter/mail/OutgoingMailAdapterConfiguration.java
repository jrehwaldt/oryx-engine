package org.jodaengine.eventmanagement.adapter.mail;

import org.jodaengine.eventmanagement.AdapterManagement;
import org.jodaengine.eventmanagement.adapter.AbstractAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.EventAdapter;
import org.jodaengine.eventmanagement.adapter.EventType;

/**
 * The configuration for our outgoing mail adapter with the necessary information to send an email via SMTP.
 */

// TODO well does this implementation more or less equal the one for the POP/IMAP stuff?
public class OutgoingMailAdapterConfiguration extends AbstractAdapterConfiguration {

    public OutgoingMailAdapterConfiguration(EventType type) {

        super(type);
        // TODO Auto-generated constructor stub
    }

    @Override
    public EventAdapter registerAdapter(AdapterManagement adapterRegistrar) {

        // TODO Auto-generated method stub
        return null;
    }

}
