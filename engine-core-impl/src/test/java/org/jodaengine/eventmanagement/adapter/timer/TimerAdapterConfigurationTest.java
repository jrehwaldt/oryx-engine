package org.jodaengine.eventmanagement.adapter.timer;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * The TimerAdapterConfiguration test, basically testing equality.
 */
public class TimerAdapterConfigurationTest {

    private final static int DEFAULT_WAITING_TIME = 100;
    private final static int OTHER_WAITING_TIME = 101;
    private TimerAdapterConfiguration configuration;

    /**
     * Sets the up.
     */
    @BeforeTest
    public void setUp() {

        configuration = new TimerAdapterConfiguration(DEFAULT_WAITING_TIME);
    }

    // TODO: tests for equality will have to be mocked, problem being that they are set to currentTime and you cant
    // manually set this

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
     * Test hash code with different values for the waiting time..
     */
    @Test
    public void testHashCodeWithDifferentValues() {

        TimerAdapterConfiguration otherConfiguration = new TimerAdapterConfiguration(OTHER_WAITING_TIME);
        Assert.assertNotSame(configuration.hashCode(), otherConfiguration.hashCode(),
            "Hashcodes should be different for different Configurations");
    }

}
