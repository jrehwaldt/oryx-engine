package org.jodaengine.eventmanagement.subscription.processevent.intermediate;

import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.twitter.OutgoingTwitterSingleAccountTweetAdapterConfiguration;
import org.jodaengine.process.token.Token;

/**
 * The Intermediate TwiterEvent.
 */
public class TwitterProcessIntermediateEvent extends AbstractProcessIntermediateEvent {

    /**
     * Instantiates a new twitter process intermediate event.
     * 
     * @param configuration
     *            the configuration
     * @param token
     *            the token
     */
    public TwitterProcessIntermediateEvent(OutgoingTwitterSingleAccountTweetAdapterConfiguration configuration,
                                           Token token) {

        super(EventTypes.Twitter, configuration, token);
    }

}
