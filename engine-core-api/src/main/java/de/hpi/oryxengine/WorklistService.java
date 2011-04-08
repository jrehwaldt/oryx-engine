package de.hpi.oryxengine;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.hpi.oryxengine.resource.Resource;
import de.hpi.oryxengine.resource.worklist.WorklistItem;

/**
 * The Worklist Service which is used to manage our {@link Worklist}, add tasks and remove them that is.
 */
public interface WorklistService {

    /**
     * Resolves all {@link Worklist} items belonging to the given resource.
     * 
     * @param resource
     *            - the resource the {@link Worklist} items shall be searched for
     * @return a list of {@link WorklistItem}s; the list is unmodifiable (read-only)
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
     * @param resource the resource that triggers this method
     */
    void claimWorklistItemBy(WorklistItem worklistItem, Resource<?> resource);

    /**
     * Begin a {@link WorklistItem}.
     *
     * @param worklistItem - {@link WorklistItem} that should be claimed
     * @param resource the resource that triggers this method
     */
    void beginWorklistItemBy(WorklistItem worklistItem, Resource<?> resource);

    /**
     * Completes a {@link WorklistItem}.
     *
     * @param worklistItem - {@link WorklistItem} that was completed
     * @param resource the resource that triggers this method
     */
    void completeWorklistItemBy(WorklistItem worklistItem, Resource<?> resource);

    /**
     * Aborts a {@link WorklistItem}.
     *
     * @param worklistItem - {@link WorklistItem} that is aborted
     * @param resource the resource that triggers this method
     */
    void abortWorklistItemBy(WorklistItem worklistItem, Resource<?> resource);

    /**
     * Returns a {@link WorklistItem} by id.
     * 
     * @param resource the {@link Resource}, to which the {@link WorklistItem} belongs
     * @param worklistItemId the {@link WorklistItem}'s id
     * @return the {@link WorklistItem}
     */
    @Nullable WorklistItem getWorklistItem(@Nonnull Resource<?> resource,
                                           @Nonnull UUID worklistItemId);
    
    // TODO: Observable Interface für die GUI
    
    /**
     * Returns the number of worklist items which are offered or allocated? to the given resources
     *
     * @param resources the resources
     * @return the int
     */
    int size(List<Resource<?>> resources);
}
