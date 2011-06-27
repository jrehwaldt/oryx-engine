package org.jodaengine.ext.debugging.listener;

import java.util.Collection;

import org.jodaengine.ext.debugging.api.DebuggerCommand;
import org.jodaengine.ext.debugging.shared.DebuggerInstanceAttribute;
import org.jodaengine.ext.listener.JoinListener;
import org.jodaengine.ext.listener.SplitListener;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;

/**
 * This listener implementation reacts on {@link Token} splits and joins
 * and keeps the {@link DebuggerInstanceAttribute} reference available.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-27
 */
public class SplitJoinListener implements SplitListener, JoinListener {
    
    @Override
    public void joinPerformed(Token triggeringToken,
                              Node node,
                              Collection<Token> outgoing) {
        
        DebuggerInstanceAttribute parentAttribute = DebuggerInstanceAttribute.getAttribute(triggeringToken);
        DebuggerCommand command = parentAttribute.getCommand(triggeringToken);
        
        for (Token token: outgoing) {
            
            if (!DebuggerInstanceAttribute.hasAttribute(token)) {
                DebuggerInstanceAttribute.setAttribute(parentAttribute, token);
            }
            
            DebuggerInstanceAttribute attribute = DebuggerInstanceAttribute.getAttribute(token);
            attribute.setCommand(token, command);
        }
    }

    @Override
    public void splitPerformed(Token triggeringToken,
                               Node node,
                               Collection<Token> incoming,
                               Collection<Token> outgoing) {
        
        DebuggerInstanceAttribute attribute = DebuggerInstanceAttribute.getAttribute(triggeringToken);
        
        //
        // register the attribute within all outgoing tokens
        //
        for (Token token: outgoing) {
            DebuggerInstanceAttribute.setAttribute(attribute, token);
        }
    }
}
