package de.hpi.oryxengine.plugin.activity;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.activity.ActivityState;
import de.hpi.oryxengine.activity.impl.AutomatedDummyActivity;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;

/**
 * Test class for various activity plugin tests.
 */
public class ActivityLifecyclePluginTest {
    
    private AbstractActivity activity;
    private Token token;
    private AbstractActivityLifecyclePlugin mock;
    
    /**
     * Setup method.
     */
    @BeforeTest
    public void setUp() {
        this.activity = new AutomatedDummyActivity("s.out");
        this.token = new TokenImpl(new NodeImpl(this.activity));
        this.mock = mock(AbstractActivityLifecyclePlugin.class);
        this.activity.registerPlugin(mock);
        
        activity.execute(this.token);
    }
    
    /**
     * Tests that the plugin is called twice during activity lifecycle (active, completed).
     * It's final state will be completed.
     */
    @Test
    public void testStartedTrigger() {
        ArgumentCaptor<ActivityLifecycleChangeEvent> activeEvent
            = ArgumentCaptor.forClass(ActivityLifecycleChangeEvent.class);
        verify(this.mock, times(2)).update(eq(this.activity), activeEvent.capture());
        assertEquals(ActivityState.COMPLETED, activeEvent.getValue().getNewState());
    }
    
}
