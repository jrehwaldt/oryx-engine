package org.jodaengine.ext.debugging;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.RepositoryService;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.shared.DebuggerAttribute;
import org.jodaengine.ext.debugging.util.AttributeKeyProvider;
import org.jodaengine.ext.service.ExtensionNotAvailableException;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * This class tests the proper functions of the {@link DebuggerServiceImpl}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-31
 */
public class DebuggerServiceTest extends AbstractJodaEngineTest {
    
    private static final int NUM_OF_BREAKPOINTS = 4;
    
    private ExtensionService extensionService;
    private DebuggerServiceImpl debugger;
    private List<Breakpoint> breakpoints;
    
    private ProcessDefinition mockDefinition;
    private AbstractProcessInstance mockInstance;
    
    /**
     * Setup.
     * 
     * @throws ExtensionNotAvailableException test fails
     */
    @BeforeMethod
    public void setUp() throws ExtensionNotAvailableException {
        this.extensionService = this.jodaEngineServices.getExtensionService();
        this.debugger = this.extensionService.getExtensionService(
            DebuggerServiceImpl.class,
            DebuggerServiceImpl.DEBUGGER_SERVICE_NAME);
        
        this.mockInstance = mock(AbstractProcessInstance.class);
        this.mockDefinition = mock(ProcessDefinition.class);
        
        when(this.mockInstance.getDefinition()).thenReturn(this.mockDefinition);
        
        ProcessDefinitionID definitionID = mock(ProcessDefinitionID.class);
        when(this.mockDefinition.getID()).thenReturn(definitionID);
        
        this.breakpoints = new ArrayList<Breakpoint>();
        for (int i = 0; i < NUM_OF_BREAKPOINTS; i++) {
            this.breakpoints.add(mock(Breakpoint.class));
        }
    }
    
    /**
     * Tests the registering and unregistering of breakpoints.
     */
    @Test
    public void testRegisteringAndUnregisteringOfBreakpoints() {
        
        //
        // initial setup - no breakpoints available
        //
        Assert.assertNotNull(this.debugger.getBreakpoints(this.mockInstance));
        Assert.assertTrue(this.debugger.getBreakpoints(this.mockInstance).isEmpty());
        
        //
        // register breakpoints
        //
        this.debugger.registerBreakpoints(this.breakpoints, this.mockDefinition);
        
        List<Breakpoint> registered = this.debugger.getBreakpoints(this.mockInstance);
        Assert.assertNotNull(registered);
        Assert.assertFalse(registered.isEmpty());
        
        //
        // only those four are available
        //
        for (Breakpoint breakpoint: registered) {
            Assert.assertTrue(this.breakpoints.contains(breakpoint));
        }
        
        for (Breakpoint breakpoint: this.breakpoints) {
            Assert.assertTrue(registered.contains(breakpoint));
        }
        
        //
        // unregister them
        //
        this.debugger.unregisterBreakpoints(this.mockDefinition);
        
        registered = this.debugger.getBreakpoints(this.mockInstance);
        Assert.assertNotNull(registered);
        Assert.assertTrue(registered.isEmpty());
    }
    
    /**
     * Tests that a proper exception is thrown when a svg artifact should be received for
     * a non-debug {@link ProcessDefinition}.
     * 
     * @throws DefinitionNotFoundException test fails
     * @throws ProcessArtifactNotFoundException expected
     */
    @Test(expectedExceptions = ProcessArtifactNotFoundException.class)
    public void testNonDebugDefinitionGetsSvg() throws ProcessArtifactNotFoundException, DefinitionNotFoundException {
        
        RepositoryService repository = this.jodaEngineServices.getRepositoryService();
        
        //
        // do not provide a DebuggerAttribute
        //
        when(this.mockDefinition.getAttribute(AttributeKeyProvider.getAttributeKey())).thenReturn(null);
        Assert.assertNull(DebuggerAttribute.getAttributeIfExists(this.mockDefinition));
        
        //
        // deploy our definition
        //
        repository.addProcessDefinition(this.mockDefinition);
        
        //
        // getting a svg artifact should fail, because no name is defined
        //
        this.debugger.getSvgArtifact(this.mockDefinition);
    }
    
    /**
     * Tests the removal of an existing breakpoint.
     */
    @Test
    public void testRemovalOfBreakpoint() {
        
        //
        // register this breakpoint
        //
        Breakpoint breakpoint = mock(Breakpoint.class);
        
        List<Breakpoint> removalBreakpoints = new ArrayList<Breakpoint>();
        removalBreakpoints.add(breakpoint);
        this.debugger.registerBreakpoints(removalBreakpoints, this.mockDefinition);
        
        //
        // remove it
        //
        Assert.assertFalse(this.debugger.getBreakpoints(this.mockInstance).isEmpty());
        Assert.assertTrue(this.debugger.removeBreakpoint(breakpoint));
        Assert.assertTrue(this.debugger.getBreakpoints(this.mockInstance).isEmpty());
    }
    
    /**
     * Tests the removal of a not existing breakpoint.
     */
    @Test
    public void testRemovalOfMissingBreakpoint() {
        
        //
        // register other breakpoints
        //
        Breakpoint breakpoint = mock(Breakpoint.class);
        
        this.debugger.registerBreakpoints(this.breakpoints, this.mockDefinition);
        
        //
        // remove it
        //
        Assert.assertEquals(this.debugger.getBreakpoints(this.mockInstance).size(), NUM_OF_BREAKPOINTS);
        Assert.assertFalse(this.debugger.removeBreakpoint(breakpoint));
        Assert.assertEquals(this.debugger.getBreakpoints(this.mockInstance).size(), NUM_OF_BREAKPOINTS);
    }
}
