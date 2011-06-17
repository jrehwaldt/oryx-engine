package org.jodaengine.eventmanagement.adapter.twitter;

import org.jodaengine.eventmanagement.adapter.AbstractEventAdapter;
import org.jodaengine.eventmanagement.adapter.AddressableMessage;
import org.jodaengine.eventmanagement.adapter.Message;
import org.jodaengine.eventmanagement.adapter.outgoing.OutgoingMessagingAdapter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.util.CharacterUtil;

/**
 * An adapter used to let one and only one account tweet.
 * So none of the PIN/Authentication stuff has to be done here. You need the oauth tokens of the account and of the
 * application.
 */
public class OutgoingTwitterSingleAccountTweetAdapter 
    extends AbstractEventAdapter<OutgoingTwitterSingleAccountTweetAdapterConfiguration>
    implements OutgoingMessagingAdapter {

    private Twitter twitter;
    
    /**
     * Instantiates a new adapter from a configuration.
     * 
     * @param configuration
     *            the adapter configuration
     */
    public OutgoingTwitterSingleAccountTweetAdapter(
        OutgoingTwitterSingleAccountTweetAdapterConfiguration configuration) {

        super(configuration);
        connectToTwitter();
    }

    @Override
    public void sendMessage(Message message) {
        this.tweet(message.getContent());
    }
    
    /**
     * Send a message with an Address.
     * Addressing in Twitter works via the @-Symbol.
     *
     * @param message the message
     */
    public void sendMessage(AddressableMessage message) {
        String content = "@" + message.getAddress() + " " + message.getContent();
        this.tweet(content);
    }
    
    /**
     * Send a message (tweet) using the Twitter online service.
     * 
     * (see www.twitter.com)
     *
     * @param message the message to tweet
     */
    private void tweet(String message) {
        if (CharacterUtil.isExceedingLengthLimitation(message)) {
            logger.error("The tweet is longer than 140 characters.");            
        } else {
            try {
                twitter.updateStatus(message);
            } catch (TwitterException e) {
                logger.error("The connection to Twitter wasn't possible for some reason.", e);
            }
        }
    }
       

    /**
     * Uses the configuration in order to connect to Twitter.
     */
    private void connectToTwitter() {
      TwitterFactory twitterFactory = new TwitterFactory(this.configuration.getConfigurationBuilder().build());
      this.twitter = twitterFactory.getInstance();
    }

}
