package de.hpi.oryxengine.allocation;

import java.util.List;

import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;


/**
 * A pattern interface for the creation of worklist items.
 */
public interface CreationPattern {

    /**
     * Creates the worklist items according to a specific Creation strategy.
     *
     * @param token the token
     * @return a list of created worklist items
     */
    List<AbstractWorklistItem> createWorklistItems(Token token);
    
    AbstractResource<?>[] getAssignedResources();
    
    String getItemSubject();
    
    String getItemDescription();
    
    Form getItemForm();
    
}
