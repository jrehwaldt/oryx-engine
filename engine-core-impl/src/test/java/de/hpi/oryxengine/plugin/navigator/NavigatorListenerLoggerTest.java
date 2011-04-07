package de.hpi.oryxengine.plugin.navigator;

import static org.mockito.Mockito.mock;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.navigator.NavigatorState;

/**
 * Tests the navigator logger.
 * 
 * @author Jan Rehwaldt
 */
// TODO mock it so it really tests something
public class NavigatorListenerLoggerTest {
    
    private NavigatorImpl navigator = null;
    private AbstractNavigatorListener listener = null;
    
    /**
     * Tests {@link Navigator} stopped.
     */
    @Test
    public void testLoggingNavigatorStopped() {
        NavigatorState state = NavigatorState.RUNNING;
        this.listener.update(this.navigator, state);
        this.listener.stateChanged(this.navigator, state);
    }
    
    /**
     * Tests {@link Navigator} started.
     */
    @Test
    public void testLoggingNavigatorStarted() {
        NavigatorState state = NavigatorState.STOPPED;
        this.listener.update(this.navigator, state);
        this.listener.stateChanged(this.navigator, state);
    }
    
    /**
     * Tests {@link Navigator} initialized (default switch branch...).
     */
    @Test
    public void testLoggingNavigatorInitializedAndDefaultSwitchBranch() {
        this.listener.update(this.navigator, NavigatorState.INIT);
    }
    
    
    /**
     * Setup.
     */
   @BeforeTest
   public void beforeMethod() {
       this.navigator = mock(NavigatorImpl.class);
       this.listener = NavigatorListenerLogger.getInstance();
   }
}
