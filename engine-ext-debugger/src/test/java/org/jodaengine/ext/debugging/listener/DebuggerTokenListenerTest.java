package org.jodaengine.ext.debugging.listener;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.ext.debugging.DebuggerServiceImpl;
import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.shared.BreakpointImpl;
import org.jodaengine.ext.debugging.shared.DebuggerAttribute;
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
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
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
    private Breakpoint breakpoint;
    private ActivityLifecycleChangeEvent event;
    
    private DebuggerServiceImpl mockDebuggerWithoutBreakpoint;
    private DebuggerServiceImpl mockDebuggerWithBreakpoint;
    private AbstractProcessInstance mockInstance;
    private Token mockToken;
    
    /**
     * Setup.
     * 
     * @throws IllegalStarteventException setup fails
     */
    @BeforeClass
    public void setUp() throws IllegalStarteventException {
        
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
        this.breakpoint = new BreakpointImpl(this.node);
        attribute.addBreakpoint(this.breakpoint);
        
        this.definition = builder.buildDefinition();
        
        Assert.assertFalse(this.definition.getStartNodes().isEmpty());
        Assert.assertEquals(this.definition.getStartNodes().get(0), this.node);
        
        Assert.assertEquals(this.node, this.breakpoint.getNode());
    }
    
    /**
     * Setup before method.
     */
    @BeforeMethod
    public void setupMethod() {
        
        //
        // setup clean mocks
        //
        this.mockDebuggerWithBreakpoint = mock(DebuggerServiceImpl.class);
        this.mockDebuggerWithoutBreakpoint = mock(DebuggerServiceImpl.class);
        this.mockToken = mock(Token.class);
        this.mockInstance = mock(AbstractProcessInstance.class);
        
        this.event = new ActivityLifecycleChangeEvent(
            this.node, ActivityState.ACTIVE, ActivityState.COMPLETED, this.mockToken);
        
        List<Breakpoint> breakpoints = new ArrayList<Breakpoint>();
        breakpoints.add(this.breakpoint);
        
        when(this.mockToken.getInstance()).thenReturn(this.mockInstance);
        when(this.mockToken.getCurrentNode()).thenReturn(this.node);
        when(this.mockInstance.getDefinition()).thenReturn(this.definition);
        when(this.mockDebuggerWithBreakpoint.getBreakpoints(this.node, this.mockInstance)).thenReturn(breakpoints);
        
        //
        // create bounded listeners
        //
        this.listenerWithBreakpoint = new DebuggerTokenListener(this.mockDebuggerWithBreakpoint);
        this.listenerWithoutBreakpoint = new DebuggerTokenListener(this.mockDebuggerWithoutBreakpoint);
        
    }
    
    /**
     * Tests the invocation of a simple process with a single breakpoint.
     * 
     * @throws IllegalStarteventException test fails
     */
    @Test
    public void testSimpleProcessWithBreakpoint() throws IllegalStarteventException {
        
        this.listenerWithBreakpoint.stateChanged(event);
        
        verify(this.mockDebuggerWithBreakpoint, times(1)).getBreakpoints(this.node, this.mockInstance);
        verify(this.mockDebuggerWithBreakpoint, times(1)).breakpointMatched(this.mockToken, this.breakpoint);
    }
    
    /**
     * Tests the invocation of a simple process without any breakpoint.
     * 
     * @throws IllegalStarteventException test fails
     */
    @Test
    public void testSimpleProcessWithoutBreakpoint() throws IllegalStarteventException {
        
        Assert.assertTrue(this.breakpoint.isEnabled());
        
        this.listenerWithoutBreakpoint.stateChanged(event);
        
        verify(this.mockDebuggerWithoutBreakpoint, times(1)).getBreakpoints(this.node, this.mockInstance);
        verify(this.mockDebuggerWithoutBreakpoint, times(0)).breakpointMatched(this.mockToken, this.breakpoint);
    }
    
    /**
     * Tests the invocation of a simple process with a single breakpoint, which is disabled.
     * 
     * @throws IllegalStarteventException test fails
     */
    @Test
    public void testSimpleProcessWithBreakpointDisabled() throws IllegalStarteventException {
        
        this.breakpoint.disable();
        Assert.assertFalse(this.breakpoint.isEnabled());
        
        this.listenerWithBreakpoint.stateChanged(event);
        
        verify(this.mockDebuggerWithBreakpoint, times(1)).getBreakpoints(this.node, this.mockInstance);
        verify(this.mockDebuggerWithBreakpoint, times(0)).breakpointMatched(this.mockToken, this.breakpoint);
    }
    
    /**
     * Tests the invocation of a simple process with a single breakpoint,
     * which is disabled at first and enabled afterwards.
     * 
     * @throws IllegalStarteventException test fails
     */
    @Test
    public void testSimpleProcessWithBreakpointDisabledAndEnabledAfterwards() throws IllegalStarteventException {
        
        this.breakpoint.disable();
        Assert.assertFalse(this.breakpoint.isEnabled());
        
        this.listenerWithBreakpoint.stateChanged(event);
        
        verify(this.mockDebuggerWithBreakpoint, times(1)).getBreakpoints(this.node, this.mockInstance);
        verify(this.mockDebuggerWithBreakpoint, times(0)).breakpointMatched(this.mockToken, this.breakpoint);
        
        this.breakpoint.enable();
        Assert.assertTrue(this.breakpoint.isEnabled());
        
        this.listenerWithBreakpoint.stateChanged(event);
        
        verify(this.mockDebuggerWithBreakpoint, times(2)).getBreakpoints(this.node, this.mockInstance);
        verify(this.mockDebuggerWithBreakpoint, times(1)).breakpointMatched(this.mockToken, this.breakpoint);
    }
}
