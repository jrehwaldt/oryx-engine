package org.jodaengine.ext.debugging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import org.jodaengine.ext.debugging.listener.DebuggerTokenListener;
import org.jodaengine.ext.debugging.rest.DebuggerWebService;
import org.jodaengine.ext.debugging.shared.BreakpointImpl;
import org.jodaengine.ext.debugging.shared.DebuggerAttribute;
import org.jodaengine.ext.debugging.shared.JuelBreakpointCondition;
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
        if (!this.running) {
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
    public synchronized Breakpoint createBreakpoint(ProcessDefinition targetDefinition,
                                                    Node targetNode,
                                                    String juelCondition)
    throws DefinitionNotFoundException {
        logger.debug("Create a breakpoint for node {}", targetNode);
        
        Breakpoint breakpoint = new BreakpointImpl(targetNode);
        
        if (juelCondition != null) {
            breakpoint.setCondition(new JuelBreakpointCondition(juelCondition));
        }
        
        registerBreakpoints(Arrays.asList(breakpoint), targetDefinition);
        return breakpoint;
    }
    
    @Override
    public synchronized boolean removeBreakpoint(Breakpoint breakpoint) {
        
        logger.info("Remove breakpoint {}", breakpoint);
        
        //
        // we remove it from the local breakpoint registration map
        //
        for (Map.Entry<ProcessDefinition, List<Breakpoint>> entry: this.breakpoints.entrySet()) {
            
            //
            // remove it, if it is available in this map
            //
            boolean removed = entry.getValue().remove(breakpoint);
            if (removed) {
                logger.info("Removed breakpoint {} from {}", breakpoint, entry.getKey());
                return true;
            }
        }
        
        logger.info("Brekpoint {} could not be removed. It was not found.", breakpoint);
        return false;
    }

    @Override
    public Breakpoint enableBreakpoint(Breakpoint breakpoint) {
        
        logger.debug("Enable breakpoint {}", breakpoint);
        breakpoint.enable();
        return breakpoint;
    }

    @Override
    public Breakpoint disableBreakpoint(Breakpoint breakpoint) {
        
        logger.debug("Disable breakpoint {}", breakpoint);
        breakpoint.disable();
        return breakpoint;
    }
    
    @Override
    public List<Breakpoint> getAllBreakpoints() {
        
        List<Breakpoint> knownBreakpoints = new ArrayList<Breakpoint>();
        for (List<Breakpoint> tmp: breakpoints.values()) {
            knownBreakpoints.addAll(tmp);
        }
        return knownBreakpoints;
    }
    
    //=================================================================
    //=================== DebuggerArtifactService methods =============
    //=================================================================
    
    @Override
    public String getSvgArtifact(ProcessDefinition definition)
    throws ProcessArtifactNotFoundException, DefinitionNotFoundException {
        
        DebuggerAttribute attribute = DebuggerAttribute.getAttributeIfExists(definition);
        
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
        
        artifactID = "svg-artifact-for-" + definition.getID() + "-not-defined";
        throw new ProcessArtifactNotFoundException(artifactID);
    }
    
    //=================================================================
    //=================== Intern methods ==============================
    //=================================================================
    
    /**
     * Registers a list of {@link Breakpoint}s for a certain {@link ProcessDefinition}.
     * 
     * Any previously registered breakpoint will still be available.
     * 
     * @param breakpoints the breakpoints to register
     * @param definition the process definition, the breakpoint belong to
     */
    public synchronized void registerBreakpoints(@Nonnull List<Breakpoint> breakpoints,
                                                 @Nonnull ProcessDefinition definition) {
        
        logger.info("Registering {} breakpoints for {}", breakpoints.size(), definition);
        
        if (this.breakpoints.containsKey(definition)) {
            this.breakpoints.get(definition).addAll(breakpoints);
        } else {
            List<Breakpoint> registered = new ArrayList<Breakpoint>();
            registered.addAll(breakpoints);
            this.breakpoints.put(definition, registered);
        }
    }
    
    /**
     * Unregisters all {@link Breakpoint}s for a certain {@link ProcessDefinition}.
     * 
     * @param definition the process definition
     */
    public synchronized void unregisterAllBreakpoints(@Nonnull ProcessDefinition definition) {
        
        logger.info("Unregistering breakpoints for {}", definition);
        this.breakpoints.remove(definition);
    }
    
    /**
     * Returns a list of registered breakpoints for a certain process instance.
     * 
     * @param instance the {@link AbstractProcessInstance}
     * @return a list of {@link Breakpoint}s
     */
    public synchronized @Nonnull List<Breakpoint> getBreakpoints(@Nonnull AbstractProcessInstance instance) {
        
        //
        // are there any breakpoints?
        //
        ProcessDefinition definition = instance.getDefinition();
        
        if (!this.breakpoints.containsKey(definition)) {
            return Collections.emptyList();
        }
        
        //
        // does any of the breakpoints match?
        //
        return this.breakpoints.get(definition);
    }

    /**
     * The {@link DebuggerTokenListener} triggers this method when a {@link Breakpoint}
     * matched the {@link AbstractProcessInstance}'s current {@link Node}.
     * 
     * It will not check the proper matching of the breakpoint, as it is verified beforehand.
     * 
     * @param token the {@link Token}, which matched a breakpoint
     * @param breakpoint the {@link Breakpoint}, which was matched
     * @param listener the {@link DebuggerTokenListener}, which triggered this breakpoint match
     */
    public void breakpointTriggered(@Nonnull Token token,
                                    @Nonnull Breakpoint breakpoint,
                                    @Nonnull DebuggerTokenListener listener) {
        
        logger.info("Breakpoint {} triggered for token {}", breakpoint, token);
        
        //
        // TODO Jan crazy stuff: suspend token, remember state and token, ...
        //
        
    }
}
