package org.jodaengine.ext.debugging;

import static org.mockito.Mockito.mock;

import java.util.Collection;

import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.api.InterruptedInstance;
import org.jodaengine.ext.debugging.api.Interrupter;
import org.jodaengine.ext.debugging.listener.DebuggerTokenListener;
import org.jodaengine.ext.service.ExtensionNotAvailableException;
import org.jodaengine.process.token.Token;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * This class tests the proper functions of the {@link DebuggerServiceImpl}.
 * It specifically tests the interruption-part.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-01
 */
public class DebuggerServiceTest extends AbstractJodaEngineTest {
    
    private DebuggerServiceImpl debugger;
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
        this.debugger = this.jodaEngineServices.getExtensionService().getExtensionService(
            DebuggerServiceImpl.class,
            DebuggerServiceImpl.DEBUGGER_SERVICE_NAME);
        
        Assert.assertNotNull(this.debugger);
        
        this.token = mock(Token.class);
        this.breakpoint = mock(Breakpoint.class);
        this.listener = mock(DebuggerTokenListener.class);
    }
    
    /**
     * Tests that interrupted instances are correctly stored and provided.
     */
    @Test
    public void testInterruptingInstancesAreCorrectlyRemembered() {
        
        Interrupter signal = this.debugger.breakpointTriggered(token, breakpoint, listener);
        Assert.assertNotNull(signal);
        Assert.assertNotNull(signal.getID());
        
        Collection<InterruptedInstance> instances = this.debugger.getInterruptedInstances();
        Assert.assertTrue(instances.contains(signal));
    }
    
    /**
     * Tests that interrupted instances are correctly stored and provided.
     */
    @Test
    public void testInterruptingInstancesAreCorrectlyRemovedWhenUnexpectedBehaviourOccurrs() {
        
        //
        // create a new interrupted instance
        //
        Interrupter signal = this.debugger.breakpointTriggered(token, breakpoint, listener);
        Collection<InterruptedInstance> instances = this.debugger.getInterruptedInstances();
        Assert.assertTrue(instances.contains(signal));
        
        //
        // instances, which are unexpectedly interrupted get removed
        //
        this.debugger.unexspectedInterruption(signal);
        
        instances = this.debugger.getInterruptedInstances();
        Assert.assertFalse(instances.contains(signal));
    }
}
