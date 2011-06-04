package org.jodaengine.ext.debugging.rest;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.exception.ServiceUnavailableException;
import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.api.BreakpointService;
import org.jodaengine.ext.debugging.api.DebuggerArtifactService;
import org.jodaengine.ext.debugging.api.DebuggerService;
import org.jodaengine.ext.debugging.api.InterruptedInstance;
import org.jodaengine.ext.debugging.api.NodeBreakpoint;
import org.jodaengine.ext.debugging.api.ReferenceResolverService;
import org.jodaengine.ext.service.ExtensionNotAvailableException;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The rest web service for our {@link DebuggerService}.
 * 
 * It is based on JAX-RS and mapped via {@link Path} annotation
 * as well as deployment descriptor definition in <i>web.xml</i>.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-17
 */
@Path("/debugger")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class DebuggerWebService implements DebuggerService, BreakpointService, DebuggerArtifactService {

    private static final String NOT_ACCESSIBLE_VIA_WEBSERVICE = "This method is not accessible via web service.";
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private DebuggerService debugger;
    private ReferenceResolverService resolver;
    private final JodaEngineServices engineServices;
    
    /**
     * Creates the {@link DebuggerWebService} rest API for the engine's {@link DebuggerService}.
     * 
     * @param engineServices the engine's loaded services
     */
    public DebuggerWebService(@Nonnull JodaEngineServices engineServices) {
        logger.info("Starting Debugger REST API");
        this.engineServices = engineServices;
        
        ExtensionService extensionService = this.engineServices.getExtensionService();
        try {
            this.debugger = extensionService.getExtensionService(DebuggerService.class, DEBUGGER_SERVICE_NAME);
            this.resolver = extensionService.getExtensionService(
                ReferenceResolverService.class, ReferenceResolverService.RESOLVER_SERVICE_NAME);
        } catch (ExtensionNotAvailableException e) {
            logger.error("The Debugger REST API will be unavailable. No Debugger/Resolver Service found.");
            this.debugger = null;
        }
    }

    @Path("/status/is-running")
    @GET
    @Override
    public boolean isRunning() {
        if (this.debugger != null) {
            return this.debugger.isRunning();
        }
        
        return false;
    }
    
    @Override
    public void start(JodaEngineServices services) {
        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }
    
    @Override
    public void stop() {
        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }
    
    //=================================================================
    //=================== DebuggerService methods =====================
    //=================================================================
    
    @Path("/instance/step-over")
    @POST
    @Override
    public void stepOverInstance(InterruptedInstance targetInstance) {
        if (this.debugger != null) {
            this.debugger.stepOverInstance(targetInstance);
        }
        
        throw new ServiceUnavailableException(DebuggerService.class);
    }

    @Path("/instance/terminate")
    @POST
    @Override
    public void terminateInstance(InterruptedInstance targetInstance) {
        if (this.debugger != null) {
            this.debugger.terminateInstance(targetInstance);
        }
        throw new ServiceUnavailableException(DebuggerService.class);
    }

    @Path("/instance/resume")
    @POST
    @Override
    public void resumeInstance(InterruptedInstance targetInstance) {
        if (this.debugger != null) {
            this.debugger.resumeInstance(targetInstance);
        }
        throw new ServiceUnavailableException(DebuggerService.class);
    }

    @Path("/instance/continue")
    @POST
    @Override
    public void continueInstance(InterruptedInstance targetInstance) {
        if (this.debugger != null) {
            this.debugger.continueInstance(targetInstance);
        }
        throw new ServiceUnavailableException(DebuggerService.class);
    }
    
    @Override
    public Collection<InterruptedInstance> getInterruptedInstances() {
        if (this.debugger != null) {
            return this.debugger.getInterruptedInstances();
        }
        throw new ServiceUnavailableException(DebuggerService.class);
    }
    
    //=================================================================
    //=================== BreakpointService methods ===================
    //=================================================================

    @Path("/breakpoints/create")
    @POST
    @Override
    public NodeBreakpoint createNodeBreakpoint(ProcessDefinition dereferencedTargetDefinition,
                                               Node dereferencedTargetNode,
                                               ActivityState targetActivityState,
                                               String juelCondition)
    throws DefinitionNotFoundException {
        
        if (this.debugger != null) {
            ProcessDefinition definition = resolver.resolveDefinition(dereferencedTargetDefinition);
            Node node = resolver.resolveNode(definition, dereferencedTargetNode);
            return this.debugger.createNodeBreakpoint(definition, node, targetActivityState, juelCondition);
        }
        throw new ServiceUnavailableException(DebuggerService.class);
    }

    @Path("/breakpoints/remove")
    @POST
    @Override
    public boolean removeBreakpoint(Breakpoint dereferencedBreakpoint) {
        
        if (this.debugger != null) {
            Breakpoint breakpoint = resolver.resolveBreakpoint(dereferencedBreakpoint);
            return this.debugger.removeBreakpoint(breakpoint);
        }
        throw new ServiceUnavailableException(DebuggerService.class);
    }

    @Path("/breakpoints/enable")
    @POST
    @Override
    public Breakpoint enableBreakpoint(Breakpoint dereferencedBreakpoint) {
        
        if (this.debugger != null) {
            Breakpoint breakpoint = this.resolver.resolveBreakpoint(dereferencedBreakpoint);
            return this.debugger.enableBreakpoint(breakpoint);
        }
        
        throw new ServiceUnavailableException(DebuggerService.class);
    }

    @Path("/breakpoints/disable")
    @POST
    @Override
    public Breakpoint disableBreakpoint(Breakpoint dereferencedBreakpoint) {
        
        if (this.debugger != null) {
            Breakpoint breakpoint = this.resolver.resolveBreakpoint(dereferencedBreakpoint);
            return this.debugger.disableBreakpoint(breakpoint);
        }
        
        throw new ServiceUnavailableException(DebuggerService.class);
    }
    
    @Path("/breakpoints")
    @GET
    @Override
    public Collection<Breakpoint> getBreakpoints() {
        
        if (this.debugger != null) {
            return this.debugger.getBreakpoints();
        }
        
        throw new ServiceUnavailableException(DebuggerService.class);
    }

    //=================================================================
    //=================== DebuggerArtifactService methods =============
    //=================================================================
    @Path("/artifacts")
    @GET
    @Override
    @Produces(MediaType.APPLICATION_SVG_XML)
    public String getSvgArtifact(ProcessDefinition definition)
    throws ProcessArtifactNotFoundException, DefinitionNotFoundException {
        
        if (this.debugger != null) {
            this.debugger.getSvgArtifact(definition);
        }
        
        throw new ServiceUnavailableException(DebuggerService.class);
    }
}
