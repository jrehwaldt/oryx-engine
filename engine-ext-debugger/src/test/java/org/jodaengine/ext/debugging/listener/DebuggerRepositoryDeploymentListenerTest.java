package org.jodaengine.ext.debugging.listener;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.jodaengine.RepositoryService;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.ext.debugging.DebuggerServiceImpl;
import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.shared.BreakpointImpl;
import org.jodaengine.ext.debugging.shared.DebuggerAttribute;
import org.jodaengine.ext.listener.RepositoryDeploymentListener;
import org.jodaengine.ext.service.ExtensionNotAvailableException;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.ArgumentCaptor;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * This class tests the {@link DebuggerRepositoryDeploymentListener} implementation.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-29
 */
public class DebuggerRepositoryDeploymentListenerTest extends AbstractJodaEngineTest {
    
    private RepositoryDeploymentListener listener;
    private DebuggerServiceImpl debugger;
    private ProcessDefinition definition;
    private Breakpoint breakpoint;
    private DebuggerAttribute attributesDeployed;
    
    /**
     * Setup.
     * 
     * @throws ExtensionNotAvailableException test fails
     * @throws IllegalStarteventException test fails
     */
    @BeforeMethod
    public void setUp() throws ExtensionNotAvailableException, IllegalStarteventException {
        this.debugger = mock(DebuggerServiceImpl.class);
        this.listener = new DebuggerRepositoryDeploymentListener(this.debugger);
        
        //
        // build a definition
        //
        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();
        BpmnCustomNodeFactory.createBpmnNullStartNode(builder);
        BpmnProcessDefinitionModifier.decorateWithDefaultBpmnInstantiationPattern(builder);
        
        //
        // register breakpoint
        //
        DebuggerAttribute attribute = DebuggerAttribute.getAttribute(builder);
        Assert.assertNotNull(attribute);
        this.breakpoint = new BreakpointImpl(null);
        attribute.addBreakpoint(this.breakpoint);
        
        this.definition = builder.buildDefinition();
        
        this.attributesDeployed = DebuggerAttribute.getAttributeIfExists(this.definition);
        Assert.assertNotNull(this.attributesDeployed);
        Assert.assertEquals(attribute, this.attributesDeployed);
        
        Assert.assertNotNull(this.attributesDeployed.getBreakpoints());
        Assert.assertEquals(this.attributesDeployed.getBreakpoints().size(), 1);
    }
    
    /**
     * Tests that the listener correctly registers the breakpoints.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testBreakpointsAreCorrectlyRegisteredAndUnregistered() {
        
        this.attributesDeployed.enable();
        Assert.assertTrue(this.attributesDeployed.isEnabled());
        
        //
        // simulate the definition deployment
        //
        RepositoryService repository = this.jodaEngineServices.getRepositoryService();
        this.listener.definitionDeployed(repository, definition);
        
        //
        // the breakpoints should be registered within our DebuggerService
        //
        @SuppressWarnings("rawtypes")
        ArgumentCaptor<List> breakpointsCaptor = ArgumentCaptor.forClass(List.class);
        verify(this.debugger, times(1)).registerBreakpoints(breakpointsCaptor.capture(), eq(definition));
        
        Assert.assertEquals(this.attributesDeployed.getBreakpoints().size(), breakpointsCaptor.getValue().size());
        Assert.assertEquals(this.attributesDeployed.getBreakpoints(), breakpointsCaptor.getValue());
        
        //
        // the breakpoints should be unregistered successfully
        //
        this.listener.definitionDeleted(repository, definition);
        verify(this.debugger, times(1)).unregisterBreakpoints(eq(definition));
    }

    
    /**
     * Tests that the listener does not register the breakpoints when debugging is disabled.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testBreakpointsAreNotRegisteredIfDisabled() {
        
        this.attributesDeployed.disable();
        Assert.assertFalse(this.attributesDeployed.isEnabled());
        
        //
        // simulate the definition deployment
        //
        RepositoryService repository = this.jodaEngineServices.getRepositoryService();
        this.listener.definitionDeployed(repository, definition);
        
        //
        // the breakpoints should not be registered, because the definition is disabled
        //
        @SuppressWarnings("rawtypes")
        ArgumentCaptor<List> breakpointsCaptor = ArgumentCaptor.forClass(List.class);
        verify(this.debugger, times(0)).registerBreakpoints(breakpointsCaptor.capture(), eq(definition));
        
        //
        // the breakpoints should be unregistered (even if none were registered)
        //
        this.listener.definitionDeleted(repository, definition);
        verify(this.debugger, times(1)).unregisterBreakpoints(eq(definition));
    }
}
