package org.jodaengine.ext.debugging;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jodaengine.RepositoryService;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.api.ReferenceResolverService;
import org.jodaengine.ext.debugging.rest.DereferencedObjectException;
import org.jodaengine.ext.debugging.util.IDProcessDefinitionImpl;
import org.jodaengine.ext.debugging.util.UUIDBreakpointImpl;
import org.jodaengine.ext.service.ExtensionNotAvailableException;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.definition.ProcessDefinitionImpl;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeImpl;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * This class tests the availability and functionality of the {@link ReferenceResolverServiceImpl}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-31
 */
public class ReferenceResolverServiceTest extends AbstractJodaEngineTest {
    
    private DebuggerServiceImpl debugger;
    private RepositoryService repository;
    private ReferenceResolverService resolver;
    private List<Breakpoint> breakpoints;
    private ProcessDefinitionID definitionID;
    private ProcessDefinition definition;
    private Node node;
    private Breakpoint breakpoint;
    
    /**
     * Setup.
     * 
     * @throws ExtensionNotAvailableException test fails
     * @throws IllegalAccessException reflection failed
     * @throws NoSuchFieldException reflection failed
     */
    @BeforeMethod
    public void setUp() throws ExtensionNotAvailableException, NoSuchFieldException, IllegalAccessException {
        
        //
        // services
        //
        ExtensionService extensionService = this.jodaEngineServices.getExtensionService();
        
        this.debugger = extensionService.getExtensionService(
            DebuggerServiceImpl.class,
            DebuggerServiceImpl.DEBUGGER_SERVICE_NAME);
        
        this.resolver = extensionService.getExtensionService(
            ReferenceResolverService.class,
            ReferenceResolverService.RESOLVER_SERVICE_NAME);
        
        this.repository = this.jodaEngineServices.getRepositoryService();
        
        Assert.assertNotNull(this.debugger);
        Assert.assertNotNull(this.resolver);
        Assert.assertNotNull(this.repository);
        Assert.assertTrue(this.debugger.isRunning());
        Assert.assertTrue(this.resolver.isRunning());
        Assert.assertTrue(this.repository.isRunning());
        
        //
        // artifacts and resources
        //
        
        this.node = new NodeImpl(null, null, null);
        List<Node> nodes = new ArrayList<Node>();
        nodes.add(this.node);
        this.definitionID = new ProcessDefinitionID("test", 1);
        this.definition = new IDProcessDefinitionImpl(this.definitionID, nodes);
        
        this.breakpoints = new ArrayList<Breakpoint>();
        this.breakpoints.add(new UUIDBreakpointImpl(UUID.randomUUID(), this.node));
        this.breakpoints.add(new UUIDBreakpointImpl(UUID.randomUUID(), this.node));
        this.breakpoints.add(new UUIDBreakpointImpl(UUID.randomUUID(), this.node));
        this.breakpoints.add(new UUIDBreakpointImpl(UUID.randomUUID(), this.node));
        Assert.assertFalse(this.breakpoints.isEmpty());
        this.breakpoint = this.breakpoints.get(0);
    }
    
    /**
     * Tests the proper resolution of a {@link ProcessDefinition}.
     * 
     * @throws DefinitionNotFoundException test fails
     */
    @Test
    public void testDefinitionResolution() throws DefinitionNotFoundException {
        
        //
        // deploy process definition
        //
        this.repository.addProcessDefinition(this.definition);
        
        ProcessDefinition dereferencedDefinition = mock(ProcessDefinitionImpl.class);
        
        //
        // same id - resolution
        //
        ProcessDefinitionID definitionIDEqual = new ProcessDefinitionID(
            this.definitionID.getIdentifier(), this.definitionID.getVersion());
        when(dereferencedDefinition.getID()).thenReturn(definitionIDEqual);
        
        Assert.assertTrue(this.definition.equals(dereferencedDefinition));
        Assert.assertSame(this.resolver.resolveDefinition(dereferencedDefinition), this.definition);
    }
    
    /**
     * Tests the failing resolution of {@link ProcessDefinition}.
     * 
     * @throws DefinitionNotFoundException expected
     */
    @Test(dependsOnMethods = "testDefinitionResolution", expectedExceptions = DefinitionNotFoundException.class)
    public void testFailingDefinitionResolution() throws DefinitionNotFoundException {
        
        ProcessDefinition dereferencedDefinition = mock(ProcessDefinitionImpl.class);
        
        //
        // different id - no resolution
        //
        ProcessDefinitionID definitionIDDifferent = new ProcessDefinitionID(
            this.definitionID.getIdentifier(), this.definitionID.getVersion() + 1);
        when(dereferencedDefinition.getID()).thenReturn(definitionIDDifferent);
        
        Assert.assertFalse(this.definition.equals(dereferencedDefinition));
        Assert.assertSame(this.resolver.resolveDefinition(dereferencedDefinition), this.definition);
    }
    
    /**
     * This tests the resolution of a {@link Node}.
     */
    @Test
    public void testNodeResolution() {
        
        //
        // deploy process definition
        //
        this.repository.addProcessDefinition(this.definition);
        
        //
        // same id - resolution
        //
        Node dereferencedNode = mock(NodeImpl.class);
        when(dereferencedNode.getID()).thenReturn(this.node.getID());
        
        Assert.assertTrue(this.node.equals(dereferencedNode));
        Assert.assertSame(this.resolver.resolveNode(this.definition, dereferencedNode), this.node);
    }
    
    /**
     * Tests the failing resolution of {@link Node}.
     */
    @Test(dependsOnMethods = "testNodeResolution", expectedExceptions = DereferencedObjectException.class)
    public void testFailingNodeResolution() {
        
        Node dereferencedNode = mock(Node.class);
        
        //
        // different id - no resolution
        //
        when(dereferencedNode.getID()).thenReturn(UUID.randomUUID());
        
        Assert.assertFalse(this.node.equals(dereferencedNode));
        Assert.assertSame(this.resolver.resolveNode(this.definition, dereferencedNode), this.node);
    }
    
    /**
     * This tests the resolution of a {@link Breakpoint}.
     */
    @Test
    public void testBreakpointResolution() {
        
        //
        // register breakpoint
        //
        this.debugger.registerBreakpoints(this.breakpoints, this.definition);
        
        //
        // same id - resolution
        //
        Breakpoint dereferencedBreakpoint = mock(Breakpoint.class);
        when(dereferencedBreakpoint.getID()).thenReturn(this.breakpoint.getID());
        when(dereferencedBreakpoint.getNode()).thenReturn(this.breakpoint.getNode());
        when(dereferencedBreakpoint.getCondition()).thenReturn(this.breakpoint.getCondition());
        
        Assert.assertTrue(this.breakpoint.equals(dereferencedBreakpoint));
        Assert.assertSame(this.resolver.resolveBreakpoint(dereferencedBreakpoint), this.breakpoint);
    }
    
    /**
     * Tests the failing resolution of {@link Breakpoint}.
     */
    @Test(dependsOnMethods = "testBreakpointResolution", expectedExceptions = DereferencedObjectException.class)
    public void testFailingBreakpointResolution() {
        
        Breakpoint dereferencedBreakpoint = mock(Breakpoint.class);
        when(dereferencedBreakpoint.getNode()).thenReturn(this.breakpoint.getNode());
        when(dereferencedBreakpoint.getCondition()).thenReturn(this.breakpoint.getCondition());
        
        //
        // different id - no resolution
        //
        when(dereferencedBreakpoint.getID()).thenReturn(UUID.randomUUID());
        
        Assert.assertFalse(this.breakpoint.equals(dereferencedBreakpoint));
        Assert.assertSame(this.resolver.resolveBreakpoint(dereferencedBreakpoint), this.breakpoint);
    }
}
