package org.jodaengine.eventmanagement.adapter.mail;

import org.jodaengine.factory.eventmanagement.AdapterConfigurationFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Testing the InboundMailAdapterConfiguration, basically looking if equals and hashCode() work well.
 */
public class InboundMailAdapterConfigurationTest {

    /** The configuration. */
    private IncomingMailAdapterConfiguration configuration;

    /**
     * Sets the up.
     */
    @BeforeTest
    public void setUp() {

        configuration = AdapterConfigurationFactory.createMailAdapterConfiguration();

    }

    /**
     * Tests that 2 configurations with the same values are equal.
     */
    @Test
    public void testEqualsWithIdenticalConfigurations() {

        IncomingMailAdapterConfiguration configurationWithSameValues = AdapterConfigurationFactory
        .createMailAdapterConfiguration();
        Assert.assertEquals(configuration.equals(configurationWithSameValues), true,
            "identical configurations should be equal.");
    }
    
    /**
     * Tests that 2 configurations with different values are not equal.
     */
    @Test
    public void testEqualsWithDifferentConfigurations() {

        IncomingMailAdapterConfiguration configurationWithDifferentValues = AdapterConfigurationFactory
        .createSlightlyDifferentMailAdapterConfiguration();
        Assert.assertEquals(configuration.equals(configurationWithDifferentValues), false,
            "not identical configurations should not be equal.");
    }
    
    /**
     * Test equals works with null.
     */
    @Test
    public void testEqualsWorksWithNull() {
        IncomingMailAdapterConfiguration nullito = null;
        Assert.assertEquals(configuration.equals(nullito), false, "Oops our should not equal null!");
    }
    
    /**
     * Tests that 2 configurations with the same values have the same hashcode.
     */
    @Test
    public void testHashCodeWithIdenticalConfigurations() {

        IncomingMailAdapterConfiguration configurationWithSameValues = AdapterConfigurationFactory
        .createMailAdapterConfiguration();
        Assert.assertEquals(configurationWithSameValues.hashCode(), configuration.hashCode(),
            "Hash codes of identical configurations should be identical");
    }
    
    /**
     * Tests that 2 configurations with different values have not the same hashCode.
     */
    @Test
    public void testHashCodeWithDifferentConfigurations() {

        IncomingMailAdapterConfiguration configurationWithDifferentValues = AdapterConfigurationFactory
        .createSlightlyDifferentMailAdapterConfiguration();
        Assert.assertNotSame(configuration.hashCode(), configurationWithDifferentValues.hashCode(),
            "not identical configurations should not have the same hashCode.");
    }
    
    
}
