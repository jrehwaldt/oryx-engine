package de.hpi.oryxengine.resource.worklist;

import java.util.List;

import de.hpi.oryxengine.resource.Resource;

/**
 * Represents the Worklist that contains several WorkItems for a {@link Resource}.
 */
public interface Worklist {

    /**
     * Retrieves the contained {@link WorklistItem}s.
     * 
     * @return a list of {@link WorklistItem}s; the list is unmodifiable (read-only) 
     */
    List<WorklistItem> getWorklistItems();
    
    /**
     * Notifies this {@link Worklist} that the item has been allocated by a certain resource.
     * 
     * The resource is now able to move, edit and remove the {@link WorklistItem}. 
     * 
     * @param worklistItem - a {@link WorklistItem} that was allocated
     * @param claimingResource - the {@link Resource} that allocated the {@link WorklistItem}
     */
    void itemIsAllocatedBy(WorklistItem worklistItem, Resource<?> claimingResource);

    /**
     * Notifies the {@link Worklist} that the {@link WorklistItem} has been started.
     * 
     * @param worklistItem - a {@link WorklistItem} that has been started
     */
    void itemIsStarted(WorklistItem worklistItem);

    /**
     * Notifies this {@link Worklist} that the {@link WorklistItem} has been completed.
     * 
     * @param worklistItem - a {@link WorklistItem} that was completed
     */
    void itemIsCompleted(WorklistItem worklistItem);

    /**
     * Adds a {@link WorklistItem} into the {@link Worklist}.
     * 
     * @param worklistItem - a {@link WorklistItem} to add
     */
    void addWorklistItem(WorklistItem worklistItem);
}
