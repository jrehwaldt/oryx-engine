package org.jodaengine.ext.debugging.listener;

import java.util.Collection;

import org.jodaengine.ext.Extension;
import org.jodaengine.ext.debugging.api.DebuggerCommand;
import org.jodaengine.ext.debugging.api.DebuggerService;
import org.jodaengine.ext.debugging.shared.DebuggerInstanceAttribute;
import org.jodaengine.ext.listener.JoinListener;
import org.jodaengine.ext.listener.SplitListener;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This listener implementation reacts on {@link Token} splits and joins
 * and keeps the {@link DebuggerInstanceAttribute} reference available.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-27
 */
@Extension(DebuggerService.DEBUGGER_SERVICE_NAME)
public class SplitJoinListener implements SplitListener, JoinListener {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public void joinPerformed(Token triggeringToken,
                              Node node,
                              Collection<Token> outgoing) {
        
        logger.debug("Join performed on {} triggered by {}", node, triggeringToken);
        
        DebuggerInstanceAttribute parentAttribute = DebuggerInstanceAttribute.getAttribute(triggeringToken);
        DebuggerCommand parentCommand = parentAttribute.getCommand(triggeringToken);
        
        for (Token token: outgoing) {
            
            if (token == triggeringToken) {
                continue;
            }
            
            DebuggerInstanceAttribute.setAttribute(parentAttribute, token);
            parentAttribute.setCommand(token, parentCommand);
        }
    }

    @Override
    public void splitPerformed(Token triggeringToken,
                               Node node,
                               Collection<Token> incoming,
                               Collection<Token> outgoing) {
        
        logger.debug(
            "Split performed on {} triggered by {} with " + outgoing.size() + " outgoing flow(s)",
            node,
            triggeringToken);
        
        DebuggerInstanceAttribute parentAttribute = DebuggerInstanceAttribute.getAttribute(triggeringToken);
        DebuggerCommand parentCommand = parentAttribute.getCommand(triggeringToken);
        
        //
        // register the attribute within all outgoing tokens
        //
        for (Token token: outgoing) {
            
            if (token == triggeringToken) {
                continue;
            }
            
            DebuggerInstanceAttribute.setAttribute(parentAttribute, token);
            parentAttribute.setCommand(token, parentCommand);
        }
    }
}
