package de.hpi.oryxengine;

import java.util.List;
import java.util.Map;

import de.hpi.oryxengine.resource.Resource;
import de.hpi.oryxengine.worklist.WorklistItem;

/**
 * The worklist service which is used to manage our worklist, add tasks and remove them that is.
 */
public interface WorklistService {

    /**
     * Resolves all worklist items belonging to the given resources.
     * 
     * @param resources
     *            - the resources the worklist items shall be searched for
     * @return a map where the key is a {@link Resource} and the value is a list of {@link WorklistItem}s
     */
    Map<Resource<?>, List<WorklistItem>> getWorklistItems(List<Resource<?>> resources);

    /**
     * Claims a {@link WorklistItem}. 
     * 
     * @param worklistItem - {@link WorklistItem} that should be claimed
     */
    void claimWorklistItem(WorklistItem worklistItem);

    /**
     * Aborts a {@link WorklistItem}. 
     * 
     * @param worklistItem - {@link WorklistItem} that is aborted
     */
    void abortWorklistItem(WorklistItem worklistItem);
    
    /**
     * Completes a {@link WorklistItem}.
     * 
     * @param worklistItem - {@link WorklistItem} that was completed
     */
    void completeWorklistItem(WorklistItem worklistItem);
    
    // TODO: Observable Interface für die GUI
}
