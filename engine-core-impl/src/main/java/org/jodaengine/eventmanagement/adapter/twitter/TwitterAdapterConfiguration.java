package org.jodaengine.eventmanagement.adapter.twitter;

import org.jodaengine.eventmanagement.AdapterManagement;
import org.jodaengine.eventmanagement.adapter.AbstractAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.EventAdapter;
import org.jodaengine.eventmanagement.adapter.EventType;
import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.outgoing.OutgoingAdapter;

import twitter4j.conf.ConfigurationBuilder;

/**
 * This is the configuration that Twitter4J needs to connect an application to a specific
 * Twitter account.
 * 
 * Twitter uses OAuth.
 */
public class TwitterAdapterConfiguration extends AbstractAdapterConfiguration {

    /**
     * The ConfigurationBuilder holds all the oatuh specific things and is then used to connect to Twitter.
     * So This is what the adapter is going to work with.
     */
    private ConfigurationBuilder configurationBuilder;

    /**
     * Instantiates a new twitter adapter configuration, given the path to a properties file.
     * 
     * @param propertiesFilePath
     *            the properties file path
     */
    public TwitterAdapterConfiguration(String propertiesFilePath) {

        super(EventTypes.Twitter);
        // TODO open the file and do stuff
    }

    /**
     * Instantiates a new twitter adapter, given all the necessary keys.
     * 
     * @param oauthConsumerKey
     *            the oauth consumer key
     * @param oauthConsumerSecret
     *            the oauth consumer secret
     * @param oauthAccessToken
     *            the oauth access token
     * @param oauthAccessTokenSecret
     *            the oauth access token secret string
     */
    public TwitterAdapterConfiguration(String oauthConsumerKey,
                                       String oauthConsumerSecret,
                                       String oauthAccessToken,
                                       String oauthAccessTokenSecret) {

        super(EventTypes.Twitter);
        populateConfigurationBuilder(oauthConsumerKey, oauthConsumerSecret, oauthAccessToken, oauthAccessTokenSecret);

    }

    /**
     * Populate the configuration builder with the necessary oauth data.
     *
     * @param oauthConsumerKey the oauth consumer key
     * @param oauthConsumerSecret the oauth consumer secret
     * @param oauthAccessToken the oauth access token
     * @param oauthAccessTokenSecret the oauth access token secret
     */
    private void populateConfigurationBuilder(String oauthConsumerKey,
                                              String oauthConsumerSecret,
                                              String oauthAccessToken,
                                              String oauthAccessTokenSecret) {

        configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true)
            .setOAuthConsumerKey(oauthConsumerKey)
            .setOAuthConsumerSecret(oauthConsumerSecret)
            .setOAuthAccessToken(oauthAccessToken)
            .setOAuthAccessTokenSecret(oauthAccessTokenSecret);
    }

    /**
     * Gets the configuration builder, which can be used to connect to Twitter.
     * Therefore you need to do roughly the equivalent of the following:
     * 
     * new TwitterFactory(configuration.getConfigurationBuilder.build());
     *
     * @return the configuration builder
     */
    public ConfigurationBuilder getConfigurationBuilder() {

        return configurationBuilder;
    }
    
    @Override
    public EventAdapter registerAdapter(AdapterManagement adapterRegistrar) {

        // TODO Auto-generated method stub
        return null;
    }

}
