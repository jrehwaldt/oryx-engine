package org.jodaengine.eventmanagement.adapter.twitter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.jodaengine.eventmanagement.adapter.AbstractAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.exception.JodaEngineRuntimeException;

import twitter4j.conf.Configuration;
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
    private Configuration configuration;

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

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey(oauthConsumerKey)
        .setOAuthConsumerSecret(oauthConsumerSecret).setOAuthAccessToken(oauthAccessToken)
        .setOAuthAccessTokenSecret(oauthAccessTokenSecret);
        this.configuration = configurationBuilder.build();
    }

    /**
     * Gets the oauth consumer key.
     * 
     * @return the oauth consumer key
     */
    public String getOauthConsumerKey() {

        return configuration.getOAuthConsumerKey();
    }

    /**
     * Gets the oauth consumer secret.
     * 
     * @return the oauth consumer secret
     */
    public String getOauthConsumerSecret() {

        return configuration.getOAuthConsumerSecret();
    }

    /**
     * Gets the oauth access token.
     * 
     * @return the oauth access token
     */
    public String getOauthAccessToken() {

        return configuration.getOAuthAccessToken();
    }

    /**
     * Gets the oauth access token secret.
     * 
     * @return the oauth access token secret
     */
    public String getOauthAccessTokenSecret() {

        return configuration.getOAuthAccessTokenSecret();
    }

    /**
     * Gets the configuration builder, which can be used to connect to Twitter.
     * Therefore you need to do roughly the equivalent of the following:
     * 
     * new TwitterFactory(configuration.getConfigurationBuilder.build());
     * 
     * (lazy initialized)
     * 
     * @return the configuration builder
     */
    public Configuration getTwitterConfiguration() {

        return configuration;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result 
            + ((this.getOauthAccessToken() == null) ? 0 : this.getOauthAccessToken().hashCode());
        result = prime * result
            + ((this.getOauthAccessTokenSecret() == null) ? 0 : this.getOauthAccessTokenSecret().hashCode());
        result = prime * result 
            + ((this.getOauthConsumerKey() == null) ? 0 : this.getOauthConsumerKey().hashCode());
        result = prime * result
            + ((this.getOauthConsumerSecret() == null) ? 0 : this.getOauthConsumerSecret().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractTwitterAdapterConfiguration other = (AbstractTwitterAdapterConfiguration) obj;
        if (this.getOauthAccessToken() == null) {
            if (other.getOauthAccessToken() != null) {
                return false;
            }
        } else if (!this.getOauthAccessToken().equals(other.getOauthAccessToken())) {
            return false;
        }
        if (this.getOauthAccessTokenSecret() == null) {
            if (other.getOauthAccessTokenSecret() != null) {
                return false;
            }
        } else if (!this.getOauthAccessTokenSecret().equals(other.getOauthAccessTokenSecret())) {
            return false;
        }
        if (this.getOauthConsumerKey() == null) {
            if (other.getOauthConsumerKey() != null) {
                return false;
            }
        } else if (!this.getOauthConsumerKey().equals(other.getOauthConsumerKey())) {
            return false;
        }
        if (this.getOauthConsumerSecret() == null) {
            if (other.getOauthConsumerSecret() != null) {
                return false;
            }
        } else if (!this.getOauthConsumerSecret().equals(other.getOauthConsumerSecret())) {
            return false;
        }
        return true;
    }
}
