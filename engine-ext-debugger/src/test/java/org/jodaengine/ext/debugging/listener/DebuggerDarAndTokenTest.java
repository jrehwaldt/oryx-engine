package org.jodaengine.ext.debugging.listener;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.jodaengine.RepositoryService;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.deployment.importer.archive.DarImporter;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.ext.debugging.DebuggerServiceImpl;
import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.shared.DebuggerAttribute;
import org.jodaengine.ext.listener.token.ActivityLifecycleChangeEvent;
import org.jodaengine.ext.service.ExtensionNotAvailableException;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.jodaengine.util.ReflectionUtil;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * This class tests the {@link DebuggerTokenListener} as well as the
 * {@link DebuggerDarHandler} implementation.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-30
 */
public class DebuggerDarAndTokenTest extends AbstractJodaEngineTest {
    
    private static final String RESOURCE_PATH = "org/jodaengine/ext/debugging/listener/";
    private static final String WORKING_PROCESS_AVAILABLE = RESOURCE_PATH + "WorkingProcessWithSvgAvailable.dar";
    
    private DebuggerTokenListener listener;
    private ActivityLifecycleChangeEvent event;
    
    private DebuggerServiceImpl mockDebugger;
    private AbstractProcessInstance mockInstance;
    private Token mockToken;
    
    /**
     * Setup before method.
     */
    @BeforeMethod
    public void setupMethod() {
        
        //
        // setup clean mocks
        //
        this.mockDebugger = mock(DebuggerServiceImpl.class);
        this.mockToken = mock(Token.class);
        this.mockInstance = mock(AbstractProcessInstance.class);
        
        //
        // create bounded listeners
        //
        this.listener = new DebuggerTokenListener(this.mockDebugger);
    }
    
    /**
     * Tests that the dar handler properly registers a svg artifact within the repository.
     * 
     * @throws ExtensionNotAvailableException test fails
     * @throws ProcessArtifactNotFoundException test fails
     * @throws DefinitionNotFoundException test fails
     * @throws URISyntaxException test fails, file uri not valid
     */
    @Test
    public void testTriggeringOfBreakpointsWhenLoadedViaDarImport()
    throws ExtensionNotAvailableException, ProcessArtifactNotFoundException, URISyntaxException,
    DefinitionNotFoundException {
        
        DebuggerServiceImpl debugger = this.jodaEngineServices.getExtensionService().getExtensionService(
            DebuggerServiceImpl.class,
            DebuggerServiceImpl.DEBUGGER_SERVICE_NAME);
        Assert.assertNotNull(debugger);
        
        RepositoryService repository = this.jodaEngineServices.getRepositoryService();
        Assert.assertNotNull(repository);
        
        DarImporter importer = repository.getNewDarImporter();
        
        URL darFileUrl = ReflectionUtil.getResource(WORKING_PROCESS_AVAILABLE);
        File darFile = new File(new URI(darFileUrl.toString()));
        
        Assert.assertTrue(darFile.exists());
        Assert.assertTrue(darFile.canRead());
        
        //
        // get the deployment and deploy it in the repository
        //
        Deployment deployment = importer.importDarFile(darFile);
        Assert.assertNotNull(deployment);
        repository.deployInNewScope(deployment);
        
        //
        // ... start the instance and check that it's breakpoints will trigger
        //
        Assert.assertNotNull(deployment.getDefinitions());
        Assert.assertEquals(deployment.getDefinitions().size(), 1);
        for (ProcessDefinition definition: deployment.getDefinitions()) {
            
            DebuggerAttribute attribute = DebuggerAttribute.getAttributeIfExists(definition);
            Assert.assertNotNull(attribute);
            
            Assert.assertEquals(attribute.getSvgArtifact(), "svg1.svg");
            
            List<Breakpoint> breakpoints = attribute.getBreakpoints();
            Assert.assertFalse(breakpoints.isEmpty());
            
            Breakpoint breakpoint = breakpoints.get(0);
            Assert.assertNotNull(breakpoint);
            Assert.assertTrue(breakpoint.isEnabled());
            Assert.assertNotNull(breakpoint.getCondition());
            Assert.assertNotNull(breakpoint.getNode());
            Assert.assertNotNull(breakpoint.getID());
            
            Node node = definition.getStartNodes().get(0);
            Assert.assertNotNull(node);
            
            Assert.assertEquals(node, breakpoint.getNode());
            
            //=======================================
            //==== Test the RepositoryListener ======
            //=======================================
            //
            // the breakpoint should correctly be registered within the DebuggerService
            // (via DebuggerRepositoryDeploymentListener)
            //
            when(this.mockInstance.getDefinition()).thenReturn(definition);
            List<Breakpoint> deployedBreakpoints = debugger.getBreakpoints(this.mockInstance);
            Assert.assertNotNull(deployedBreakpoints);
            Assert.assertEquals(deployedBreakpoints.size(), breakpoints.size());
            Assert.assertEquals(deployedBreakpoints.get(0), breakpoints.get(0));
            
            //=======================================
            //==== Test the TokenListener ===========
            //=======================================
            //
            // create test objects
            //
            this.event = new ActivityLifecycleChangeEvent(
                node, ActivityState.ACTIVE, ActivityState.COMPLETED, this.mockToken);
            
            when(this.mockToken.getInstance()).thenReturn(this.mockInstance);
            when(this.mockToken.getCurrentNode()).thenReturn(node);
            when(this.mockInstance.getDefinition()).thenReturn(definition);
            when(this.mockDebugger.getBreakpoints(this.mockInstance)).thenReturn(breakpoints);
            
            //
            // breakpoint is enabled
            //
            Assert.assertTrue(breakpoint.isEnabled());
            
            this.listener.stateChanged(event);
            
            verify(this.mockDebugger, times(1)).getBreakpoints(this.mockInstance);
            verify(this.mockDebugger, times(1)).breakpointTriggered(this.mockToken, breakpoint, this.listener);
            
            //
            // breakpoint is disabled
            //
            breakpoint.disable();
            Assert.assertFalse(breakpoint.isEnabled());
            
            this.listener.stateChanged(event);
            
            verify(this.mockDebugger, times(2)).getBreakpoints(this.mockInstance);
            verify(this.mockDebugger, times(1)).breakpointTriggered(this.mockToken, breakpoint, this.listener);
        }
    }

}
