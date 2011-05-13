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
    
    /**
     * Gets the resources that the workitem(s) will be created for.
     *
     * @return the assigned resources
     */
    AbstractResource<?>[] getAssignedResources();
    
    /**
     * Gets the item's subject.
     *
     * @return the item subject
     */
    String getItemSubject();
    
    /**
     * Gets the item's description.
     *
     * @return the item description
     */
    String getItemDescription();
    
    /**
     * Gets the item's form.
     *
     * @return the item form
     */
    Form getItemForm();
    
}
