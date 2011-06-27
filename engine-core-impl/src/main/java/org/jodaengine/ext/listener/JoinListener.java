package org.jodaengine.ext.listener;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;

/**
 * An extension listener, which will be informed whenever a join is performed.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-27
 */
public interface JoinListener {
    
    /**
     * This method is called whenever a join is performed.
     * 
     * @param triggeringToken the incoming token, which triggered the final join
     * @param node the node, which performed the the split
     * @param outgoing the outgoing token
     */
    void joinPerformed(@Nonnull Token triggeringToken,
                       @Nonnull Node node,
                       @Nonnull Collection<Token> outgoing);
}
