package de.hpi.oryxengine.resource.worklist;

import java.util.List;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import de.hpi.oryxengine.resource.AbstractResource;

/**
 * Represents the Worklist that contains several {@link AbstractWorklistItem} for a {@link AbstractResource}.
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public abstract class AbstractWorklist implements Iterable<AbstractWorklistItem> {

    /**
     * Retrieves all contained {@link AbstractWorklistItem}s (offered, allocated, executing). 
     * 
     * @return a list of {@link AbstractWorklistItem}s; the list is unmodifiable (read-only) 
     */
    @JsonIgnore
    public abstract @Nonnull List<AbstractWorklistItem> getAllWorklistItems();
    
    /**
     * Gets all allocated {@link AbstractWorklistItem item} of the corresponding resource.
     *
     * @return a list of allocated {@link AbstractWorklistItem items}
     */
    @JsonIgnore
    public abstract @Nonnull List<AbstractWorklistItem> getAllocatedWorklistItems();
    
    /**
     * Gets all offered {@link AbstractWorklistItem item} of the corresponding resource.
     *
     * @return a list of offered {@link AbstractWorklistItem items}
     */
    @JsonIgnore
    public abstract @Nonnull List<AbstractWorklistItem> getOfferedWorklistItems();
    
    /**
     * Gets all {@link AbstractWorklistItem item} of the corresponding resource that are in execution.
     *
     * @return a list containing the {@link AbstractWorklistItem items} in execution
     */
    @JsonIgnore
    public abstract @Nonnull List<AbstractWorklistItem> getExecutingWorklistItems();
    
    
    
    /**
     * Notifies this {@link Worklist} that the item has been allocated by a certain resource.
     * 
     * The resource is now able to move, edit and remove the {@link AbstractWorklistItem}. 
     * 
     * @param worklistItem - a {@link AbstractWorklistItem} that was allocated
     * @param claimingResource - the {@link AbstractResource} that allocated the {@link AbstractWorklistItem}
     */
    public abstract void itemIsAllocatedBy(@Nonnull AbstractWorklistItem worklistItem,
                                           @Nonnull AbstractResource<?> claimingResource);

    /**
     * Notifies the {@link Worklist} that the {@link AbstractWorklistItem} has been started.
     * 
     * @param worklistItem - a {@link AbstractWorklistItem} that has been started
     */
    public abstract void itemIsStarted(@Nonnull AbstractWorklistItem worklistItem);

    /**
     * Notifies this {@link Worklist} that the {@link AbstractWorklistItem} has been completed.
     * 
     * @param worklistItem - a {@link AbstractWorklistItem} that was completed
     */
    public abstract void itemIsCompleted(@Nonnull AbstractWorklistItem worklistItem);

    /**
     * Adds a {@link AbstractWorklistItem} into the {@link Worklist}.
     * 
     * @param worklistItem - a {@link AbstractWorklistItem} to add
     */
    public abstract void addWorklistItem(@Nonnull AbstractWorklistItem worklistItem);
}
