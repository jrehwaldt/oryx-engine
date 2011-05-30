package org.jodaengine.ext.debugging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.io.IOUtils;
import org.jodaengine.JodaEngineServices;
import org.jodaengine.RepositoryService;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.ext.Extension;
import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.api.BreakpointService;
import org.jodaengine.ext.debugging.api.DebuggerArtifactService;
import org.jodaengine.ext.debugging.api.DebuggerService;
import org.jodaengine.ext.debugging.rest.DebuggerWebService;
import org.jodaengine.ext.debugging.shared.DebuggerAttribute;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link DebuggerService} implementation providing the possibility to set
 * {@link Breakpoint}s and debug instances of {@link ProcessDefinition}s.
 * 
 * This class implements both, the {@link DebuggerService} as well as the {@link BreakpointService}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-19
 */
@Extension(value = DebuggerService.DEBUGGER_SERVICE_NAME, webServices = DebuggerWebService.class)
public class DebuggerServiceImpl implements DebuggerService, BreakpointService, DebuggerArtifactService {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private JodaEngineServices engineServices;
    private Navigator navigator;
    private RepositoryService repository;
    
    private Map<ProcessDefinition, List<Breakpoint>> breakpoints;
    
    private boolean running = false;
    
    @Override
    public void start(JodaEngineServices services) {
        
        //
        // skip method if the service is already running
        //
        if (this.running) {
            return;
        }
        
        logger.info("Starting the DebuggerService");
        this.engineServices = services;
        this.navigator = services.getNavigatorService();
        this.repository = services.getRepositoryService();
        this.breakpoints = new HashMap<ProcessDefinition, List<Breakpoint>>();
        
        this.running = true;
    }

    @Override
    public void stop() {
        
        //
        // skip method if the service is already stopped
        //
        if (this.running) {
            return;
        }
        
        logger.info("Stopping the DebuggerService");
        this.engineServices = null;
        this.navigator = null;
        
        this.running = false;
    }
    
    @Override
    public boolean isRunning() {
        return this.running;
    }
    
    //=================================================================
    //=================== DebuggerService methods =====================
    //=================================================================

    @Override
    public void stepOverInstance(AbstractProcessInstance instance) {
        // TODO Auto-generated method stub
        logger.debug("Step over instance {}", instance);
    }

    @Override
    public void termianteInstance(AbstractProcessInstance instance) {
        // TODO Auto-generated method stub
        logger.debug("Terminate instance {}", instance);
    }

    @Override
    public void resumeInstance(AbstractProcessInstance instance) {
        // TODO Auto-generated method stub
        logger.debug("Resume instance {}", instance);
    }

    @Override
    public void continueInstance(AbstractProcessInstance instance) {
        // TODO Auto-generated method stub
        logger.debug("Continue instance {}", instance);
    }
    
    //=================================================================
    //=================== BreakpointService methods ===================
    //=================================================================

    @Override
    public Breakpoint addBreakpoint(Node node) {
        logger.debug("Add breakpoint to node {}", node);
        
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void removeBreakpoint(Breakpoint breakpoint) {
        // TODO Auto-generated method stub
        logger.debug("Remove breakpoint {}", breakpoint);
    }

    @Override
    public void enableBreakpoint(Breakpoint breakpoint) {
        // TODO Auto-generated method stub
        logger.debug("Enable breakpoint {}", breakpoint);
    }

    @Override
    public void disableBreakpoint(Breakpoint breakpoint) {
        // TODO Auto-generated method stub
        logger.debug("Disable breakpoint {}", breakpoint);
    }
    
    //=================================================================
    //=================== DebuggerArtifactService methods =============
    //=================================================================
    
    @Override
    public String getSvgArtifact(ProcessDefinition definition)
    throws ProcessArtifactNotFoundException, DefinitionNotFoundException {
        
        DebuggerAttribute attribute = DebuggerAttribute.getAttribute(definition);
        
        String artifactID = null;
        
        //
        // get the artifact name from the debugger context
        //
        if (attribute != null) {
            artifactID = attribute.getSvgArtifact();
            
            if (artifactID != null) {
                AbstractProcessArtifact artifact = this.repository.getProcessArtifact(
                    DEBUGGER_ARTIFACT_NAMESPACE + artifactID, definition.getID());
                
                try {
                    return IOUtils.toString(artifact.getInputStream());
                } catch (IOException ioe) {
                    logger.error("Unable read artifact stream.", ioe);
                }
            }
        }
        
        artifactID = "svg-artifact-for-" + definition.getID().getIdentifier() + "-not-defined";
        throw new ProcessArtifactNotFoundException(artifactID);
    }
    
    //=================================================================
    //=================== Intern methods ==============================
    //=================================================================
    
    /**
     * Registers a list of {@link Breakpoint}s for a certain {@link ProcessDefinition}.
     * 
     * @param breakpoints the breakpoints to register
     * @param definition the process definition, the breakpoint belong to
     */
    public void registerBreakpoints(@Nonnull List<Breakpoint> breakpoints,
                                    @Nonnull ProcessDefinition definition) {
        
        logger.info("Registering {} breakpoints for {}", breakpoints.size(), definition);
        this.breakpoints.put(definition, breakpoints);
    }
    
    /**
     * Unregisters all {@link Breakpoint}s for a certain {@link ProcessDefinition}.
     * 
     * @param definition the process definition
     */
    public void unregisterBreakpoints(@Nonnull ProcessDefinition definition) {
        
        logger.info("Unregistering breakpoints for {}", definition);
        this.breakpoints.remove(definition);
    }
    
    /**
     * Returns a possibly registered breakpoint.
     * 
     * @param node the {@link Node}, for which the breakpoint should be registered
     * @param instance the {@link AbstractProcessInstance}
     * @return a list of {@link Breakpoint}s
     */
    public @Nonnull List<Breakpoint> getBreakpoints(@Nonnull Node node,
                                                    @Nonnull AbstractProcessInstance instance) {
        
        //
        // are there any breakpoints?
        //
        ProcessDefinition definition = instance.getDefinition();
        List<Breakpoint> nodeBreakpoints = new ArrayList<Breakpoint>();
        
        if (!this.breakpoints.containsKey(definition)) {
            return nodeBreakpoints;
        }
        
        //
        // does any of the breakpoints match?
        //
        for (Breakpoint breakpoint: this.breakpoints.get(definition)) {
            if (breakpoint.getNode().equals(node)) {
                nodeBreakpoints.add(breakpoint);
            }
        }
        
        return nodeBreakpoints;
    }
    
    /**
     * Indicates that a breakpoint matched.
     * 
     * @param token the {@link Token}, which matched a breakpoint
     * @param breakpoint the {@link Breakpoint}, which was matched
     */
    public void breakpointMatched(@Nonnull Token token,
                                  @Nonnull Breakpoint breakpoint) {
        
        //
        // TODO Jan crazy stuff: suspend token, remember state and token, ...
        //
        
    }
}
