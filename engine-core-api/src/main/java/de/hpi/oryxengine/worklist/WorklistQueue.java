package de.hpi.oryxengine.worklist;

import java.util.List;

import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.Resource;

/**
 * Is the internal interface for operating on the Worklists. This interface is used by the patterns in order to query
 * and modify the queues.
 */
public interface WorklistQueue {

    
    /**
     * Gets the {@link WorklistItem}s that are in a certain queue.
     *
     * @param resource - in order to identify its queue
     * @return a list of {@link WorklistItem}s of the {@link Resource}
     */
    List<WorklistItem> getWorkListItems(Resource<?> resource);

    /**
     * Adds a {@link WorklistItem} to the specified {@link Resource}.
     *
     * @param worklistItem - {@link WorklistItem} that should be added to the queue.
     * @param resourceToFillIn - the {@link Resource} where the {@link WorklistItem} should be added to
     */
    void addWortlistItem(WorklistItem worklistItem, Resource<?> resourceToFillIn);

    /**
     * Adds a {@link WorklistItem} to the specified {@link Resource}s.
     *
     * @param worklistItem - {@link WorklistItem} that should be added to the queue.
     * @param resourcesToFillIn - the {@link Resource}s where the {@link WorklistItem} should be added to
     */
    void addWorklistItem(WorklistItem worklistItem, List<Resource<?>> resourcesToFillIn);
}
