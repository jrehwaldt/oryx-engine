package org.jodaengine.ext.debugging;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jodaengine.RepositoryService;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.api.InterruptedInstance;
import org.jodaengine.ext.debugging.api.NodeBreakpoint;
import org.jodaengine.ext.debugging.api.ReferenceResolverService;
import org.jodaengine.ext.debugging.listener.DebuggerTokenListener;
import org.jodaengine.ext.debugging.rest.DereferencedObjectException;
import org.jodaengine.ext.debugging.util.IDProcessDefinitionImpl;
import org.jodaengine.ext.debugging.util.UUIDBreakpointImpl;
import org.jodaengine.ext.service.ExtensionNotAvailableException;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.navigator.NavigatorInside;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.process.definition.AbstractProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.definition.bpmn.BpmnProcessDefinition;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeImpl;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenBuilder;
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
    private Navigator navigator;
    
    private ReferenceResolverService resolver;
    private List<Breakpoint> breakpoints;
    private ProcessDefinitionID definitionID;
    private ProcessDefinition definition;
    private Node node;
    private Node nodeTarget;
    private NodeBreakpoint breakpoint;
    private AbstractProcessInstance instance;
    private InterruptedInstance interruptedInstance;
    
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
        this.navigator = this.jodaEngineServices.getNavigatorService();
        
        Assert.assertNotNull(this.debugger);
        Assert.assertNotNull(this.resolver);
        Assert.assertNotNull(this.repository);
        Assert.assertNotNull(this.navigator);
        Assert.assertTrue(this.debugger.isRunning());
        Assert.assertTrue(this.resolver.isRunning());
        Assert.assertTrue(this.repository.isRunning());
        Assert.assertTrue(this.navigator.isRunning());
        
        //
        // artifacts and resources
        //
        
        this.node = new NodeImpl(null, null, null);
        this.nodeTarget = new NodeImpl(null, null, null);
        List<Node> nodes = new ArrayList<Node>();
        nodes.add(this.node);
        this.node.controlFlowTo(this.nodeTarget);
        this.definitionID = new ProcessDefinitionID("test", 1);
        this.definition = new IDProcessDefinitionImpl(this.definitionID, nodes);
        
        this.debugger.breakTriggered(mock(Token.class), null, mock(DebuggerTokenListener.class));
        Assert.assertFalse(this.debugger.getInterruptedInstances().isEmpty());
        this.interruptedInstance = this.debugger.getInterruptedInstances().iterator().next();
        this.instance = new ProcessInstance(null, mock(TokenBuilder.class));
        
        this.breakpoints = new ArrayList<Breakpoint>();
        this.breakpoints.add(new UUIDBreakpointImpl(UUID.randomUUID(), this.node, ActivityState.READY));
        this.breakpoints.add(new UUIDBreakpointImpl(UUID.randomUUID(), this.node, ActivityState.READY));
        this.breakpoints.add(new UUIDBreakpointImpl(UUID.randomUUID(), this.node, ActivityState.READY));
        this.breakpoints.add(new UUIDBreakpointImpl(UUID.randomUUID(), this.node, ActivityState.READY));
        Assert.assertFalse(this.breakpoints.isEmpty());
        this.breakpoint = (NodeBreakpoint) this.breakpoints.get(0);
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
        
        ProcessDefinition dereferencedDefinition = mock(BpmnProcessDefinition.class);
        
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
        
        ProcessDefinition dereferencedDefinition = mock(BpmnProcessDefinition.class);
        
        //
        // different id - no resolution
        //
        ProcessDefinitionID definitionIDDifferent = new ProcessDefinitionID(
            this.definitionID.getIdentifier(), this.definitionID.getVersion() + 1);
        when(dereferencedDefinition.getID()).thenReturn(definitionIDDifferent);
        
        Assert.assertFalse(this.definition.equals(dereferencedDefinition));
        
        //
        // cause an error
        //
        this.resolver.resolveDefinition(dereferencedDefinition);
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
        
        //
        // it might, magically, be the case that our random id is equal to the id before...
        //
        while (dereferencedNode.getID().equals(this.node.getID())) {
            when(dereferencedNode.getID()).thenReturn(UUID.randomUUID());
        }
        
        Assert.assertFalse(this.node.equals(dereferencedNode));
        
        //
        // cause an error
        //
        this.resolver.resolveNode(this.definition, dereferencedNode);
    }
    
    /**
     * This tests the resolution of a {@link Node}, which is not the start node.
     */
    @Test
    public void testGraphBasedNodeResolution() {
        
        //
        // deploy process definition
        //
        this.repository.addProcessDefinition(this.definition);
        
        //
        // same id - resolution
        //
        Node dereferencedNode = mock(NodeImpl.class);
        when(dereferencedNode.getID()).thenReturn(this.nodeTarget.getID());
        
        Assert.assertTrue(this.nodeTarget.equals(dereferencedNode));
        Assert.assertSame(this.resolver.resolveNode(this.definition, dereferencedNode), this.nodeTarget);
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
        NodeBreakpoint dereferencedBreakpoint = mock(NodeBreakpoint.class);
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
        
        NodeBreakpoint dereferencedBreakpoint = mock(NodeBreakpoint.class);
        when(dereferencedBreakpoint.getNode()).thenReturn(this.breakpoint.getNode());
        when(dereferencedBreakpoint.getCondition()).thenReturn(this.breakpoint.getCondition());
        
        //
        // different id - no resolution
        //
        when(dereferencedBreakpoint.getID()).thenReturn(UUID.randomUUID());
        
        //
        // it might, magically, be the case that our random id is equal to the id before...
        //
        while (dereferencedBreakpoint.getID().equals(this.breakpoint.getID())) {
            when(dereferencedBreakpoint.getID()).thenReturn(UUID.randomUUID());
        }
        
        Assert.assertFalse(this.breakpoint.equals(dereferencedBreakpoint));
        
        //
        // cause an error
        //
        this.resolver.resolveBreakpoint(dereferencedBreakpoint);
    }
    
    /**
     * Tests the proper resolution of a {@link AbstractProcessInstance}.
     * 
     * @throws DefinitionNotFoundException test fails
     */
    @Test
    public void testInstanceResolution() throws DefinitionNotFoundException {
        
        //
        // deploy process definition and start an instance
        //
        AbstractProcessDefinition mockDefinition = mock(AbstractProcessDefinition.class);
        when(mockDefinition.createProcessInstance((NavigatorInside) this.navigator)).thenReturn(this.instance);
        when(mockDefinition.getID()).thenReturn(new ProcessDefinitionID("test:1"));
        
        this.repository.addProcessDefinition(mockDefinition);
        Assert.assertTrue(this.navigator.getRunningInstances().isEmpty());
        this.navigator.startProcessInstance(mockDefinition.getID());
        Assert.assertFalse(this.navigator.getRunningInstances().isEmpty());
        
        AbstractProcessInstance dereferencedInstance = mock(AbstractProcessInstance.class);
        
        //
        // same id - resolution
        //
        when(dereferencedInstance.getID()).thenReturn(this.instance.getID());
        
        Assert.assertSame(this.resolver.resolveInstance(dereferencedInstance), this.instance);
    }
    
    /**
     * Tests the failing resolution of {@link AbstractProcessInstance}.
     */
    @Test(dependsOnMethods = "testInstanceResolution", expectedExceptions = DereferencedObjectException.class)
    public void testFailingInstanceResolution() {
        
        AbstractProcessInstance dereferencedInstance = mock(AbstractProcessInstance.class);
        
        //
        // different id - no resolution
        //
        when(dereferencedInstance.getID()).thenReturn(UUID.randomUUID());
        Assert.assertFalse(this.instance.equals(dereferencedInstance));
        
        //
        // cause an error
        //
        this.resolver.resolveInstance(dereferencedInstance);
    }
    
    /**
     * Tests the proper resolution of a {@link InterruptedInstance}.
     */
    @Test
    public void testInterruptedInstanceResolution() {
        
        InterruptedInstance dereferencedInterruptedInstance = mock(InterruptedInstance.class);
        
        //
        // same id - resolution
        //
        when(dereferencedInterruptedInstance.getID()).thenReturn(this.interruptedInstance.getID());
        Assert.assertTrue(this.interruptedInstance.equals(dereferencedInterruptedInstance));
        
        Assert.assertSame(
            this.resolver.resolveInterruptedInstance(dereferencedInterruptedInstance),
            this.interruptedInstance);
    }
    
    /**
     * Tests the failing resolution of {@link InterruptedInstance}.
     */
    @Test(
        dependsOnMethods = "testInterruptedInstanceResolution",
        expectedExceptions = DereferencedObjectException.class
    )
    public void testFailingInterruptedInstanceResolution() {
        
        InterruptedInstance dereferencedInterruptedInstance = mock(InterruptedInstance.class);
        
        //
        // different id - no resolution
        //
        when(dereferencedInterruptedInstance.getID()).thenReturn(UUID.randomUUID());
        Assert.assertFalse(this.interruptedInstance.equals(dereferencedInterruptedInstance));
        
        //
        // cause an error
        //
        this.resolver.resolveInterruptedInstance(dereferencedInterruptedInstance);
    }
}
