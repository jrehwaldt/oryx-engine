package org.jodaengine.ext.debugging.rest;

import javax.annotation.Nonnull;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.exception.ServiceUnavailableException;
import org.jodaengine.ext.debugging.Breakpoint;
import org.jodaengine.ext.debugging.api.BreakpointService;
import org.jodaengine.ext.debugging.api.DebuggerService;
import org.jodaengine.ext.service.ExtensionNotAvailableException;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The rest web service for our {@link DebuggerService}.
 * 
 * It is based on JAX-RS and used as {@link Provider},
 * which is mapped as annotated as well as defined in <i>web.xml</i>.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-17
 */
@Path("/debugger")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class DebuggerWebService implements DebuggerService, BreakpointService {

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

    @Override
    public void stepOverInstance(AbstractProcessInstance instance) {
        if (this.debugger != null) {
            this.debugger.stepOverInstance(instance);
        }
        
        throw new ServiceUnavailableException(DebuggerService.class);
    }

    @Override
    public void termianteInstance(AbstractProcessInstance instance) {
        if (this.debugger != null) {
            this.debugger.termianteInstance(instance);
        }
        throw new ServiceUnavailableException(DebuggerService.class);
    }

    @Override
    public void resumeInstance(AbstractProcessInstance instance) {
        if (this.debugger != null) {
            this.debugger.resumeInstance(instance);
        }
        throw new ServiceUnavailableException(DebuggerService.class);
    }

    @Override
    public void continueInstance(AbstractProcessInstance instance) {
        if (this.debugger != null) {
            this.debugger.continueInstance(instance);
        }
        throw new ServiceUnavailableException(DebuggerService.class);
    }
    
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
    
    @Override
    public Breakpoint addBreakpoint(Node node) {
        if (this.debugger != null) {
            return this.debugger.addBreakpoint(node);
        }
        throw new ServiceUnavailableException(DebuggerService.class);
    }

    @Override
    public Breakpoint addBreakpoint(Node node, AbstractProcessInstance instance) {
        if (this.debugger != null) {
            return this.debugger.addBreakpoint(node, instance);
        }
        throw new ServiceUnavailableException(DebuggerService.class);
    }

    @Override
    public void removeBreakpoint(Breakpoint breakpoint) {
        if (this.debugger != null) {
            this.debugger.removeBreakpoint(breakpoint);
        }
        throw new ServiceUnavailableException(DebuggerService.class);
    }

    @Override
    public void enableBreakpoint(Breakpoint breakpoint) {
        if (this.debugger != null) {
            this.debugger.enableBreakpoint(breakpoint);
        }
        
        throw new ServiceUnavailableException(DebuggerService.class);
    }

    @Override
    public void disableBreakpoint(Breakpoint breakpoint) {
        if (this.debugger != null) {
            this.debugger.disableBreakpoint(breakpoint);
        }
        
        throw new ServiceUnavailableException(DebuggerService.class);
    }
    
}