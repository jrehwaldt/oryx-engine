package org.jodaengine.correlation;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.jodaengine.correlation.adapter.error.ErrorAdapter;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.navigator.NavigatorImpl;

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
    private CorrelationManagerImpl manager = null;

    /**
     * Tests that a fresh correlation manager contains an {@link ErrorAdapter}.
     */
    @Test
    public void testErrorAdapterExistsAfterStart() {

        this.manager.start();
        assertNotNull(this.manager.getErrorAdapter());
    }

    /**
     * Tests that a fresh correlation manager contains no additional adapters.
     */
    @Test
    public void testNoUnusedAdaptersRegisteredByStartup() {

        this.manager.start();
        assertEquals(this.manager.getInboundAdapters().size(), INITIAL_ADAPTERS_COUNT);
    }

    /**
     * Setup.
     */
    @BeforeTest
    public void beforeMethod() {

        this.navigator = mock(NavigatorImpl.class);
        this.manager = new CorrelationManagerImpl(this.navigator);
    }
}
