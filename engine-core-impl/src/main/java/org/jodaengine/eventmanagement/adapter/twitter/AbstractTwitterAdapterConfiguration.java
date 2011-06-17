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
    private String oauthConsumerKey;
    private String oauthConsumerSecret;
    private String oauthAccessToken;
    private String oauthAccessTokenSecret;

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
            this.oauthConsumerKey = properties.getProperty("oauth.consumerKey");
            this.oauthConsumerSecret = properties.getProperty("oauth.consumerSecret");
            this.oauthAccessToken = properties.getProperty("oauth.accessToken");
            this.oauthAccessTokenSecret = properties.getProperty("oauth.accessTokenSecret");

            this.populateConfigurationBuilder();
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
        this.oauthConsumerKey = oauthConsumerKey;
        this.oauthConsumerSecret = oauthConsumerSecret;
        this.oauthAccessToken = oauthAccessToken;
        this.oauthAccessTokenSecret = oauthAccessTokenSecret;

    }

    /**
     * Populate the configuration builder with the necessary oauth data.
     */
    private void populateConfigurationBuilder() {

        configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey(oauthConsumerKey)
        .setOAuthConsumerSecret(oauthConsumerSecret).setOAuthAccessToken(oauthAccessToken)
        .setOAuthAccessTokenSecret(oauthAccessTokenSecret);
    }
    
    /**
     * Gets the oauth consumer key.
     *
     * @return the oauth consumer key
     */
    public String getOauthConsumerKey() {
        
        return oauthConsumerKey;
    }

    /**
     * Gets the oauth consumer secret.
     *
     * @return the oauth consumer secret
     */
    public String getOauthConsumerSecret() {
    
        return oauthConsumerSecret;
    }

    /**
     * Gets the oauth access token.
     *
     * @return the oauth access token
     */
    public String getOauthAccessToken() {
    
        return oauthAccessToken;
    }

    /**
     * Gets the oauth access token secret.
     *
     * @return the oauth access token secret
     */
    public String getOauthAccessTokenSecret() {
    
        return oauthAccessTokenSecret;
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
    public ConfigurationBuilder getConfigurationBuilder() {

        if (this.configurationBuilder == null) {
            this.populateConfigurationBuilder();
        }
        return configurationBuilder;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((oauthAccessToken == null) ? 0 : oauthAccessToken.hashCode());
        result = prime * result + ((oauthAccessTokenSecret == null) ? 0 : oauthAccessTokenSecret.hashCode());
        result = prime * result + ((oauthConsumerKey == null) ? 0 : oauthConsumerKey.hashCode());
        result = prime * result + ((oauthConsumerSecret == null) ? 0 : oauthConsumerSecret.hashCode());
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
        if (oauthAccessToken == null) {
            if (other.oauthAccessToken != null) {
                return false;
            }
        } else if (!oauthAccessToken.equals(other.getOauthAccessToken())) {
            return false;
        }
        if (oauthAccessTokenSecret == null) {
            if (other.oauthAccessTokenSecret != null) {
                return false;
            }
        } else if (!oauthAccessTokenSecret.equals(other.getOauthAccessTokenSecret())) {
            return false;
        }
        if (oauthConsumerKey == null) {
            if (other.oauthConsumerKey != null) {
                return false;
            }
        } else if (!oauthConsumerKey.equals(other.getOauthConsumerKey())) {
            return false;
        }
        if (oauthConsumerSecret == null) {
            if (other.oauthConsumerSecret != null) {
                return false;
            }
        } else if (!oauthConsumerSecret.equals(other.getOauthAccessTokenSecret())) {
            return false;
        }
        return true;
    }
}
