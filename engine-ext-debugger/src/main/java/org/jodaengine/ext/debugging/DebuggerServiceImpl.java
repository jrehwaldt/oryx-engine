package org.jodaengine.ext.debugging;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.ext.Extension;
import org.jodaengine.ext.debugging.api.BreakpointService;
import org.jodaengine.ext.debugging.api.DebuggerService;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link DebuggerService} implementation providing the possibility to set
 * {@link Breakpoint} and debug process instances.
 * 
 * This class implements both, the {@link DebuggerService} as well as the {@link BreakpointService}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-19
 */
@Extension(DebuggerService.EXTENSION_SERVICE_NAME)
public class DebuggerServiceImpl implements DebuggerService, BreakpointService {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private JodaEngineServices engineServices;
    private Navigator navigator;
    
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
        return addBreakpoint(node, null);
    }

    @Override
    public Breakpoint addBreakpoint(Node node,
                                    AbstractProcessInstance instance) {
        // TODO Auto-generated method stub
        logger.debug("Add breakpoint to node {} | instance {}", node, instance);
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

}
