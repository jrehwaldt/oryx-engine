package org.jodaengine.ext.debugging.rest;

import java.util.Collection;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
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
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.instance.AbstractProcessInstance;
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
        
        if (this.debugger == null) {
            throw new ServiceUnavailableException(DebuggerService.class);
        }
        
        this.debugger.stepOverInstance(targetInstance);
    }

    @Path("/instance/terminate")
    @POST
    @Override
    public void terminateInstance(InterruptedInstance targetInstance) {
        
        if (this.debugger == null) {
            throw new ServiceUnavailableException(DebuggerService.class);
        }
        
        this.debugger.terminateInstance(targetInstance);
    }

    @Path("/instance/resume")
    @POST
    @Override
    public void resumeInstance(InterruptedInstance targetInstance) {
        
        if (this.debugger == null) {
            throw new ServiceUnavailableException(DebuggerService.class);
        }
        
        this.debugger.resumeInstance(targetInstance);
    }

    @Path("/instance/continue")
    @POST
    @Override
    public void continueInstance(InterruptedInstance targetInstance) {
        
        if (this.debugger == null) {
            throw new ServiceUnavailableException(DebuggerService.class);
        }
        
        this.debugger.continueInstance(targetInstance);
    }
    
    @Override
    public Collection<InterruptedInstance> getInterruptedInstances() {
        
        if (this.debugger == null) {
            throw new ServiceUnavailableException(DebuggerService.class);
        }
        
        return this.debugger.getInterruptedInstances();
    }
    
    //=================================================================
    //=================== BreakpointService methods ===================
    //=================================================================
    
    /**
     * Helper method mapping the rest interface to the underlying api.
     * 
     * @param definitionID the target {@link ProcessDefinition}'s id
     * @param nodeID the target {@link Node}'s id
     * @param targetActivityState the {@link ActivityState}
     * @param juelCondition the condition
     * 
     * @return the created {@link Breakpoint}
     * 
     * @throws DefinitionNotFoundException in case the definition is not found
     */
    @Path("/breakpoints/create")
    @POST
    public NodeBreakpoint createNodeBreakpoint(@Nonnull @QueryParam("definitionId") ProcessDefinitionID definitionID,
                                               @Nonnull @QueryParam("nodeId") UUID nodeID,
                                               @Nonnull @QueryParam("activityState") ActivityState targetActivityState,
                                               @Nullable @QueryParam("juelCondition") String juelCondition)
    throws DefinitionNotFoundException {
        
        if (this.debugger == null) {
            throw new ServiceUnavailableException(DebuggerService.class);
        }
        
        ProcessDefinition definition = this.resolver.resolveDefinition(definitionID);
        Node node = this.resolver.resolveNode(definition, nodeID);
        
        return createNodeBreakpoint(definition, node, targetActivityState, juelCondition);
    }
    
    @Override
    public NodeBreakpoint createNodeBreakpoint(ProcessDefinition dereferencedTargetDefinition,
                                               Node dereferencedTargetNode,
                                               ActivityState targetActivityState,
                                               String juelCondition)
    throws DefinitionNotFoundException {
        
        ProcessDefinition definition = resolver.resolveDefinition(dereferencedTargetDefinition);
        Node node = resolver.resolveNode(definition, dereferencedTargetNode);
        return this.debugger.createNodeBreakpoint(definition, node, targetActivityState, juelCondition);
    }

    @Path("/breakpoints/remove")
    @DELETE
    @Override
    public boolean removeBreakpoint(Breakpoint dereferencedBreakpoint) {
        
        if (this.debugger == null) {
            throw new ServiceUnavailableException(DebuggerService.class);
        }
        
        Breakpoint breakpoint = resolver.resolveBreakpoint(dereferencedBreakpoint);
        return this.debugger.removeBreakpoint(breakpoint);
    }

    @Path("/breakpoints/enable")
    @POST
    @Override
    public Breakpoint enableBreakpoint(Breakpoint dereferencedBreakpoint) {
        
        if (this.debugger == null) {
            throw new ServiceUnavailableException(DebuggerService.class);
        }
        
        Breakpoint breakpoint = this.resolver.resolveBreakpoint(dereferencedBreakpoint);
        return this.debugger.enableBreakpoint(breakpoint);
    }

    @Path("/breakpoints/disable")
    @POST
    @Override
    public Breakpoint disableBreakpoint(Breakpoint dereferencedBreakpoint) {
        
        if (this.debugger == null) {
            throw new ServiceUnavailableException(DebuggerService.class);
        }
        
        Breakpoint breakpoint = this.resolver.resolveBreakpoint(dereferencedBreakpoint);
        return this.debugger.disableBreakpoint(breakpoint);
    }
    
    /**
     * Helper method mapping the rest interface to the underlying api.
     * 
     * @param dereferencedDefinitionID the {@link ProcessDefinition}'s id
     * @return the related {@link Breakpoint}s
     * 
     * @throws DefinitionNotFoundException definition not found
     */
    @Path("/definitions/{definition-id}/breakpoints")
    @GET
    public Collection<Breakpoint> getBreakpoints(
            @Nonnull @PathParam("definition-id") ProcessDefinitionID dereferencedDefinitionID)
            throws DefinitionNotFoundException {
        
        if (this.debugger == null) {
            throw new ServiceUnavailableException(DebuggerService.class);
        }
        
        ProcessDefinition definition = this.resolver.resolveDefinition(dereferencedDefinitionID);
        return getBreakpoints(definition);
    }
    
    @Override
    public Collection<Breakpoint> getBreakpoints(ProcessDefinition definition) {
        
        return this.debugger.getBreakpoints(definition);
    }
    
    /**
     * Helper method mapping the rest interface to the underlying api.
     * 
     * @param dereferencedInstanceID the {@link AbstractProcessInstance}'s id
     * @return the related {@link Breakpoint}s
     */
    @Path("/instance/{instance-id}/breakpoints")
    @GET
    public Collection<Breakpoint> getBreakpoints(@Nonnull @PathParam("instance-id") UUID dereferencedInstanceID) {
        
        if (this.debugger == null) {
            throw new ServiceUnavailableException(DebuggerService.class);
        }
        
        AbstractProcessInstance instance = this.resolver.resolveInstance(dereferencedInstanceID);
        return getBreakpoints(instance);
    }
    
    @Override
    public Collection<Breakpoint> getBreakpoints(AbstractProcessInstance instance) {
        
        return this.debugger.getBreakpoints(instance);
    }
    
    @Path("/breakpoints")
    @GET
    @Override
    public Collection<Breakpoint> getBreakpoints() {
        
        if (this.debugger == null) {
            throw new ServiceUnavailableException(DebuggerService.class);
        }
        
        return this.debugger.getBreakpoints();
    }

    //=================================================================
    //=================== DebuggerArtifactService methods =============
    //=================================================================
    @Override
    public String getSvgArtifact(ProcessDefinition definition)
    throws ProcessArtifactNotFoundException, DefinitionNotFoundException {
        
        return this.debugger.getSvgArtifact(definition);
    }
    
    @Override
    public void setSvgArtifact(ProcessDefinition definition,
                               String svgArtifact) {
        
        this.debugger.setSvgArtifact(definition, svgArtifact);
    }
    
    /**
     * Helper method mapping the rest parameter to the implemented api.
     * 
     * @param dereferencedDefinitionID the process' definition id
     * @return svgArtifact the artifact to set
     * @throws DefinitionNotFoundException in case the definition is unknown
     * @throws ProcessArtifactNotFoundException in case no artifact is found
     */
    @Path("/artifacts/{definitionID}/svg.svg")
    @GET
    @Produces("image/svg+xml")
    public String getSvgArtifact2(@Nonnull @PathParam("definitionID") ProcessDefinitionID dereferencedDefinitionID)
    throws ProcessArtifactNotFoundException, DefinitionNotFoundException {
        
        return getSvgArtifact(dereferencedDefinitionID);
    }
    
    /**
     * Helper method mapping the rest parameter to the implemented api.
     * 
     * @param dereferencedDefinitionID the process' definition id
     * @return svgArtifact the artifact to set
     * @throws DefinitionNotFoundException in case the definition is unknown
     * @throws ProcessArtifactNotFoundException in case no artifact is found
     */
    @Path("/artifacts/{definitionID}/svg")
    @GET
    @Produces("image/svg+xml")
    public String getSvgArtifact(@Nonnull @PathParam("definitionID") ProcessDefinitionID dereferencedDefinitionID)
    throws ProcessArtifactNotFoundException, DefinitionNotFoundException {
        
        if (this.debugger == null) {
            throw new ServiceUnavailableException(DebuggerService.class);
        }
        
        ProcessDefinition definition = this.resolver.resolveDefinition(dereferencedDefinitionID);
        return getSvgArtifact(definition);
    }
    
    /**
     * Helper method mapping the rest parameter to the implemented api.
     * 
     * @param dereferencedDefinitionID the process' definition id
     * @param formData the artifact data
     * @throws DefinitionNotFoundException in case the definition is unknown
     */
    @Path("/artifacts/{definitionID}/svg")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void setSvgArtifact(@Nonnull @PathParam("definitionID") ProcessDefinitionID dereferencedDefinitionID,
                               @Nullable @MultipartForm SvgArtifactFormData formData)
    throws DefinitionNotFoundException {
        
        if (this.debugger == null) {
            throw new ServiceUnavailableException(DebuggerService.class);
        }
        
        ProcessDefinition definition = this.resolver.resolveDefinition(dereferencedDefinitionID);
        setSvgArtifact(definition, formData.getSvgArtifactString());
    }
}
