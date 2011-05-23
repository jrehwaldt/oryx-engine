package org.jodaengine.eventmanagement;

import static org.testng.Assert.assertEquals;

import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.eventmanagement.adapter.mail.InboundMailAdapterConfiguration;
import org.jodaengine.factory.eventmanagement.AdapterConfigurationFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the correlation manager, which should start a process instance after correlation.
 * 
 * @author Jan Rehwaldt
 */
public class EventManagerTest {

    // there is one adapter, the error adapter.
    private static final int INITIAL_ADAPTERS_COUNT = 1;
    private EventManager manager;
    
    // /**
    // * Tests that a fresh correlation manager contains an {@link ErrorAdapter}.
    // */
    // @Test
    // public void testErrorAdapterExistsAfterStart() {
    //
    // this.manager.start();
    // assertNotNull(this.manager.getErrorAdapter());
    // }

    /**
     * Setup.
     */
    @BeforeMethod
    public void beforeMethod() {

        // this.navigator = mock(NavigatorImpl.class);
        this.manager = new EventManager();
        this.manager.start(new JodaEngine());
    }

    /**
     * Tests that a fresh correlation manager contains no additional adapters.
     */
    @Test
    public void testNoUnusedAdaptersRegisteredByStartup() {

        assertEquals(this.manager.getEventAdapters().size(), INITIAL_ADAPTERS_COUNT);
    }

    /**
     * Tests whether equal adapter configurations are noticed and no new adapters are added.
     */
    @Test
    public void testEqualAdapterConfigurationsAreNoticed() {

        int initialAdapterCount = manager.getEventAdapters().size();
        InboundMailAdapterConfiguration config1 = AdapterConfigurationFactory.createMailAdapterConfiguration();
        config1.registerAdapter(manager);
        InboundMailAdapterConfiguration config2 = AdapterConfigurationFactory.createMailAdapterConfiguration();
        config2.registerAdapter(manager);

        assertEquals(manager.getEventAdapters().size(), initialAdapterCount + 1,
            "There should only be one new Adapter as the configs were identical "
            + "and so are the corresponding adapters.");
    }

    /**
     * Stop the event manager.
     */
    @AfterMethod
    public void tearDown() {
        this.manager.stop();
    }
}
