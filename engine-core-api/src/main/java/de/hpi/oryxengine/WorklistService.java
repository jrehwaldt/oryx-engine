package de.hpi.oryxengine;

import java.util.List;
import java.util.Map;

import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.resource.Resource;
import de.hpi.oryxengine.worklist.WorklistItem;

/**
 * The worklist service which is used to manage our worklist, add tasks and remove them that is.
 */
public interface WorklistService {

    /**
     * Resolves all worklist items belonging to the given resource.
     * 
     * @param resource
     *            - the resource the worklist items shall be searched for
     * @return a list of {@link WorklistItem}s
     */
    List<WorklistItem> getWorklistItems(Resource<?> resource);
    
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
     * @throws DalmatinaException thrown if claim fails
     */
    void claimWorklistItem(WorklistItem worklistItem) throws DalmatinaException;

    /**
     * Begin a {@link WorklistItem}. 
     * 
     * @param worklistItem - {@link WorklistItem} that should be claimed
     * @throws DalmatinaException thrown if begin fails
     */
    void beginWorklistItem(WorklistItem worklistItem) throws DalmatinaException;

    /**
     * Completes a {@link WorklistItem}.
     * 
     * @param worklistItem - {@link WorklistItem} that was completed
     * @throws DalmatinaException thrown if completing fails
     */
    void completeWorklistItem(WorklistItem worklistItem) throws DalmatinaException;

    /**
     * Aborts a {@link WorklistItem}. 
     * 
     * @param worklistItem - {@link WorklistItem} that is aborted
     * @throws DalmatinaException thrown if aborting fails
     */
    void abortWorklistItem(WorklistItem worklistItem) throws DalmatinaException;
    
    // TODO: Observable Interface für die GUI
}
