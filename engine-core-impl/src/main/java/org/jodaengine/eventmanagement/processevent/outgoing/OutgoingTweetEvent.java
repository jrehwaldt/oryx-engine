package org.jodaengine.eventmanagement.processevent.outgoing;

import org.jodaengine.eventmanagement.adapter.twitter.OutgoingTwitterSingleAccountTweetAdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.AbstractProcessEvent;

/**
 * The Outgoing Tweet event.
 */
public class OutgoingTweetEvent extends AbstractProcessEvent {

    /**
     * Instantiates a new outgoing tweet event.
     *
     * @param config the config
     */
    public  OutgoingTweetEvent(OutgoingTwitterSingleAccountTweetAdapterConfiguration config) {

        super(config);
    }

}
