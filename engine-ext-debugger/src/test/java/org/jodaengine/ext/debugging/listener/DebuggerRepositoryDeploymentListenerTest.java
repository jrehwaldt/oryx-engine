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
import org.testng.annotations.BeforeClass;
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
    
    /**
     * Setup.
     * 
     * @throws ExtensionNotAvailableException test fails
     */
    @BeforeClass
    public void setUp() throws ExtensionNotAvailableException {
        this.debugger = mock(DebuggerServiceImpl.class);
        this.listener = new DebuggerRepositoryDeploymentListener(this.debugger);
    }
    
    /**
     * Tests that the listener correctly extracts the debugger enabled state.
     * 
     * @throws IllegalStarteventException test fails
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testBreakpointsAreCorrectlyRegistered() throws IllegalStarteventException {
        
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
        Breakpoint breakpoint = new BreakpointImpl(null);
        attribute.addBreakpoint(breakpoint);
        
        ProcessDefinition definition = builder.buildDefinition();
        
        DebuggerAttribute attribute2 = DebuggerAttribute.getAttributeIfExists(definition);
        Assert.assertNotNull(attribute2);
        Assert.assertEquals(attribute, attribute2);
        
        Assert.assertNotNull(attribute2.getBreakpoints());
        Assert.assertEquals(attribute2.getBreakpoints().size(), 1);
        
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
        
        Assert.assertEquals(attribute2.getBreakpoints().size(), breakpointsCaptor.getValue().size());
        Assert.assertEquals(attribute2.getBreakpoints(), breakpointsCaptor.getValue());
    }
}
