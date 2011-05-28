package org.jodaengine.ext.debugging.listener;

import javax.annotation.Nonnull;

import org.jodaengine.ext.Extension;
import org.jodaengine.ext.debugging.DebuggerServiceImpl;
import org.jodaengine.ext.debugging.api.DebuggerService;
import org.jodaengine.ext.debugging.shared.BreakpointImpl;
import org.jodaengine.ext.listener.AbstractTokenListener;
import org.jodaengine.ext.listener.token.ActivityLifecycleChangeEvent;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This listener class belongs to the {@link DebuggerService}. It is called while process execution
 * using the {@link org.jodaengine.process.token.Token} takes place and can handle
 * {@link BreakpointImpl}, if those are defined.
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
        // do we have a breakpoint for this node
        //
        Node currentNode = event.getNode();
        BreakpointImpl breakpoint = BreakpointImpl.getAttributeIfExists(currentNode);
        
        //
        // case: no
        //
        if (breakpoint == null) {
            return;
        }
        
        //
        // case: yes - does the breakpoint match?
        //
        Token token = event.getProcessToken();
        
        if (breakpoint.matches(token)) {
            logger.debug("Breakpoint {} matches {}", breakpoint, token);
            
            Node node = event.getNode();
            ActivityState currentState = event.getNewState();
            this.debugger.breakpointTriggered(node, token, breakpoint, currentState, this);
            
        } else {
            logger.debug("Breakpoint {} did not match {}", breakpoint, token);
        }
    }
    
}
