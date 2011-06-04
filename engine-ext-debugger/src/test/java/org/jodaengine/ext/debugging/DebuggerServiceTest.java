package org.jodaengine.ext.debugging;

import static org.mockito.Mockito.mock;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.api.DebuggerCommand;
import org.jodaengine.ext.debugging.api.InterruptedInstance;
import org.jodaengine.ext.debugging.api.Interrupter;
import org.jodaengine.ext.debugging.listener.DebuggerTokenListener;
import org.jodaengine.ext.debugging.shared.InterruptedInstanceImpl;
import org.jodaengine.ext.service.ExtensionNotAvailableException;
import org.jodaengine.process.token.Token;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.inject.internal.Nullable;

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
        
        //
        // create an interrupted instance
        //
        Interrupter signal = createInstance(this.token, this.breakpoint, this.listener);
        Assert.assertNotNull(signal);
        Assert.assertNotNull(signal.getID());
        
        //
        // it is provided via the interrupted instances
        //
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
        Interrupter signal = createInstance(this.token, this.breakpoint, this.listener);
        
        //
        // instances, which are unexpectedly interrupted get removed
        //
        this.debugger.unexspectedInterruption(signal);
        
        Collection<InterruptedInstance> instances = this.debugger.getInterruptedInstances();
        Assert.assertFalse(instances.contains(signal));
    }
    
    /**
     * Tests the continuing of a interrupted process instance.
     * It is required to be removed from the list of interrupted instances.
     * 
     * @throws InterruptedException test fails
     */
    @Test
    public void testInterruptedInstancesWillCorrectlyBeContinued() throws InterruptedException {
        
        //
        // create a new interrupted instance
        //
        InterruptedInstanceImpl instance = createInstance(this.token, this.breakpoint, this.listener);
        Assert.assertFalse(instance.isReleased());
        
        //
        // continue instance
        //
        this.debugger.continueInstance(instance);
        
        //
        // we interrupt the instance, which should directly release us
        //
        assertIsRelease(instance, DebuggerCommand.CONTINUE);
    }
    
    /**
     * Tests the resume of a interrupted process instance.
     * It is required to be removed from the list of interrupted instances.
     * 
     * @throws InterruptedException test fails
     */
    @Test
    public void testInterruptedInstancesWillCorrectlyBeResumed() throws InterruptedException {
        
        //
        // create a new interrupted instance
        //
        InterruptedInstanceImpl instance = createInstance(this.token, this.breakpoint, this.listener);
        Assert.assertFalse(instance.isReleased());
        
        //
        // resume instance
        //
        this.debugger.resumeInstance(instance);
        
        //
        // we interrupt the instance, which should directly release us
        //
        assertIsRelease(instance, DebuggerCommand.RESUME);
    }
    
    /**
     * Tests the step over of a interrupted process instance.
     * It is required to be removed from the list of interrupted instances.
     * 
     * @throws InterruptedException test fails
     */
    @Test
    public void testInterruptedInstancesWillCorrectlyBeSteppedOver() throws InterruptedException {
        
        //
        // create a new interrupted instance
        //
        InterruptedInstanceImpl instance = createInstance(this.token, this.breakpoint, this.listener);
        Assert.assertFalse(instance.isReleased());
        
        //
        // step over instance
        //
        this.debugger.stepOverInstance(instance);
        
        //
        // we interrupt the instance, which should directly release us
        //
        assertIsRelease(instance, DebuggerCommand.STEP_OVER);
    }
    
    /**
     * Tests the terminate of a interrupted process instance.
     * It is required to be removed from the list of interrupted instances.
     * 
     * @throws InterruptedException test fails
     */
    @Test
    public void testInterruptedInstancesWillCorrectlyBeTerminated() throws InterruptedException {
        
        //
        // create a new interrupted instance
        //
        InterruptedInstanceImpl instance = createInstance(this.token, this.breakpoint, this.listener);
        Assert.assertFalse(instance.isReleased());
        
        //
        // terminate instance
        //
        this.debugger.terminateInstance(instance);
        
        //
        // we interrupt the instance, which should directly release us
        //
        assertIsRelease(instance, DebuggerCommand.TERMINATE);
    }
    
    /**
     * Creates a new {@link InterruptedInstanceImpl} in the debugger.
     * 
     * @param token the token
     * @param breakpoint the breakpoint
     * @param listener the listener
     * @return the instance
     */
    private InterruptedInstanceImpl createInstance(@Nonnull Token token,
                                                   @Nonnull Breakpoint breakpoint,
                                                   @Nonnull DebuggerTokenListener listener) {
        
        //
        // create a new interrupted instance
        //
        Interrupter signal = this.debugger.breakpointTriggered(token, breakpoint, listener);
        Collection<InterruptedInstance> instances = this.debugger.getInterruptedInstances();
        Assert.assertTrue(instances.contains(signal));
        
        return (InterruptedInstanceImpl) signal;
    }
    
    /**
     * This method asserts, that an {@link InterruptedInstanceImpl} is released
     * with a certain {@link DebuggerCommand}.
     * 
     * @param instance the {@link InterruptedInstanceImpl}, which should have been released
     * @param command the release command
     * @throws InterruptedException test fails, someone interrupted us unexpectedly
     */
    private void assertIsRelease(@Nonnull InterruptedInstanceImpl instance,
                                 @Nullable DebuggerCommand command) throws InterruptedException {
        
        //
        // is no longer available for releasing
        //
        Collection<InterruptedInstance> instances = this.debugger.getInterruptedInstances();
        Assert.assertFalse(instances.contains(instance));
        Assert.assertTrue(instance.isReleased());
        
        //
        // we interrupt the instance, which should directly release us
        //
        Assert.assertEquals(instance.interrupt(), command);
    }
}
