package org.jodaengine.node.activity.custom;

import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.eventmanagement.SendEvents;
import org.jodaengine.eventmanagement.adapter.twitter.OutgoingTwitterSingleAccountTweetAdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.outgoing.OutgoingTweetEvent;
import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.token.AbstractToken;

/**
 * An activity used to tweet something.. for now the ProcessInstanceID
 */
public class TweetActivity extends AbstractActivity {

    public static final String PATH_TO_TWITTERCONFIG = "src/main/resources/twitter/twitter.properties";
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeIntern(AbstractToken token) {

        EventSubscriptionManager eventManager = token.getEventManagerService();

        OutgoingTweetEvent processEvent = createOutgoingIntermediateProcessEvent();

        SendEvents bla = (SendEvents) token.getEventManagerService();
        bla.sendMessageFromAdapter("A different Tweet this is. Testing has to be done.", processEvent);
    }
    
    /**
     * Creates the TwitterEcent.
     *
     * @return the process event
     */
    private OutgoingTweetEvent createOutgoingIntermediateProcessEvent() {
        return new OutgoingTweetEvent(new OutgoingTwitterSingleAccountTweetAdapterConfiguration(PATH_TO_TWITTERCONFIG));
    }

}
