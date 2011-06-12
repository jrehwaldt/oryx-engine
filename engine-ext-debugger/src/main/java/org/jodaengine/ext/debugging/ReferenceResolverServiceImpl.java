package org.jodaengine.ext.debugging;

import java.util.UUID;

import javax.annotation.Nonnull;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.RepositoryService;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.ServiceUnavailableException;
import org.jodaengine.ext.Extension;
import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.api.DebuggerService;
import org.jodaengine.ext.debugging.api.InterruptedInstance;
import org.jodaengine.ext.debugging.api.ReferenceResolverService;
import org.jodaengine.ext.debugging.rest.DereferencedObjectException;
import org.jodaengine.ext.service.ExtensionNotAvailableException;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.structure.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This {@link ReferenceResolverService} helps to resolve dereferenced instances,
 * such as a {@link ProcessDefinition} or a {@link Node}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-31
 */
@Extension(ReferenceResolverService.RESOLVER_SERVICE_NAME)
public class ReferenceResolverServiceImpl implements ReferenceResolverService {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private RepositoryService repository;
    private DebuggerService debugger;
    private Navigator navigator;
    
    private boolean running = false;
    
    //=================================================================
    //=================== Service methods =============================
    //=================================================================
    
    @Override
    public void start(JodaEngineServices services) {
        
        //
        // skip method if the service is already running
        //
        if (this.running) {
            return;
        }
        
        logger.info("Starting the ReferenceResolverService");
        ExtensionService extensionService = services.getExtensionService();
        
        this.repository = services.getRepositoryService();
        this.navigator = services.getNavigatorService();
        try {
            this.debugger = extensionService.getExtensionService(
                DebuggerService.class, DebuggerService.DEBUGGER_SERVICE_NAME);
        } catch (ExtensionNotAvailableException enae) {
            logger.error("The Breakpoint Resolver will be unavailable. No Debugger Service found.");
            this.debugger = null;
        }
        
        this.running = true;
    }

    @Override
    public void stop() {
        
        //
        // skip method if the service is already stopped
        //
        if (!this.running) {
            return;
        }
        
        logger.info("Stopping the ReferenceResolverService");
        this.repository = null;
        this.debugger = null;
        this.navigator = null;
        
        this.running = false;
    }
    
    @Override
    public boolean isRunning() {
        return this.running;
    }
    
    //=================================================================
    //=================== ResolverService methods =====================
    //=================================================================

    @Override
    public Node resolveNode(ProcessDefinition definition,
                            UUID dereferencedNodeID) {
        
        if (this.repository == null) {
            throw new ServiceUnavailableException(RepositoryService.class);
        }
        
        for (Node node: definition.getStartNodes()) {
            Node tmp = rereferenceNode(node, dereferencedNodeID);
            
            if (tmp != null) {
                return tmp;
            }
        }
        
        throw new DereferencedObjectException(Node.class, dereferencedNodeID);
    }
    
    @Override
    public Node resolveNode(ProcessDefinition definition,
                            Node dereferencedNode) {
        
        return resolveNode(definition, dereferencedNode.getID());
    }
    
    @Override
    public ProcessDefinition resolveDefinition(ProcessDefinitionID dereferencedDefinitionID)
    throws DefinitionNotFoundException {
        
        if (this.repository == null) {
            throw new ServiceUnavailableException(RepositoryService.class);
        }
        
        return this.repository.getProcessDefinition(dereferencedDefinitionID);
    }
    
    @Override
    public ProcessDefinition resolveDefinition(ProcessDefinition dereferencedDefinition)
    throws DefinitionNotFoundException {
        
        return resolveDefinition(dereferencedDefinition.getID());
    }
    
    @Override
    public Breakpoint resolveBreakpoint(UUID dereferencedBreakpointID) {
        
        if (this.debugger == null) {
            throw new ServiceUnavailableException(DebuggerService.class);
        }
        
        for (Breakpoint breakpoint: this.debugger.getBreakpoints()) {
            if (breakpoint.getID().equals(dereferencedBreakpointID)) {
                return breakpoint;
            }
        }
        
        throw new DereferencedObjectException(Breakpoint.class, dereferencedBreakpointID);
    }
    
    @Override
    public Breakpoint resolveBreakpoint(Breakpoint dereferencedBreakpoint) {
        
        return resolveBreakpoint(dereferencedBreakpoint.getID());
    }
    
    @Override
    public AbstractProcessInstance resolveInstance(UUID dereferencedInstanceID) {
        
        if (this.navigator == null) {
            throw new ServiceUnavailableException(Navigator.class);
        }
        
        for (AbstractProcessInstance instance: this.navigator.getRunningInstances()) {
            if (instance.getID().equals(dereferencedInstanceID)) {
                return instance;
            }
        }
        
        throw new DereferencedObjectException(AbstractProcessInstance.class, dereferencedInstanceID);
    }

    @Override
    public InterruptedInstance resolveInterruptedInstance(UUID dereferencedInterruptedInstanceID) {
        
        if (this.debugger == null) {
            throw new ServiceUnavailableException(DebuggerService.class);
        }
        
        for (InterruptedInstance instance: this.debugger.getInterruptedInstances()) {
            if (instance.getID().equals(dereferencedInterruptedInstanceID)) {
                return instance;
            }
        }
        
        throw new DereferencedObjectException(InterruptedInstance.class, dereferencedInterruptedInstanceID);
    }

    @Override
    public AbstractProcessInstance resolveInstance(AbstractProcessInstance dereferencedInstance) {
        
        return resolveInstance(dereferencedInstance.getID());
    }

    @Override
    public InterruptedInstance resolveInterruptedInstance(InterruptedInstance dereferencedInterruptedInst) {
        
        return resolveInterruptedInstance(dereferencedInterruptedInst.getID());
    }
    
    //=================================================================
    //=================== Intern methods ==============================
    //=================================================================
    /**
     * A helper method allowing to traverse the node's graph and search for a certain {@link Node}.
     * 
     * This method works recursively. Calling it with cyclic node-{@link ControlFlow}-node-references will,
     * of course, cause a {@link StackOverflowError}.
     * 
     * @param initialNode the searching start point
     * @param dereferencedNodeID the node's, which is required
     * @return the node if found, or null
     */
    private @Nonnull Node rereferenceNode(@Nonnull Node initialNode,
                                          @Nonnull UUID dereferencedNodeID) {
        
        if (initialNode.getID().equals(dereferencedNodeID)) {
            return initialNode;
        }
        
        for (ControlFlow controlFlow: initialNode.getOutgoingControlFlows()) {
            Node tmp = rereferenceNode(controlFlow.getDestination(), dereferencedNodeID);
            
            if (tmp != null) {
                return tmp;
            }
        }
        
        return null;
    }
}
