package org.jodaengine.allocation;

import org.jodaengine.process.token.Token;
import org.jodaengine.resource.worklist.AbstractWorklistItem;

/**
 * A pattern interface for the creation of worklist items.
 */
public interface CreationPattern {

    /**
     * Creates the worklist item according to a specific Creation strategy.
     * 
     * @param token
     *            the token
     * @return a list of created worklist items
     */
    AbstractWorklistItem createWorklistItem(Token token);
    
    
}
