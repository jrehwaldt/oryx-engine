package org.jodaengine.eventmanagement.adapter.twitter;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Testing the OutgoingTwitterSingleAccountTweetAdapterConfiguration.
 * 
 * As this is mostly about equals/hashCode don't be surprised that the used values aren't really oauth tokens.
 */
public class OutgoingTwitterSingleAccountTweetAdapterConfigurationTest {

    private OutgoingTwitterSingleAccountTweetAdapterConfiguration config1;
    private OutgoingTwitterSingleAccountTweetAdapterConfiguration config2;
    private static final String CONSUMER_KEY = "asdjau7AHJSasFFiaU";
    private static final String CONSUMER_SECRET = "HUuasdahtwq6201Mjaa";
    private static final String ACCESS_TOKEN = "OM7281UThsZlO2W";
    private static final String ACCESS_TOKEN_SECRET = "mZPLoqw7hGs5WrE2D";

    /**
     * Sets the up.
     */
    @BeforeClass
    public void setUp() {
        this.config1 = createConfig(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
    }
    
    /**
     * Test equals with same attributes.
     */
    @Test
    public void testEqualsWithSameAttributes() {     
        config2 = createConfig(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
        Assert.assertTrue(config1.equals(config2));
    }
    
    /**
     * Test hash code with same attributes.
     */
    @Test
    public void testHashCodeWithSameAttributes() {
        config2 = createConfig(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
        Assert.assertEquals(config1.hashCode(), config2.hashCode());
    }
    
    /**
     * Test equals with slightly different attributes.
     */
    @Test
    public void testEqualsWithSlightlyDifferentAttributes() {
        config2 = createConfig(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN + " ", ACCESS_TOKEN_SECRET);
        Assert.assertFalse(config1.equals(config2));
    }
    
    /**
     * Test hash code with slightly different attributes.
     */
    @Test
    public void testHashCodeWithSlightlyDifferentAttributes() {
        config2 = createConfig(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN + " ", ACCESS_TOKEN_SECRET);
        Assert.assertFalse(config1.hashCode() == config2.hashCode());
    }

    /**
     * A method creating the configuration from given arguments (yes it basically calls the constructor but it is
     * shorter).
     * 
     * @param oauthConsumerKey
     *            the oauth consumer key
     * @param oauthConsumerSecret
     *            the oauth consumer secret
     * @param oauthAccessToken
     *            the oauth access token
     * @param oauthAccessTokenSecret
     *            the oauth access token secret
     * @return the outgoing twitter single account tweet adapter configuration
     */
    private OutgoingTwitterSingleAccountTweetAdapterConfiguration createConfig(String oauthConsumerKey,
                                                                               String oauthConsumerSecret,
                                                                               String oauthAccessToken,
                                                                               String oauthAccessTokenSecret) {

        return new OutgoingTwitterSingleAccountTweetAdapterConfiguration(oauthConsumerKey, oauthConsumerSecret,
            oauthAccessToken, oauthAccessTokenSecret);
    }
}
