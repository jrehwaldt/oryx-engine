package org.jodaengine.ext.debugging;

import javax.annotation.Nonnull;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.RepositoryService;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.ServiceUnavailableException;
import org.jodaengine.ext.Extension;
import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.api.DebuggerService;
import org.jodaengine.ext.debugging.api.ReferenceResolverService;
import org.jodaengine.ext.debugging.rest.DereferencedObjectException;
import org.jodaengine.ext.service.ExtensionNotAvailableException;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.ControlFlow;
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
                            Node dereferencedNode) {
        
        if (this.repository == null) {
            throw new ServiceUnavailableException(RepositoryService.class);
        }
        
        for (Node node: definition.getStartNodes()) {
            Node tmp = rereferenceNode(node, dereferencedNode);
            
            if (tmp != null) {
                return tmp;
            }
        }
        
        throw new DereferencedObjectException(Node.class, dereferencedNode.getID());
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
    public Breakpoint resolveBreakpoint(Breakpoint dereferencedBreakpoint) {
        
        if (this.debugger == null) {
            throw new ServiceUnavailableException(DebuggerService.class);
        }
        
        for (Breakpoint breakpoint: this.debugger.getBreakpoints()) {
            if (breakpoint.equals(dereferencedBreakpoint)) {
                return breakpoint;
            }
        }
        
        throw new DereferencedObjectException(Breakpoint.class, dereferencedBreakpoint.getID());
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
     * @param dereferencedNode the node, which is required
     * @return the node if found, or null
     */
    private @Nonnull Node rereferenceNode(@Nonnull Node initialNode,
                                          @Nonnull Node dereferencedNode) {
        
        if (initialNode.equals(dereferencedNode)) {
            return initialNode;
        }
        
        for (ControlFlow controlFlow: initialNode.getOutgoingControlFlows()) {
            Node tmp = rereferenceNode(controlFlow.getDestination(), dereferencedNode);
            
            if (tmp != null) {
                return tmp;
            }
        }
        
        return null;
    }
    
}
