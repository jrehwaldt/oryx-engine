package org.jodaengine.eventmanagement.adapter.twitter;

import org.jodaengine.eventmanagement.adapter.AbstractEventAdapter;
import org.jodaengine.eventmanagement.adapter.outgoing.OutgoingAdapter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.util.CharacterUtil;

/**
 * An adapter used to let one and only one account tweet.
 * So none of the PIN/Authentication stuff has to be done here. You need the oauth tokens of the account and of the
 * application.
 */
public class OutgoingTwitterSingleAccountTweetAdapter extends AbstractEventAdapter<OutgoingTwitterSingleAccountTweetAdapterConfiguration>
implements OutgoingAdapter {

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

    /**
     * Send a message (tweet).
     *
     * @param message the message to tweet
     * @throws TwitterException the twitter exception
     */
    public void sendMessage(String message) throws TwitterException {
        if (CharacterUtil.isExceedingLengthLimitation(message)) {
            // TODO change - new exception with extension service?
            throw new TwitterException("Tweet is longer than 140 characters!");
        } else {
            twitter.updateStatus(message);
        }
    }
    
    @Override
    public void sendMessage(String recipient, String message) {

        // TODO Auto-generated method stub

    }

    @Override
    public void sendMessage(String recipient, String subject, String message) {

        // TODO Auto-generated method stub

    }
    
    /**
     * Uses the configuration in order to connect to Twitter.
     */
    private void connectToTwitter() {
      TwitterFactory twitterFactory = new TwitterFactory(this.configuration.getConfigurationBuilder().build());
      this.twitter = twitterFactory.getInstance();
    }

}
