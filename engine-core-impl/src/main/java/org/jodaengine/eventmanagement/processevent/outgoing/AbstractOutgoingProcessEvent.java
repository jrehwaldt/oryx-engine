package org.jodaengine.eventmanagement.processevent.outgoing;

import org.jodaengine.eventmanagement.adapter.Message;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.AbstractProcessEvent;

/**
 * The class AbstractOutgoingProcessEvent implements the OutgoingProcessEvent interface and therefore adds
 * the ability to retrieve the message saved in an outgoing process event.
 */
public abstract class AbstractOutgoingProcessEvent extends AbstractProcessEvent implements OutgoingProcessEvent {

    /** The message. */
    private Message message;
    
    /**
     * Instantiates a new abstract outgoing process event.
     *
     * @param message the message to be send
     * @param config the configuration of the adapter to be used
     */
    protected AbstractOutgoingProcessEvent(Message message, AdapterConfiguration config) {
        super(config);
        this.message = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message getMessage() {

        return this.message;
    }

}
