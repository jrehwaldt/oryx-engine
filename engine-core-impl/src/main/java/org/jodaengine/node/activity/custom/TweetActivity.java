package org.jodaengine.node.activity.custom;

import org.jodaengine.eventmanagement.EventService;
import org.jodaengine.eventmanagement.adapter.MessageImpl;
import org.jodaengine.eventmanagement.adapter.twitter.OutgoingTwitterSingleAccountTweetAdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.outgoing.OutgoingTweetEvent;
import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.token.AbstractToken;

/**
 * An activity that gets a String on its creation and every time it gets executed,
 * it twitters this message using the account specified in the twitterconfig.
 */
public class TweetActivity extends AbstractActivity {

    private String pathToTwitterConfig;
    // The message that should be twittered.
    private final String message;
    
    /**
     * Instantiates a new tweet activity.
     *
     * @param message the message that should be twittered
     * @param pathToTwitterConfig the path to the twitter configuration properties file
     */
    public TweetActivity(String message, String pathToTwitterConfig) {
        this.message = message;
        this.pathToTwitterConfig = pathToTwitterConfig;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeIntern(AbstractToken token) {

        EventService eventManager = token.getEventManagerService();

        OutgoingTweetEvent processEvent = createTwitterProcessEvent();
        eventManager.send(processEvent);
    }
    
    /**
     * Creates the TwitterEcent.
     *
     * @return the process event
     */
    private OutgoingTweetEvent createTwitterProcessEvent() {
        return new OutgoingTweetEvent(new MessageImpl(message), 
                                      new OutgoingTwitterSingleAccountTweetAdapterConfiguration(pathToTwitterConfig));
    }
}
