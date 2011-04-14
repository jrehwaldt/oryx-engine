package de.hpi.oryxengine.resource.worklist;

import java.util.List;

import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.oryxengine.resource.AbstractResource;

/**
 * Represents the Worklist that contains several {@link WorklistItem} for a {@link AbstractResource}.
 */
@XmlRootElement
public abstract class AbstractWorklist implements Iterable<WorklistItem> {

    /**
     * Retrieves the contained {@link WorklistItem}s.
     * 
     * @return a list of {@link WorklistItem}s; the list is unmodifiable (read-only) 
     */
    public abstract @Nonnull List<WorklistItem> getWorklistItems();
    
    /**
     * Notifies this {@link Worklist} that the item has been allocated by a certain resource.
     * 
     * The resource is now able to move, edit and remove the {@link WorklistItem}. 
     * 
     * @param worklistItem - a {@link WorklistItem} that was allocated
     * @param claimingResource - the {@link AbstractResource} that allocated the {@link WorklistItem}
     */
    public abstract void itemIsAllocatedBy(@Nonnull WorklistItem worklistItem,
                           @Nonnull AbstractResource<?> claimingResource);

    /**
     * Notifies the {@link Worklist} that the {@link WorklistItem} has been started.
     * 
     * @param worklistItem - a {@link WorklistItem} that has been started
     */
    public abstract void itemIsStarted(@Nonnull WorklistItem worklistItem);

    /**
     * Notifies this {@link Worklist} that the {@link WorklistItem} has been completed.
     * 
     * @param worklistItem - a {@link WorklistItem} that was completed
     */
    public abstract void itemIsCompleted(@Nonnull WorklistItem worklistItem);

    /**
     * Adds a {@link WorklistItem} into the {@link Worklist}.
     * 
     * @param worklistItem - a {@link WorklistItem} to add
     */
    public abstract void addWorklistItem(@Nonnull WorklistItem worklistItem);
}
