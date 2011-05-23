package org.jodaengine.ext.debugging.listener;

import javax.annotation.Nonnull;

import org.jodaengine.ext.Extension;
import org.jodaengine.ext.debugging.DebuggerServiceImpl;
import org.jodaengine.ext.debugging.api.DebuggerService;
import org.jodaengine.ext.listener.AbstractTokenListener;
import org.jodaengine.ext.listener.token.ActivityLifecycleChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This listener class belongs to the {@link DebuggerService}. It is called while process execution
 * using the {@link org.jodaengine.process.token.Token} takes place and can handle
 * {@link Breakpoint}, if those are defined.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-22
 */
@Extension(DebuggerService.DEBUGGER_SERVICE_NAME)
public class DebuggerTokenListener extends AbstractTokenListener {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private DebuggerServiceImpl debugger;
    
    /**
     * Default constructor. Creates a token listener for a debugger service.
     * 
     * @param debugger the debugger service, this listener belongs to
     */
    public DebuggerTokenListener(@Nonnull DebuggerServiceImpl debugger) {
        this.debugger = debugger;
    }
    
    @Override
    public void stateChanged(ActivityLifecycleChangeEvent event) {
        
        // TODO Auto-generated method stub
        logger.debug("DebuggerTokenListener#stateChanged {} for {}", event, this.debugger);
    }
    
}
