package org.jodaengine.ext.debugging.rest;

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
import org.jodaengine.ext.service.ExtensionNotAvailableException;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.process.definition.ProcessDefinition;
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
    private final JodaEngineServices engineServices;
    
    /**
     * Creates the {@link DebuggerWebService} rest API for the engine's {@link DebuggerService}.
     * 
     * @param engineServices the engine's loaded services
     */
    public DebuggerWebService(@Nonnull JodaEngineServices engineServices) {
        logger.info("Starting Debugger REST API");
        this.engineServices = engineServices;
        
        ExtensionService extService = this.engineServices.getExtensionService();
        try {
            this.debugger = extService.getExtensionService(DebuggerService.class, DEBUGGER_SERVICE_NAME);
        } catch (ExtensionNotAvailableException e) {
            logger.error("The Debugger REST API will be unavailable. No Debugger Service found.");
            this.debugger = null;
        }
    }
    
    //=================================================================
    //=================== DebuggerService methods =====================
    //=================================================================
    
    @Path("/instance/step-over")
    @POST
    @Override
    public void stepOverInstance(AbstractProcessInstance instance) {
        if (this.debugger != null) {
            this.debugger.stepOverInstance(instance);
        }
        
        throw new ServiceUnavailableException(DebuggerService.class);
    }

    @Path("/instance/terminate")
    @POST
    @Override
    public void termianteInstance(AbstractProcessInstance instance) {
        if (this.debugger != null) {
            this.debugger.termianteInstance(instance);
        }
        throw new ServiceUnavailableException(DebuggerService.class);
    }

    @Path("/instance/resume")
    @POST
    @Override
    public void resumeInstance(AbstractProcessInstance instance) {
        if (this.debugger != null) {
            this.debugger.resumeInstance(instance);
        }
        throw new ServiceUnavailableException(DebuggerService.class);
    }

    @Path("/instance/continue")
    @POST
    @Override
    public void continueInstance(AbstractProcessInstance instance) {
        if (this.debugger != null) {
            this.debugger.continueInstance(instance);
        }
        throw new ServiceUnavailableException(DebuggerService.class);
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
    //=================== BreakpointService methods ===================
    //=================================================================

    @Path("/breakpoints/create")
    @POST
    @Override
    public Breakpoint createBreakpoint(Node node) {
        if (this.debugger != null) {
            return this.debugger.createBreakpoint(node);
        }
        throw new ServiceUnavailableException(DebuggerService.class);
    }

    @Path("/breakpoints/remove")
    @POST
    @Override
    public void removeBreakpoint(Breakpoint breakpoint) {
        if (this.debugger != null) {
            this.debugger.removeBreakpoint(breakpoint);
        }
        throw new ServiceUnavailableException(DebuggerService.class);
    }

    @Path("/breakpoints/enable")
    @POST
    @Override
    public void enableBreakpoint(Breakpoint breakpoint) {
        if (this.debugger != null) {
            this.debugger.enableBreakpoint(breakpoint);
        }
        
        throw new ServiceUnavailableException(DebuggerService.class);
    }

    @Path("/breakpoints/disable")
    @POST
    @Override
    public void disableBreakpoint(Breakpoint breakpoint) {
        if (this.debugger != null) {
            this.debugger.disableBreakpoint(breakpoint);
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
