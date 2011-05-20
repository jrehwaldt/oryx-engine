package org.jodaengine.eventmanagement;

import static org.testng.Assert.assertEquals;

import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.navigator.Navigator;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


/**
 * Tests the correlation manager, which should start a process instance after correlation.
 * 
 * @author Jan Rehwaldt
 */
public class CorrelationManagerImplTest {

    private static final int INITIAL_ADAPTERS_COUNT = 1;

    private Navigator navigator = null;
    private EventManager manager = null;

//    /**
//     * Tests that a fresh correlation manager contains an {@link ErrorAdapter}.
//     */
//    @Test
//    public void testErrorAdapterExistsAfterStart() {
//
//        this.manager.start();
//        assertNotNull(this.manager.getErrorAdapter());
//    }

    /**
     * Tests that a fresh correlation manager contains no additional adapters.
     */
    @Test
    public void testNoUnusedAdaptersRegisteredByStartup() {

        
        assertEquals(this.manager.getEventAdapters().size(), INITIAL_ADAPTERS_COUNT);
    }

    /**
     * Setup.
     */
    @BeforeTest
    public void beforeMethod() {

//        this.navigator = mock(NavigatorImpl.class);
        this.manager = new EventManager();
        this.manager.start(new JodaEngine());
    }
    
    @AfterMethod
    public void tearDown() {
        this.manager.stop();
    }
}
