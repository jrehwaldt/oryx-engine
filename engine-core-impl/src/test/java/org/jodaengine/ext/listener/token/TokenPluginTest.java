package org.jodaengine.ext.listener.token;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.ext.listener.AbstractTokenListener;
import org.jodaengine.node.activity.custom.AutomatedDummyActivity;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import org.jodaengine.plugin.activity.ActivityLifecycleChangeEvent;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeImpl;
import org.jodaengine.process.token.BPMNTokenImpl;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * The Class TokenPluginTest.
 */
public class TokenPluginTest {
    private BPMNTokenImpl token;
    private ArgumentCaptor<ActivityLifecycleChangeEvent> eventCapturer = null;
    private AbstractTokenListener mock;

    /**
     * Sets up a token that points to a node and registers a mocked plugin.
     */
    @BeforeMethod
    public void setUp() {

        String dummyString = "s.out";

        Node node1;

        node1 = new NodeImpl(new AutomatedDummyActivity(dummyString), new SimpleJoinBehaviour(),
            new TakeAllSplitBehaviour());
        this.token = new BPMNTokenImpl(node1);

        mock = mock(AbstractTokenListener.class);
        token.registerPlugin(mock);
        this.eventCapturer = ArgumentCaptor.forClass(ActivityLifecycleChangeEvent.class);
    }

    /**
     * Test the deregistration of the plugin.
     * 
     * @throws JodaEngineException the JodaEngine exception
     */
    @Test
    public void testDeregistration()
    throws JodaEngineException {

        token.deregisterPlugin(mock);
        token.executeStep();
        verify(mock, never()).update(eq(this.token), this.eventCapturer.capture());
    }

    /**
     * Tests that new tokens that are created by this token receive the same plugins the creator has.
     * 
     * @throws JodaEngineException the JodaEngine exception
     */
    @Test
    public void testPluginRegistrationInheritance()
    throws JodaEngineException {

        BPMNTokenImpl newToken = (BPMNTokenImpl) token.createNewToken(token.getCurrentNode());
        newToken.executeStep();
        verify(mock, times(2)).update(eq(newToken), this.eventCapturer.capture());
    }

    /**
     * Tests that newly created tokens do not receive plugins that were deregistered before.
     * 
     * @throws JodaEngineException the JodaEngine exception
     */
    @Test
    public void testPluginDeregistrationInheritance()
    throws JodaEngineException {

        token.deregisterPlugin(mock);
        BPMNTokenImpl newToken = (BPMNTokenImpl) token.createNewToken(token.getCurrentNode());
        newToken.executeStep();
        verify(mock, never()).update(eq(newToken), this.eventCapturer.capture());
    }
}
