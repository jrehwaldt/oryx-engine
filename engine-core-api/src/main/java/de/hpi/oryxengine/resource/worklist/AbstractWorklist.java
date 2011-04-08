package de.hpi.oryxengine.resource.worklist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.hpi.oryxengine.resource.AbstractResource;

/**
 * Represents the Worklist that contains several {@link WorklistItem} for a {@link AbstractResource}.
 */
public abstract class AbstractWorklist implements Iterable<WorklistItem> {

    private List<WorklistItem> lazyWorklistItems;
    
    /**
     * Retrieves the {@link WorklistItem}s that are in this {@link Worklist}.
     * 
     * @return a list of {@link WorklistItem}s; the list is instantiated on demand
     */
    List<WorklistItem> getLazyWorklistItems() {

        if (lazyWorklistItems == null) {
            lazyWorklistItems = new ArrayList<WorklistItem>();
        }
        
        return lazyWorklistItems;
    }

    /**
     * Retrieves the contained {@link WorklistItem}s.
     * 
     * @return a list of {@link WorklistItem}s; the list is unmodifiable (read-only) 
     */
    public abstract List<WorklistItem> getWorklistItems();
    
    /**
     * Notifies this {@link Worklist} that the item has been allocated by a certain resource.
     * 
     * The resource is now able to move, edit and remove the {@link WorklistItem}. 
     * 
     * @param worklistItem - a {@link WorklistItem} that was allocated
     * @param claimingResource - the {@link AbstractResource} that allocated the {@link WorklistItem}
     */
    public abstract void itemIsAllocatedBy(WorklistItem worklistItem, AbstractResource<?> claimingResource);

    /**
     * Notifies this {@link Worklist} that the {@link WorklistItem} has been completed.
     * 
     * @param worklistItem - a {@link WorklistItem} that was completed
     */
    public abstract void itemIsCompleted(WorklistItem worklistItem);

    /**
     * Notifies the {@link Worklist} that the {@link WorklistItem} has been started.
     * 
     * @param worklistItem - a {@link WorklistItem} that has been started
     */
    public abstract void itemIsStarted(WorklistItem worklistItem);
    
    /**
     * Adds a {@link WorklistItem} into the {@link Worklist}.
     * 
     * @param worklistItem - a {@link WorklistItem} to add
     */
    public abstract void addWorklistItem(WorklistItem worklistItem);


    @Override
    public Iterator<WorklistItem> iterator() {
        return getWorklistItems().iterator();
    }
}
