package org.jodaengine.ext.debugging.shared;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.api.InterruptedInstance;
import org.jodaengine.ext.debugging.listener.DebuggerTokenListener;
import org.jodaengine.ext.service.ExtensionNotAvailableException;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.token.Token;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the {@link InterruptedInstanceImpl} functionality.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-01
 */
public class InterruptedInstanceTest {
    
    private Breakpoint breakpoint;
    private Token token;
    private DebuggerTokenListener listener;
    
    /**
     * Setup.
     * 
     * @throws ExtensionNotAvailableException test fails
     */
    @BeforeMethod
    public void setUp() throws ExtensionNotAvailableException {
        this.token = mock(Token.class);
        this.breakpoint = mock(Breakpoint.class);
        this.listener = mock(DebuggerTokenListener.class);
    }
    
    /**
     * Tests the proper getting of a process instance.
     */
    @Test
    public void testGettingTheProcessInstance() {
        
        AbstractProcessInstance process = mock(AbstractProcessInstance.class);
        when(this.token.getInstance()).thenReturn(process);
        
        InterruptedInstance instance = new InterruptedInstanceImpl(this.token, this.breakpoint);
        Assert.assertNotNull(instance.getInterruptedInstance());
        Assert.assertEquals(process, instance.getInterruptedInstance());
    }
    
    //
    // TODO there needs a lot to be done here.
    //
    
    
}
