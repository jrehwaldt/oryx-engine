package org.jodaengine.ext.debugging.listener;

import java.util.List;

import javax.annotation.Nonnull;

import org.jodaengine.ext.Extension;
import org.jodaengine.ext.debugging.DebuggerServiceImpl;
import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.api.DebuggerService;
import org.jodaengine.ext.listener.AbstractTokenListener;
import org.jodaengine.ext.listener.token.ActivityLifecycleChangeEvent;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This listener class belongs to the {@link DebuggerService}. It is called while process execution
 * using the {@link Token} takes place and can handle {@link Breakpoint}s, if those are defined.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-22
 */
@Extension(DebuggerService.DEBUGGER_SERVICE_NAME)
public class DebuggerTokenListener extends AbstractTokenListener {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private DebuggerServiceImpl debugger;
    
    /**
     * Default constructor. Creates a {@link DebuggerTokenListener} for a {@link DebuggerService}.
     * 
     * @param debugger the debugger service, this listener belongs to
     */
    public DebuggerTokenListener(@Nonnull DebuggerServiceImpl debugger) {
        this.debugger = debugger;
    }
    
    @Override
    public void stateChanged(ActivityLifecycleChangeEvent event) {
        
        logger.debug("DebuggerTokenListener#stateChanged {} for {}", event, this.debugger);
        
        //
        // do we have breakpoints for this node?
        //
        Node node = event.getNode();
        Token token = event.getProcessToken();
        List<Breakpoint> breakpoints = this.debugger.getBreakpoints(node, token.getInstance());
        
        //
        // case: no
        //
        if (breakpoints.isEmpty()) {
            return;
        }
        
        //
        // case: yes - does any breakpoint match?
        //
        for (Breakpoint breakpoint: breakpoints) {
            
            if (breakpoint.matches(token)) {
                logger.debug("Breakpoint {} matches {}", breakpoint, token);
                
                this.debugger.breakpointTriggered(token, breakpoint);
                
                //
                // ignore any subsequent breakpoints
                //
                return;
                
            } else {
                logger.debug("Breakpoint {} did not match {}", breakpoint, token);
            }
        }
    }
    
}
