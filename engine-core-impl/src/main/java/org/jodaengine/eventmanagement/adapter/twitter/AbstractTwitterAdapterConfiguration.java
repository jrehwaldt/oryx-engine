package org.jodaengine.eventmanagement.adapter.twitter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.jodaengine.eventmanagement.adapter.AbstractAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.exception.JodaEngineRuntimeException;

import twitter4j.conf.ConfigurationBuilder;

/**
 * This is the configuration that Twitter4J needs to connect an application to a specific
 * Twitter account.
 * 
 * Twitter uses OAuth.
 */
public abstract class AbstractTwitterAdapterConfiguration extends AbstractAdapterConfiguration {

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
    public AbstractTwitterAdapterConfiguration(String propertiesFilePath) {

        super(EventTypes.Twitter);
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(propertiesFilePath));
            String oauthConsumerKey = properties.getProperty("oauth.consumerKey");
            String oauthConsumerSecret = properties.getProperty("oauth.consumerSecret");
            String oauthAccessToken = properties.getProperty("oauth.accessToken");
            String oauthAccessTokenSecret = properties.getProperty("oauth.accessTokenSecret");

            populateConfigurationBuilder(oauthConsumerKey, oauthConsumerSecret, oauthAccessToken,
                oauthAccessTokenSecret);
        } catch (IOException e) {
            String errorMessage = "Failed to load properties file for the TwitterAdapter, does the file exist?";
            throw new JodaEngineRuntimeException(errorMessage, e);
        }
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
    public AbstractTwitterAdapterConfiguration(String oauthConsumerKey,
                                               String oauthConsumerSecret,
                                               String oauthAccessToken,
                                               String oauthAccessTokenSecret) {

        super(EventTypes.Twitter);
        populateConfigurationBuilder(oauthConsumerKey, oauthConsumerSecret, oauthAccessToken, oauthAccessTokenSecret);

    }

    /**
     * Populate the configuration builder with the necessary oauth data.
     * 
     * @param oauthConsumerKey
     *            the oauth consumer key
     * @param oauthConsumerSecret
     *            the oauth consumer secret
     * @param oauthAccessToken
     *            the oauth access token
     * @param oauthAccessTokenSecret
     *            the oauth access token secret
     */
    private void populateConfigurationBuilder(String oauthConsumerKey,
                                              String oauthConsumerSecret,
                                              String oauthAccessToken,
                                              String oauthAccessTokenSecret) {

        configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey(oauthConsumerKey)
        .setOAuthConsumerSecret(oauthConsumerSecret).setOAuthAccessToken(oauthAccessToken)
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

}
