package org.jodaengine.allocation;

import java.util.Set;

import org.jodaengine.WorklistService;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.worklist.AbstractWorklistItem;

/**
 * Is the internal interface for operating on the Worklists. This interface is used by the patterns in order to query
 * and modify the queues.
 */
public interface TaskAllocation extends WorklistService {

    /**
     * Adds a {@link AbstractWorklistItem} to the specified {@link AbstractResource}.
     * 
     * @param worklistItem
     *            - {@link AbstractWorklistItem} that should be added to the queue.
     * @param resourceToFillIn
     *            - the {@link AbstractResource} where the {@link AbstractWorklistItem} should be added to
     */
    void addWorklistItem(AbstractWorklistItem worklistItem, AbstractResource<?> resourceToFillIn);

    /**
     * Adds a {@link AbstractWorklistItem} to the specified {@link AbstractResource}s.
     * 
     * @param worklistItem
     *            - {@link AbstractWorklistItem} that should be added to the queue.
     * @param resourcesToFillIn
     *            - the {@link AbstractResource}s where the {@link AbstractWorklistItem} should be added to
     */
    void addWorklistItem(AbstractWorklistItem worklistItem, Set<AbstractResource<?>> resourcesToFillIn);

    /**
     * Removes a {@link AbstractWorklistItem} from the specified {@link AbstractResource}.
     * 
     * @param worklistItem
     *            - {@link AbstractWorklistItem} that should be removed from the queue.
     * @param resourceToRemoveFrom
     *            - the {@link AbstractResource} where the {@link AbstractWorklistItem} should be removed from
     */
    void removeWorklistItem(AbstractWorklistItem worklistItem, AbstractResource<?> resourceToRemoveFrom);

    /**
     * Removes a {@link AbstractWorklistItem} from the specified {@link AbstractResource}s.
     * 
     * @param worklistItem
     *            - {@link AbstractWorklistItem} that should be removed from the queue.
     * @param resourcesToRemoveFrom
     *            - the {@link AbstractResource}s where the {@link AbstractWorklistItem} should be removed from
     */
    void removeWorklistItem(AbstractWorklistItem worklistItem, Set<AbstractResource<?>> resourcesToRemoveFrom);
}
