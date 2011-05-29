package org.jodaengine.ext.debugging.listener;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.InputStream;
import java.util.List;

import org.jodaengine.RepositoryService;
import org.jodaengine.deployment.ProcessDefinitionImporter;
import org.jodaengine.deployment.importer.definition.BpmnXmlImporter;
import org.jodaengine.deployment.importer.definition.bpmn.BpmnXmlParseListener;
import org.jodaengine.ext.debugging.DebuggerServiceImpl;
import org.jodaengine.ext.debugging.shared.DebuggerAttribute;
import org.jodaengine.ext.listener.RepositoryDeploymentListener;
import org.jodaengine.ext.service.ExtensionNotAvailableException;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.util.ReflectionUtil;
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
    
    private static final String RESOURCE_PATH = "org/jodaengine/ext/debugging/listener/";
    private static final String BREAKPOINT_DEFINED = RESOURCE_PATH + "DebuggingEnabledAndBreakpointAvailable.bpmn.xml";
    
    private BpmnXmlParseListener bpmnListener;
    private RepositoryDeploymentListener deployListener;
    private DebuggerServiceImpl debugger;
    
    /**
     * Setup.
     * 
     * @throws ExtensionNotAvailableException test fails
     */
    @BeforeClass
    public void setUp() throws ExtensionNotAvailableException {
        this.debugger = mock(DebuggerServiceImpl.class);
        this.bpmnListener = new DebuggerBpmnXmlParseListener();
        this.deployListener = new DebuggerRepositoryDeploymentListener(this.debugger);
    }

    /**
     * Tests that the listener correctly extracts the debugger enabled state.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testBreakpointsAreCorrectlyRegistered() {
        
        InputStream bpmnXml = ReflectionUtil.getResourceAsStream(BREAKPOINT_DEFINED);
        Assert.assertNotNull(bpmnXml);
        
        ProcessDefinitionImporter importer = new BpmnXmlImporter(bpmnXml, this.bpmnListener);
        //
        // parse the process definition, extract the breakpoints
        //
        ProcessDefinition definition = importer.createProcessDefinition();
        
        Assert.assertNotNull(definition);
        Assert.assertNotNull(definition.getID());
        
        DebuggerAttribute attribute = DebuggerAttribute.getAttributeIfExists(definition);
        Assert.assertNotNull(attribute);
        
        Assert.assertNotNull(attribute.getBreakpoints());
        Assert.assertEquals(attribute.getBreakpoints().size(), 1);
        
        //
        // simulate the definition deployment
        //
        RepositoryService repository = this.jodaEngineServices.getRepositoryService();
        this.deployListener.definitionDeployed(repository, definition);
        
        //
        // the breakpoints should be registered within our DebuggerService
        //
        @SuppressWarnings("rawtypes")
        ArgumentCaptor<List> breakpointsCaptor = ArgumentCaptor.forClass(List.class);
        verify(this.debugger, times(1)).registerBreakpoints(breakpointsCaptor.capture(), eq(definition));
        
        Assert.assertEquals(attribute.getBreakpoints().size(), breakpointsCaptor.getValue().size());
        Assert.assertEquals(attribute.getBreakpoints(), breakpointsCaptor.getValue());
    }
}
