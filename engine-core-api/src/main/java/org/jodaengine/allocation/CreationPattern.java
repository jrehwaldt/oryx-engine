package org.jodaengine.allocation;

import java.util.List;
import java.util.Set;

import org.jodaengine.process.token.Token;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.worklist.AbstractWorklistItem;

/**
 * A pattern interface for the creation of worklist items.
 */
public interface CreationPattern {

    /**
     * Creates the worklist items according to a specific Creation strategy.
     * 
     * @param token
     *            the token
     * @return a list of created worklist items
     */
    List<AbstractWorklistItem> createWorklistItems(Token token);
    
    /**
     * Gets the assigned resources.
     *
     * @return the assigned resources
     */
    Set<AbstractResource<?>> getAssignedResources();
    
    
}
