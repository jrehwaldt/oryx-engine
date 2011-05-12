package de.hpi.oryxengine.allocation;

import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.AbstractResource;


/**
 * A pattern interface for the creation of worklist items.
 */
public interface CreationPattern {

    /**
     * Creates the worklist items according to a specific Creation strategy.
     *
     * @param worklistService the worklist service to use for item distribution
     */
    void createWorklistItems(TaskAllocation worklistService, Token token);
    
    AbstractResource<?>[] getAssignedResources();
    
}
