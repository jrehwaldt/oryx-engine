package de.hpi.oryxengine.plugin.activity;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.mockito.ArgumentCaptor;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.exception.JodaEngineException;
import de.hpi.oryxengine.node.activity.AbstractActivity;
import de.hpi.oryxengine.node.activity.ActivityState;
import de.hpi.oryxengine.node.activity.fun.AutomatedDummyActivity;
import de.hpi.oryxengine.process.structure.ActivityBlueprint;
import de.hpi.oryxengine.process.structure.ActivityBlueprintImpl;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.process.token.TokenImpl;

/**
 * Test class for various activity plugin tests.
 */
public class ActivityLifecyclePluginTest {
    
    private AbstractActivity activity = null;
    private TokenImpl token = null;
    private ArgumentCaptor<ActivityLifecycleChangeEvent> eventCapturer = null;
    
    /**
     * Setup method.
     */
    @BeforeTest
    public void setUp() {
        Class<?>[] conSig = {String.class};
        Object[] conArgs = {"s.out"};
        ActivityBlueprint blueprint = new ActivityBlueprintImpl(AutomatedDummyActivity.class, conSig, conArgs);
        this.token = new TokenImpl(new NodeImpl(blueprint));
        this.eventCapturer = ArgumentCaptor.forClass(ActivityLifecycleChangeEvent.class);
    }
    
    /**
     * Tests that the plugin is called twice during activity lifecycle (active, completed).
     * It's final state will be completed.
     * @throws JodaEngineException 
     */
    @Test
    public void testStartedTrigger() throws JodaEngineException {
        AbstractTokenPlugin mock = mock(AbstractTokenPlugin.class);
        this.token.registerPlugin(mock);
        token.executeStep();
        
        verify(mock, times(2)).update(eq(this.token), this.eventCapturer.capture());
        Assert.assertEquals(ActivityState.COMPLETED, this.eventCapturer.getValue().getNewState());
    }
}
