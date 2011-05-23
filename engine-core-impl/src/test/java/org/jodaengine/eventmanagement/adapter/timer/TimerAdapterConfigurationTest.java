package org.jodaengine.eventmanagement.adapter.timer;

import org.mockito.internal.util.reflection.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * The TimerAdapterConfiguration test, basically testing equality.
 */
public class TimerAdapterConfigurationTest {

    private final static int DEFAULT_WAITING_TIME = 100;
    private final static int OTHER_WAITING_TIME = 101;
    private final static int SOME_TIME_STAMP_VALUE = 1306139669;
    private final static String TIMESTAMP_FIELD_NAME = "timestamp";
    private TimerAdapterConfiguration configuration;

    /**
     * Sets the up.
     */
    @BeforeTest
    public void setUp() {

        configuration = new TimerAdapterConfiguration(DEFAULT_WAITING_TIME);
    }

    /**
     * Test equals with different values for the waiting time.
     */
    @Test
    public void testEqualsWithDifferentValues() {

        TimerAdapterConfiguration otherConfiguration = new TimerAdapterConfiguration(OTHER_WAITING_TIME);
        Assert.assertEquals(configuration.equals(otherConfiguration), false,
            "Configurations with different values should not be equal.");
    }

    /**
     * Test the equal method when the two configurations are equal.
     */
    @Test
    public void testEqualsWhenEqual() {

        TimerAdapterConfiguration otherConfiguration = new TimerAdapterConfiguration(DEFAULT_WAITING_TIME);
        Whitebox.setInternalState(otherConfiguration, TIMESTAMP_FIELD_NAME, SOME_TIME_STAMP_VALUE);
        Whitebox.setInternalState(configuration, TIMESTAMP_FIELD_NAME, SOME_TIME_STAMP_VALUE);

        Assert.assertTrue(configuration.equals(otherConfiguration), "Equal TimedConfigurations should be equal.");
    }

    /**
     * Test the hash code function when the configurations are equal.
     */
    @Test
    public void testHashCodeWhenEqual() {

        TimerAdapterConfiguration otherConfiguration = new TimerAdapterConfiguration(DEFAULT_WAITING_TIME);
        Whitebox.setInternalState(otherConfiguration, TIMESTAMP_FIELD_NAME, SOME_TIME_STAMP_VALUE);
        Whitebox.setInternalState(configuration, TIMESTAMP_FIELD_NAME, SOME_TIME_STAMP_VALUE);

        Assert.assertEquals(configuration.hashCode(), otherConfiguration.hashCode(),
            "Equal TimedConfigurations should be equal.");
    }

    /**
     * Test hash code with different values for the waiting time..
     */
    @Test
    public void testHashCodeWithDifferentValues() {

        TimerAdapterConfiguration otherConfiguration = new TimerAdapterConfiguration(OTHER_WAITING_TIME);
        Assert.assertNotSame(configuration.hashCode(), otherConfiguration.hashCode(),
            "Hashcodes should be different for different Configurations");
    }

}
