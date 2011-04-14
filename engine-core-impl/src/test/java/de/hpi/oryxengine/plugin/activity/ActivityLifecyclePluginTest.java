package de.hpi.oryxengine.plugin.activity;

import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeTest;

import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.activity.impl.AutomatedDummyActivity;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;

/**
 * Test class for various activity plugin tests.
 */
public class ActivityLifecyclePluginTest {
    
    private AbstractActivity activity = null;
    private Token token = null;
    private ArgumentCaptor<ActivityLifecycleChangeEvent> eventCapturer = null;
    
    /**
     * Setup method.
     */
    @BeforeTest
    public void setUp() {
//        this.activity = new AutomatedDummyActivity("s.out");
        // TODO parameters
        this.token = new TokenImpl(new NodeImpl(AutomatedDummyActivity.class));
        this.eventCapturer = ArgumentCaptor.forClass(ActivityLifecycleChangeEvent.class);
    }
    
    /**
     * Tests that the plugin is called twice during activity lifecycle (active, completed).
     * It's final state will be completed.
     */
    // TODO add this test again, as soon as we have a solution for activity plugins
//    @Test
//    public void testStartedTrigger() {
//        AbstractActivityLifecyclePlugin mock = mock(AbstractActivityLifecyclePlugin.class);
//        this.activity.registerPlugin(mock);
//        this.activity.execute(this.token);
//        
//        verify(mock, times(2)).update(eq(this.activity), this.eventCapturer.capture());
//        assertEquals(ActivityState.COMPLETED, this.eventCapturer.getValue().getNewState());
//    }
}
