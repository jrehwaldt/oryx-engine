package org.jodaengine.ext.listener.navigator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.ext.listener.AbstractNavigatorListener;
import org.jodaengine.ext.logger.NavigatorListenerLogger;
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
       this.navigator.registerListener(this.listener);
   }
    
   /**
    * Tests {@link Navigator} started.
    */
   @Test
   public void testLoggingNavigatorStarted() {
       navigator.start(new JodaEngine());
       verify(listener).update(this.navigator, NavigatorState.RUNNING);
       
   }
   
    /**
     * Tests {@link Navigator} stopped.
     */
    @Test
    public void testLoggingNavigatorStopped() {
        navigator.start(new JodaEngine());
        navigator.stop();
        verify(listener).update(this.navigator, NavigatorState.STOPPED);
    }
}
