package org.jodaengine.ext.listener.token;

import static org.mockito.Mockito.mock;

import org.jodaengine.ext.listener.AbstractTokenListener;
import org.jodaengine.ext.logger.TokenListenerLogger;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.TokenImpl;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Tests the activity logger.
 * 
 * @author Jan Rehwaldt
 */
public class TokenListenerLoggerTest {
    
    private Node node = null;
    private AbstractTokenListener listener = null;
    private TokenImpl token = null;
    private ActivityLifecycleChangeEvent event = null;
    
    /**
     * Tests {@link TokenListenerLogger}.
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
       this.node = mock(Node.class);
       this.listener = TokenListenerLogger.getInstance();
       this.token = mock(TokenImpl.class);
       this.event = new ActivityLifecycleChangeEvent(
           this.node, ActivityState.ACTIVE, ActivityState.COMPLETED, this.token);
   }
}
