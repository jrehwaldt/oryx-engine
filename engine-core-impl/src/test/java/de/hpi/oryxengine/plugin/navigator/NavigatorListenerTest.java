package de.hpi.oryxengine.plugin.navigator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.navigator.NavigatorState;

/**
 * Test class for various navigator plugin tests.
 */
public class NavigatorListenerTest {
    
    /** The navigator. */
    private NavigatorImpl navigator = null;
    
    /** The mock. */
    private AbstractNavigatorListener mock = null;
    
    /**
     * Setup method.
     */
    @BeforeTest
    public void setUp() {
        this.navigator = new NavigatorImpl();
        this.mock = mock(AbstractNavigatorListener.class);
        this.navigator.registerPlugin(mock);
        navigator.start();
    }
    
    /**
     * Tests that the plugin is called when navigator starts.
     */
    @Test
    public void testStartedTrigger() {
        verify(mock).update(navigator, NavigatorState.RUNNING);
    }
    
    /**
     * Tests that the plugin is called when navigator stops.
     */
    @Test
    public void testStopTrigger() {
        navigator.stop();
        verify(mock).update(navigator, NavigatorState.STOPPED);
    }
}
