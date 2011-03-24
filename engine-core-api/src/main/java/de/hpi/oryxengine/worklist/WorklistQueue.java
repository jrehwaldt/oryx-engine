package de.hpi.oryxengine.worklist;

import java.util.Set;

import de.hpi.oryxengine.WorklistService;
import de.hpi.oryxengine.resource.Resource;

/**
 * Is the internal interface for operating on the Worklists. This interface is used by the patterns in order to query
 * and modify the queues.
 */
// TODO Change Name
public interface WorklistQueue extends WorklistService {

    /**
     * Adds a {@link WorklistItem} to the specified {@link Resource}.
     *
     * @param worklistItem - {@link WorklistItem} that should be added to the queue.
     * @param resourceToFillIn - the {@link Resource} where the {@link WorklistItem} should be added to
     */
    void addWorklistItem(WorklistItem worklistItem, Resource<?> resourceToFillIn);

    /**
     * Adds a {@link WorklistItem} to the specified {@link Resource}s.
     *
     * @param worklistItem - {@link WorklistItem} that should be added to the queue.
     * @param resourcesToFillIn - the {@link Resource}s where the {@link WorklistItem} should be added to
     */
    void addWorklistItem(WorklistItem worklistItem, Set<Resource<?>> resourcesToFillIn);
}
