package org.jodaengine.eventmanagement.adapter.mail;

import org.jodaengine.eventmanagement.adapter.AbstractEventAdapter;

/**
 * An Adapter for sending emails via the SMTP protocol.
 */
public class OutgoingMailAdapter extends AbstractEventAdapter<OutgoingMailAdapterConfiguration> {

    
    /**
     * Create a new outgoing MailAdapter from its configuration.
     *
     * @param configuration the configuration
     */
    public OutgoingMailAdapter(OutgoingMailAdapterConfiguration configuration) {
        super(configuration);
    }
}
