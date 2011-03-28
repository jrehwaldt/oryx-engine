package de.hpi.oryxengine.resource.worklist;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.resource.Resource;

/**
 * The abstract work list class.
 */
public abstract class AbstractWorklist implements Worklist {

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

    @Override
    public abstract List<WorklistItem> getWorklistItems();

    @Override
    public abstract void itemIsAllocatedBy(WorklistItem worklistItem, Resource<?> claimingResource);

    @Override
    public abstract void itemIsCompleted(WorklistItem worklistItem);

    @Override
    public abstract void itemIsStarted(WorklistItem worklistItem);

    @Override
    public abstract void addWorklistItem(WorklistItem worklistItem);
}
