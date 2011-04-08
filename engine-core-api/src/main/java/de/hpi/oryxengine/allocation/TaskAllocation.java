package de.hpi.oryxengine.allocation;

import java.util.Set;

import de.hpi.oryxengine.WorklistService;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.worklist.WorklistItem;

/**
 * Is the internal interface for operating on the Worklists. This interface is used by the patterns in order to query
 * and modify the queues.
 */
public interface TaskAllocation extends WorklistService {

    /**
     * Adds a {@link WorklistItem} to the specified {@link AbstractResource}.
     *
     * @param worklistItem - {@link WorklistItem} that should be added to the queue.
     * @param resourceToFillIn - the {@link AbstractResource} where the {@link WorklistItem} should be added to
     */
    void addWorklistItem(WorklistItem worklistItem, AbstractResource<?> resourceToFillIn);

    /**
     * Adds a {@link WorklistItem} to the specified {@link AbstractResource}s.
     *
     * @param worklistItem - {@link WorklistItem} that should be added to the queue.
     * @param resourcesToFillIn - the {@link AbstractResource}s where the {@link WorklistItem} should be added to
     */
    void addWorklistItem(WorklistItem worklistItem, Set<AbstractResource<?>> resourcesToFillIn);
}
