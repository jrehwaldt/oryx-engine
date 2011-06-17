package org.jodaengine.eventmanagement.adapter.twitter;

import org.jodaengine.eventmanagement.adapter.AbstractEventAdapter;
import org.jodaengine.eventmanagement.adapter.AddressableMessage;
import org.jodaengine.eventmanagement.adapter.Message;
import org.jodaengine.eventmanagement.adapter.outgoing.OutgoingMessageAdapter;
import org.jodaengine.exception.JodaEngineRuntimeException;

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
    implements OutgoingMessageAdapter {

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
        String content = "@" + message.getAdress() + " " + message.getContent();
        this.tweet(content);
    }
    
    /**
     * Send a message (tweet).
     *
     * @param message the message to tweet
     */
    private void tweet(String message) {
        if (CharacterUtil.isExceedingLengthLimitation(message)) {
            // TODO new type?
            throw new JodaEngineRuntimeException("Tweet is longer than 140 characters!");
        } else {
            try {
                twitter.updateStatus(message);
            } catch (TwitterException e) {
                throw new JodaEngineRuntimeException("Connection to twitter failed!");
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
