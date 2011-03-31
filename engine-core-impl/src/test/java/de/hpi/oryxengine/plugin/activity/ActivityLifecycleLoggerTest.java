package de.hpi.oryxengine.plugin.activity;

import static org.mockito.Mockito.mock;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.ActivityState;
import de.hpi.oryxengine.activity.impl.AutomatedDummyActivity;
import de.hpi.oryxengine.process.token.Token;

/**
 * Tests the activity logger.
 * 
 * @author Jan Rehwaldt
 */
public class ActivityLifecycleLoggerTest {
    
    private AutomatedDummyActivity activity = null;
    private AbstractActivityLifecyclePlugin listener = null;
    private Token token = null;
    private ActivityLifecycleChangeEvent event = null;
    
    /**
     * Tests {@link Activity} logger.
     */
    @Test
    public void testLoggingNavigatorStopped() {
        this.listener.update(this.activity, this.event);
        this.listener.stateChanged(this.event);
    }
    
    /**
     * Setup.
     */
   @BeforeTest
   public void beforeMethod() {
       this.activity = mock(AutomatedDummyActivity.class);
       this.listener = ActivityLifecycleLogger.getInstance();
       this.token = mock(Token.class);
       this.event = new ActivityLifecycleChangeEvent(
           this.activity, ActivityState.ACTIVE, ActivityState.COMPLETED, this.token);
   }
}
