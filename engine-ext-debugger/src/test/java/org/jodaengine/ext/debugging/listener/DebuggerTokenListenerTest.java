package org.jodaengine.ext.debugging.listener;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.ext.debugging.DebuggerServiceImpl;
import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.api.DebuggerCommand;
import org.jodaengine.ext.debugging.api.Interrupter;
import org.jodaengine.ext.debugging.api.NodeBreakpoint;
import org.jodaengine.ext.debugging.shared.BreakpointImpl;
import org.jodaengine.ext.debugging.shared.DebuggerAttribute;
import org.jodaengine.ext.debugging.util.DirectlyInterruptingInterrupter;
import org.jodaengine.ext.listener.token.ActivityLifecycleChangeEvent;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * This class tests the {@link DebuggerTokenListener} implementation.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-27
 */
public class DebuggerTokenListenerTest extends AbstractJodaEngineTest {
    
    private DebuggerTokenListener listenerWithBreakpoint;
    private DebuggerTokenListener listenerWithoutBreakpoint;
    private ProcessDefinition definition;
    private Node node;
    private ActivityState state;
    private NodeBreakpoint breakpoint;
    private ActivityLifecycleChangeEvent event;
    
    private DebuggerServiceImpl mockDebuggerWithoutBreakpoint;
    private DebuggerServiceImpl mockDebuggerWithBreakpoint;
    private AbstractProcessInstance mockInstance;
    private Token mockToken;
    private Interrupter mockInterrupter;
    
    /**
     * Setup before method.
     * 
     * @throws IllegalStarteventException setup fails
     * @throws InterruptedException nonsense
     */
    @BeforeMethod
    public void setupMethod() throws IllegalStarteventException, InterruptedException {
        
        this.state = ActivityState.READY;
        
        //
        // build a definition
        //
        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();
        this.node = BpmnCustomNodeFactory.createBpmnNullStartNode(builder);
        Assert.assertNotNull(this.node);
        BpmnProcessDefinitionModifier.decorateWithDefaultBpmnInstantiationPattern(builder);
        
        //
        // register breakpoint
        //
        DebuggerAttribute attribute = DebuggerAttribute.getAttribute(builder);
        Assert.assertNotNull(attribute);
        this.breakpoint = new BreakpointImpl(this.node, this.state);
        attribute.addBreakpoint(this.breakpoint);
        
        this.definition = builder.buildDefinition();
        
        Assert.assertFalse(this.definition.getStartNodes().isEmpty());
        Assert.assertEquals(this.definition.getStartNodes().get(0), this.node);
        Assert.assertEquals(this.node, this.breakpoint.getNode());
        
        //
        // setup clean mocks
        //
        this.mockInterrupter = mock(Interrupter.class);
        this.mockDebuggerWithBreakpoint = mock(DebuggerServiceImpl.class);
        this.mockDebuggerWithoutBreakpoint = mock(DebuggerServiceImpl.class);
        this.mockToken = mock(Token.class);
        this.mockInstance = mock(AbstractProcessInstance.class);
        
        this.event = new ActivityLifecycleChangeEvent(
            this.node, ActivityState.ACTIVE, ActivityState.COMPLETED, this.mockToken);
        
        List<Breakpoint> breakpoints = new ArrayList<Breakpoint>();
        breakpoints.add(this.breakpoint);
        breakpoints.add(new BreakpointImpl(mock(Node.class), this.state));
        
        //
        // general definition mock
        //
        when(this.mockToken.getInstance()).thenReturn(this.mockInstance);
        when(this.mockToken.getCurrentNode()).thenReturn(this.node);
        when(this.mockToken.getCurrentActivityState()).thenReturn(this.state);
        when(this.mockInstance.getDefinition()).thenReturn(this.definition);
        when(this.mockDebuggerWithBreakpoint.getBreakpoints(this.mockInstance)).thenReturn(breakpoints);
        
        //
        // interrupter mock
        //
        when(this.mockInterrupter.interruptInstance()).thenReturn(DebuggerCommand.CONTINUE);
        when(this.mockDebuggerWithBreakpoint.breakpointTriggered(
            Mockito.<Token>any(),
            Mockito.<Breakpoint>any(),
            Mockito.<DebuggerTokenListener>any())).thenReturn(this.mockInterrupter);
        when(this.mockDebuggerWithoutBreakpoint.breakpointTriggered(
            Mockito.<Token>any(),
            Mockito.<Breakpoint>any(),
            Mockito.<DebuggerTokenListener>any())).thenReturn(this.mockInterrupter);
        
        //
        // create bounded listeners
        //
        this.listenerWithBreakpoint = new DebuggerTokenListener(this.mockDebuggerWithBreakpoint);
        this.listenerWithoutBreakpoint = new DebuggerTokenListener(this.mockDebuggerWithoutBreakpoint);
        
    }
    
    /**
     * Tests the invocation of a simple process with a single breakpoint.
     * 
     * @throws InterruptedException nonsense
     */
    @Test
    public void testSimpleProcessWithBreakpoint() throws InterruptedException {
        
        this.listenerWithBreakpoint.stateChanged(this.event);
        
        verify(this.mockDebuggerWithBreakpoint, times(1)).getBreakpoints(this.mockInstance);
        verify(this.mockInterrupter, times(1)).interruptInstance();
        verify(this.mockDebuggerWithBreakpoint, times(1)).breakpointTriggered(
            this.mockToken, this.breakpoint, this.listenerWithBreakpoint);
    }
    
    /**
     * Tests the invocation of a simple process without any breakpoint.
     * 
     * @throws InterruptedException nonsense
     */
    @Test
    public void testSimpleProcessWithoutBreakpoint() throws InterruptedException {
        
        Assert.assertTrue(this.breakpoint.isEnabled());
        
        this.listenerWithoutBreakpoint.stateChanged(this.event);
        
        verify(this.mockDebuggerWithoutBreakpoint, times(1)).getBreakpoints(this.mockInstance);
        verify(this.mockInterrupter, never()).interruptInstance();
        verify(this.mockDebuggerWithoutBreakpoint, never()).breakpointTriggered(
            this.mockToken, this.breakpoint, this.listenerWithoutBreakpoint);
    }
    
    /**
     * Tests the invocation of a simple process with a single breakpoint, which is disabled.
     * 
     * @throws InterruptedException nonsense
     */
    @Test
    public void testSimpleProcessWithBreakpointDisabled() throws InterruptedException {
        
        this.breakpoint.disable();
        Assert.assertFalse(this.breakpoint.isEnabled());
        
        this.listenerWithBreakpoint.stateChanged(this.event);
        
        verify(this.mockDebuggerWithBreakpoint, times(1)).getBreakpoints(this.mockInstance);
        verify(this.mockInterrupter, never()).interruptInstance();
        verify(this.mockDebuggerWithBreakpoint, never()).breakpointTriggered(
            this.mockToken, this.breakpoint, this.listenerWithBreakpoint);
    }
    
    /**
     * Tests the invocation of a simple process with a single breakpoint,
     * which is disabled at first and enabled afterwards.
     * 
     * @throws InterruptedException nonsense
     */
    @Test
    public void testSimpleProcessWithBreakpointDisabledAndEnabledAfterwards() throws InterruptedException {
        
        this.breakpoint.disable();
        Assert.assertFalse(this.breakpoint.isEnabled());
        
        this.listenerWithBreakpoint.stateChanged(this.event);
        
        verify(this.mockDebuggerWithBreakpoint, times(1)).getBreakpoints(this.mockInstance);
        verify(this.mockDebuggerWithBreakpoint, never()).breakpointTriggered(
            this.mockToken, this.breakpoint, this.listenerWithBreakpoint);
        
        this.breakpoint.enable();
        Assert.assertTrue(this.breakpoint.isEnabled());
        
        this.listenerWithBreakpoint.stateChanged(this.event);
        
        verify(this.mockDebuggerWithBreakpoint, times(2)).getBreakpoints(this.mockInstance);
        verify(this.mockInterrupter, times(1)).interruptInstance();
        verify(this.mockDebuggerWithBreakpoint, times(1)).breakpointTriggered(
            this.mockToken, this.breakpoint, this.listenerWithBreakpoint);
    }
    
    /**
     * Tests the invocation of a simple process without any breakpoint.
     * 
     * @throws InterruptedException nonsense
     */
    @Test
    public void testSimpleProcessWithNoBreakpointsAttached() throws InterruptedException {
        
        Assert.assertTrue(this.breakpoint.isEnabled());
        when(this.mockDebuggerWithBreakpoint.getBreakpoints(this.mockInstance)).thenReturn(
            Collections.<Breakpoint>emptyList());
        
        this.listenerWithBreakpoint.stateChanged(this.event);
        
        verify(this.mockDebuggerWithBreakpoint, times(1)).getBreakpoints(this.mockInstance);
        verify(this.mockInterrupter, never()).interruptInstance();
        verify(this.mockDebuggerWithBreakpoint, never()).breakpointTriggered(
            this.mockToken, this.breakpoint, this.listenerWithBreakpoint);
    }
    
    /**
     * Tests the invocation of a simple process with any number of breakpoints,
     * where none of those matches.
     * 
     * @throws InterruptedException nonsense
     */
    @Test
    public void testSimpleProcessWithWrongBreakpointsAttached() throws InterruptedException {
        
        Assert.assertTrue(this.breakpoint.isEnabled());
        
        List<Breakpoint> breakpoints = new ArrayList<Breakpoint>();
        breakpoints.add(new BreakpointImpl(mock(Node.class), this.state));
        breakpoints.add(new BreakpointImpl(mock(Node.class), this.state));
        breakpoints.add(new BreakpointImpl(mock(Node.class), this.state));
        when(this.mockDebuggerWithBreakpoint.getBreakpoints(this.mockInstance)).thenReturn(breakpoints);
        
        this.listenerWithBreakpoint.stateChanged(this.event);
        
        verify(this.mockDebuggerWithBreakpoint, times(1)).getBreakpoints(this.mockInstance);
        verify(this.mockInterrupter, never()).interruptInstance();
        verify(this.mockDebuggerWithBreakpoint, never()).breakpointTriggered(
            this.mockToken, this.breakpoint, this.listenerWithBreakpoint);
    }
    
    
    /**
     * Tests the external interruption of a {@link DebuggerTokenListener} will
     * cause the debugger to be notified.
     */
    @Test
    public void testExternalInterruption() {
        
        //
        // create an immediately interrupting interrupter
        //
        Interrupter interrupter = new DirectlyInterruptingInterrupter();
        when(this.mockDebuggerWithBreakpoint.breakpointTriggered(
            Mockito.<Token>any(),
            Mockito.<Breakpoint>any(),
            Mockito.<DebuggerTokenListener>any())).thenReturn(interrupter);
        
        //
        // interrupt the listener
        //
        listenerWithBreakpoint.stateChanged(event);
        
        verify(this.mockDebuggerWithBreakpoint, times(1)).getBreakpoints(this.mockInstance);
        verify(this.mockDebuggerWithBreakpoint, times(1)).breakpointTriggered(
            this.mockToken, this.breakpoint, this.listenerWithBreakpoint);
        
        //
        // This method should be called. The other methods are covered by previous tests...
        //
        verify(this.mockDebuggerWithBreakpoint, times(1)).unexspectedInterruption(interrupter);
    }
}
