package org.jodaengine.plugin.navigator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.jodaengine.ext.navigator.AbstractNavigatorListener;
import org.jodaengine.ext.navigator.NavigatorListenerLogger;
import org.jodaengine.navigator.NavigatorImpl;
import org.jodaengine.navigator.NavigatorState;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the navigator logger.
 * 
 * @author Jan Rehwaldt
 */
public class NavigatorListenerLoggerTest {
    
    private NavigatorImpl navigator = null;
    private AbstractNavigatorListener listener = null;
    
   /**
    * Setup.
    */
   @BeforeMethod
   public void beforeMethod() {
       this.navigator = new NavigatorImpl();
       this.listener = mock(NavigatorListenerLogger.class);
       this.navigator.registerPlugin(this.listener);
   }
    
   /**
    * Tests {@link Navigator} started.
    */
   @Test
   public void testLoggingNavigatorStarted() {
       navigator.start();
       verify(listener).update(this.navigator, NavigatorState.RUNNING);
       
   }
   
    /**
     * Tests {@link Navigator} stopped.
     */
    @Test
    public void testLoggingNavigatorStopped() {
        navigator.start();
        navigator.stop();
        verify(listener).update(this.navigator, NavigatorState.STOPPED);
    }
}
