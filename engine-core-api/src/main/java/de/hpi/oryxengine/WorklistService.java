package de.hpi.oryxengine;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.hpi.oryxengine.exception.InvalidItemException;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;

/**
 * The Worklist Service which is used to manage our {@link Worklist}, add tasks and remove them that is. This is the
 * external API.
 */
public interface WorklistService {

    /**
     * Resolves all {@link Worklist} items belonging to the given resource.
     * 
     * @param resource
     *            - the resource the {@link Worklist} items shall be searched for
     * @return a list of {@link AbstractWorklistItem}s; the list is unmodifiable (read-only)
     */
    @Nonnull
    List<AbstractWorklistItem> getWorklistItems(@Nonnull AbstractResource<?> resource);
    
    /**
     * Gets the worklist items for the ressource with the id.
     *
     * @param id the UUID of the resource
     * @return the worklist items for the resource
     */
    List<AbstractWorklistItem> getWorklistItems(@Nonnull UUID id);

    /**
     * Resolves all worklist items belonging to the given resources.
     * 
     * @param resources
     *            - the resources the worklist items shall be searched for
     * @return a map where the key is a {@link AbstractResource} and the value is a list of {@link AbstractWorklistItem}
     */
    @Nonnull
    Map<AbstractResource<?>,
    List<AbstractWorklistItem>> getWorklistItems(@Nonnull Set<? extends AbstractResource<?>> resources);

    /**
     * Claims a {@link AbstractWorklistItem}.
     * 
     * @param worklistItem
     *            - {@link AbstractWorklistItem} that should be claimed
     * @param resource
     *            the resource that triggers this method
     */
    void claimWorklistItemBy(@Nonnull AbstractWorklistItem worklistItem, @Nonnull AbstractResource<?> resource);

    /**
     * Begin a {@link AbstractWorklistItem}.
     * 
     * @param worklistItem
     *            - {@link AbstractWorklistItem} that should be claimed
     * @param resource
     *            the resource that triggers this method
     */
    void beginWorklistItemBy(@Nonnull AbstractWorklistItem worklistItem, @Nonnull AbstractResource<?> resource);

    /**
     * Completes a {@link AbstractWorklistItem}.
     * 
     * @param worklistItem
     *            - {@link AbstractWorklistItem} that was completed
     * @param resource
     *            the resource that triggers this method
     */
    void completeWorklistItemBy(@Nonnull AbstractWorklistItem worklistItem, @Nonnull AbstractResource<?> resource);

    /**
     * Aborts a {@link AbstractWorklistItem}.
     * 
     * @param worklistItem
     *            - {@link AbstractWorklistItem} that is aborted
     * @param resource
     *            the resource that triggers this method
     */
    void abortWorklistItemBy(@Nonnull AbstractWorklistItem worklistItem, @Nonnull AbstractResource<?> resource);

    /**
     * Returns a {@link AbstractWorklistItem} by id.
     * 
     * @param resource
     *            the {@link AbstractResource}, to which the {@link AbstractWorklistItem} belongs
     * @param worklistItemId
     *            the {@link AbstractWorklistItem}'s id
     * @return the {@link AbstractWorklistItem}
     * @throws InvalidItemException 
     */
    @Nullable AbstractWorklistItem getWorklistItem(@Nonnull AbstractResource<?> resource,
                                                   @Nonnull UUID worklistItemId) throws InvalidItemException;
    // TODO: Observable Interface für die GUI
    /**
     * Returns the number of worklist items which are offered or allocated? to the given resources
     * 
     * @param resources
     *            the resources
     * @return the int
     */
    int size(@Nonnull Set<? extends AbstractResource<?>> resources);
}
