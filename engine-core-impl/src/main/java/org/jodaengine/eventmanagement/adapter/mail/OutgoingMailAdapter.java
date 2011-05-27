package org.jodaengine.eventmanagement.adapter.mail;

import org.jodaengine.eventmanagement.adapter.AbstractEventAdapter;

/**
 * An Adapter for sending emails via the SMTP protocol.
 */
// TODO EVENTTEAM:make a more generic configuration
public class OutgoingMailAdapter extends AbstractEventAdapter<InboundMailAdapterConfiguration> {

    private InboundMailAdapterConfiguration configuration;
    
    public OutgoingMailAdapter(InboundMailAdapterConfiguration configuration) {

        super(configuration);
        this.configuration = configuration;
    }
}
