package de.hpi.oryxengine.allocation;

import java.util.Set;

import de.hpi.oryxengine.WorklistService;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;

/**
 * Is the internal interface for operating on the Worklists. This interface is used by the patterns in order to query
 * and modify the queues.
 */
public interface TaskAllocation extends WorklistService {

    /**
     * Adds a {@link AbstractWorklistItem} to the specified {@link AbstractResource}.
     *
     * @param worklistItem - {@link AbstractWorklistItem} that should be added to the queue.
     * @param resourceToFillIn - the {@link AbstractResource} where the {@link AbstractWorklistItem} should be added to
     */
    void addWorklistItem(AbstractWorklistItem worklistItem, AbstractResource<?> resourceToFillIn);

    /**
     * Adds a {@link AbstractWorklistItem} to the specified {@link AbstractResource}s.
     *
     * @param worklistItem - {@link AbstractWorklistItem} that should be added to the queue.
     * @param resourcesToFillIn - the {@link AbstractResource}s where the {@link AbstractWorklistItem} should be added to
     */
    void addWorklistItem(AbstractWorklistItem worklistItem, Set<AbstractResource<?>> resourcesToFillIn);
}
