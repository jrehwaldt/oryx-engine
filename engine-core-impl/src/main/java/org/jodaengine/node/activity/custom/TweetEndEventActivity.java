package org.jodaengine.node.activity.custom;

import org.jodaengine.eventmanagement.EventManagerService;
import org.jodaengine.eventmanagement.adapter.MessageImpl;
import org.jodaengine.eventmanagement.adapter.twitter.OutgoingTwitterSingleAccountTweetAdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.outgoing.OutgoingTweetEvent;
import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.token.AbstractToken;

/**
 * An activity used to tweet something.. for now the ProcessInstanceID
 */
public class TweetEndEventActivity extends AbstractActivity {

    // You need to put one there, our one is in the dropbox under Sonstiges/resources
    public static final String PATH_TO_TWITTERCONFIG = "src/main/resources/twitter/twitter.properties";
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeIntern(AbstractToken token) {

        EventManagerService eventManager = token.getEventManagerService();

        OutgoingTweetEvent processEvent = createTwitterProcessEvent();

        String messageToSend = "Processinstance with ID " + token.getInstance().getID().toString() 
            + " finished execution...";
        eventManager.sendMessageFromAdapter(new MessageImpl(messageToSend), processEvent);
    }
    
    /**
     * Creates the TwitterEcent.
     *
     * @return the process event
     */
    private OutgoingTweetEvent createTwitterProcessEvent() {
        return new OutgoingTweetEvent(new OutgoingTwitterSingleAccountTweetAdapterConfiguration(PATH_TO_TWITTERCONFIG));
    }

}
