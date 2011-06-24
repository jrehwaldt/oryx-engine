package org.jodaengine.eventmanagement.processevent.outgoing;

import org.jodaengine.eventmanagement.adapter.Message;
import org.jodaengine.eventmanagement.adapter.twitter.OutgoingTwitterSingleAccountTweetAdapterConfiguration;

/**
 * The Outgoing Tweet event.
 */
public class OutgoingTweetEvent extends AbstractOutgoingProcessEvent {
  
    /**
     * Instantiates a new outgoing tweet event.
     *
     * @param message the message to send
     * @param config the configuration of the adapter to use
     */
    public  OutgoingTweetEvent(Message message, OutgoingTwitterSingleAccountTweetAdapterConfiguration config) {

        super(message, config);
    }

}
