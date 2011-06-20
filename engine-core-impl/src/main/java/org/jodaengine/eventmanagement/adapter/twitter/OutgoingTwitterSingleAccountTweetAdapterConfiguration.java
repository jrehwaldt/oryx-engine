package org.jodaengine.eventmanagement.adapter.twitter;

import org.jodaengine.eventmanagement.AdapterManagement;
import org.jodaengine.eventmanagement.adapter.EventAdapter;

/**
 * The configuration for the {@link OutgoingTwitterSingleAccountTweetAdapter}, all the major functionality (providing
 * the basic authentication capability for Twitter4J) is encapsulated in the super class.
 * This class is just used to instantiate the appropriate adapter.
 */
public class OutgoingTwitterSingleAccountTweetAdapterConfiguration extends AbstractTwitterAdapterConfiguration {

    /**
     * Instantiates a new outgoing twitter single account tweet adapter configuration.
     *
     * @param propertiesFilePath the properties file path
     * @see AbstractTwitterAdapterConfiguration#AbstractTwitterAdapterConfiguration(String)
     */
    public OutgoingTwitterSingleAccountTweetAdapterConfiguration(String propertiesFilePath) {

        super(propertiesFilePath);
    }

    /**
     * Instantiates a new outgoing twitter single account tweet adapter configuration.
     *
     * @param oauthConsumerKey the oauth consumer key
     * @param oauthConsumerSecret the oauth consumer secret
     * @param oauthAccessToken the oauth access token
     * @param oauthAccessTokenSecret the oauth access token secret
     * @see AbstractTwitterAdapterConfiguration#AbstractTwitterAdapterConfiguration(String, String, String, String)
     */
    public OutgoingTwitterSingleAccountTweetAdapterConfiguration(String oauthConsumerKey,
                                                                 String oauthConsumerSecret,
                                                                 String oauthAccessToken,
                                                                 String oauthAccessTokenSecret) {

        super(oauthConsumerKey, oauthConsumerSecret, oauthAccessToken, oauthAccessTokenSecret);
    }

    @Override
    public EventAdapter registerAdapter(AdapterManagement adapterRegistrar) {

        EventAdapter adapter = this.createAdapter();
        return adapter;
    }
    
    /**
     * Create a new adapter.
     *
     * @return the outgoing twitter single account tweet adapter
     */
    private OutgoingTwitterSingleAccountTweetAdapter createAdapter() {
        return new OutgoingTwitterSingleAccountTweetAdapter(this);
    }
}
