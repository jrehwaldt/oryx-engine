package org.jodaengine.ext.listener;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;

/**
 * An extension listener, which will be informed whenever a split is performed.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-27
 */
public interface SplitListener {
    
    /**
     * This method is called whenever a split is performed.
     * 
     * @param triggeringToken the token, which triggered the split
     * @param node the node, which performed the the split
     * @param incomingTokens the incoming tokens
     * @param outgoingTokens the outgoing tokens
     */
    void splitPerformed(@Nonnull Token triggeringToken,
                        @Nonnull Node node,
                        @Nonnull Collection<Token> incomingTokens,
                        @Nonnull Collection<Token> outgoingTokens);
}
