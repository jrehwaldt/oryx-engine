package org.jodaengine.plugin.activity;

import static org.mockito.Mockito.mock;

import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.node.activity.custom.AutomatedDummyActivity;
import org.jodaengine.process.token.TokenImpl;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Tests the activity logger.
 * 
 * @author Jan Rehwaldt
 */
public class ActivityLifecycleLoggerTest {
    
    private AutomatedDummyActivity activity = null;
    private AbstractTokenPlugin listener = null;
    private TokenImpl token = null;
    private ActivityLifecycleChangeEvent event = null;
    
    /**
     * Tests {@link Activity} logger.
     */
    @Test
    public void testLoggingNavigatorStopped() {
        this.listener.update(this.token, this.event);
        this.listener.stateChanged(this.event);
    }
    
    /**
     * Setup.
     */
   @BeforeTest
   public void beforeMethod() {
       this.activity = mock(AutomatedDummyActivity.class);
       this.listener = ActivityLifecycleLogger.getInstance();
       this.token = mock(TokenImpl.class);
       this.event = new ActivityLifecycleChangeEvent(
           this.activity, ActivityState.ACTIVE, ActivityState.COMPLETED, this.token);
   }
}
