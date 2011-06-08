package org.jodaengine.ext.debugging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.jodaengine.ext.debugging.api.DebuggerCommand;
import org.jodaengine.ext.debugging.api.DebuggerService;
import org.jodaengine.ext.debugging.api.InterruptedInstance;
import org.jodaengine.ext.debugging.api.Interrupter;
import org.jodaengine.ext.debugging.api.NodeBreakpoint;
import org.jodaengine.ext.debugging.listener.DebuggerTokenListener;
import org.jodaengine.ext.debugging.rest.DebuggerWebService;
import org.jodaengine.ext.debugging.shared.BreakpointImpl;
import org.jodaengine.ext.debugging.shared.DebuggerAttribute;
import org.jodaengine.ext.debugging.shared.InterruptedInstanceImpl;
import org.jodaengine.ext.debugging.shared.JuelBreakpointCondition;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.jodaengine.util.io.StreamSource;
import org.jodaengine.util.io.StringStreamSource;
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
    
    private RepositoryService repository;
    
    private Map<ProcessDefinition, List<Breakpoint>> breakpoints;
    private Map<UUID, InterruptedInstanceImpl> interruptedInstances;
    
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
        this.repository = services.getRepositoryService();
        
        this.breakpoints = new HashMap<ProcessDefinition, List<Breakpoint>>();
        this.interruptedInstances = new HashMap<UUID, InterruptedInstanceImpl>();
        
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
        
        this.breakpoints = null;
        this.interruptedInstances = null;
        
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
    public void stepOverInstance(InterruptedInstance instance) {
        
        logger.debug("Step over instance {}", instance);
        releaseInstance(instance, DebuggerCommand.STEP_OVER);
    }

    @Override
    public void terminateInstance(InterruptedInstance instance) {
        
        logger.debug("Terminate instance {}", instance);
        releaseInstance(instance, DebuggerCommand.TERMINATE);
    }

    @Override
    public void resumeInstance(InterruptedInstance instance) {
        
        logger.debug("Resume instance {}", instance);
        releaseInstance(instance, DebuggerCommand.RESUME);
    }

    @Override
    public void continueInstance(InterruptedInstance instance) {
        
        logger.debug("Continue instance {}", instance);
        releaseInstance(instance, DebuggerCommand.CONTINUE);
    }

    @Override
    public synchronized Collection<InterruptedInstance> getInterruptedInstances() {
        
        Collection<InterruptedInstance> result = new ArrayList<InterruptedInstance>();
        result.addAll(this.interruptedInstances.values());
        return result;
    }
    
    //=================================================================
    //=================== BreakpointService methods ===================
    //=================================================================

    @Override
    public synchronized NodeBreakpoint createNodeBreakpoint(ProcessDefinition targetDefinition,
                                                            Node targetNode,
                                                            ActivityState targetActivityState,
                                                            String juelCondition)
    throws DefinitionNotFoundException {
        
        logger.info("Create a breakpoint for node {} on {}", targetNode, targetActivityState);
        NodeBreakpoint breakpoint = new BreakpointImpl(targetNode, targetActivityState);
        
        if (juelCondition != null) {
            logger.info("Adding condition {}", juelCondition);
            breakpoint.setCondition(new JuelBreakpointCondition(juelCondition));
        }
        
        registerBreakpoints(Arrays.<Breakpoint>asList(breakpoint), targetDefinition);
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
    public Collection<Breakpoint> getBreakpoints() {
        
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

    @Override
    public void setSvgArtifact(ProcessDefinition definition,
                               String svgArtifact) {
        
        //
        // add the svg artifact to the repository
        //
        String artifactID = definition.getID().toString();
        StreamSource stream = new StringStreamSource(svgArtifact);
        AbstractProcessArtifact artifact = new ProcessArtifact(DEBUGGER_ARTIFACT_NAMESPACE + artifactID, stream);
        
        this.repository.addProcessArtifact(artifact, definition.getID());
        
        //
        // add the artifact name to the process definition
        //
        DebuggerAttribute attribute = DebuggerAttribute.getAttribute(definition);
        attribute.setSvgArtifact(artifactID);
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
    public synchronized void registerBreakpoints(@Nonnull Collection<Breakpoint> breakpoints,
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
    
    @Override
    public synchronized Collection<Breakpoint> getBreakpoints(AbstractProcessInstance instance) {
        
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
     * @param interruptedToken the {@link Token}, which matched a breakpoint
     * @param causingBreakpoint the {@link Breakpoint}, which was matched
     * @param interruptingListener the {@link DebuggerTokenListener}, which triggered this breakpoint match
     * 
     * @return the signaler for out {@link InterruptedInstance}
     */
    public synchronized Interrupter breakpointTriggered(@Nonnull Token interruptedToken,
                                                               @Nonnull Breakpoint causingBreakpoint,
                                                               @Nonnull DebuggerTokenListener interruptingListener) {
        
        logger.info("Breakpoint {} triggered for token {}", causingBreakpoint, interruptedToken);
        
        //
        // remember the token's state
        //
        InterruptedInstanceImpl instance = new InterruptedInstanceImpl(interruptedToken, causingBreakpoint);
        
        this.interruptedInstances.put(instance.getID(), instance);
        
        //
        // and resume this object as Interrupter
        //
        return instance;
    }
    
    /**
     * The {@link DebuggerTokenListener} invokes this method to signalize, that it was
     * interrupted by a non-Debugger component.
     * 
     * @param signal the signal, which was unexpectedly (=ignoring the Debugger) interrupted
     */
    public void unexspectedInterruption(@Nonnull Interrupter signal) {
        
        //
        // we are, unfortunately, no longer waiting for this instance - someone else stole it
        //
        InterruptedInstanceImpl instance = this.interruptedInstances.remove(signal.getID());
        logger.info("Interrupted instance {} unexpectedly continued. Breakpoint cleared.", instance);
    }
    
    /**
     * Releases an {@link InterruptedInstance} (a interrupted token) and continues execution with the
     * provided scope.
     * 
     * @param instance the instance to continue
     * @param command the command, within which scope it is requested to be continued
     */
    private synchronized void releaseInstance(@Nonnull InterruptedInstance instance,
                                              @Nonnull DebuggerCommand command) {
        
        logger.info("Release instance {} with command {}", instance, command);
        
        //
        // get the registered signal
        //
        Interrupter signal = this.interruptedInstances.remove(instance.getID());
        
        if (signal == null) {
            throw new IllegalStateException(
                "The signal is no longer available. The requested instances has already been released.");
        }
        
        //
        // release it
        //
        signal.releaseInstance(command);
    }
}
