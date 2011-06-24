package org.jodaengine.resource.worklist;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jodaengine.bootstrap.Service;
import org.jodaengine.exception.InvalidWorkItemException;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.allocation.DetourPattern;

/**
 * The Worklist Service which is used to manage our {@link Worklist}, add tasks and remove them that is. This is the
 * external API.
 */
public interface WorklistService extends Service {

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
     * Gets the offered worklist items.
     * 
     * @param resource
     *            the resource the {@link Worklist} items shall be searched for
     * @return a list of all offered {@link AbstractWorklistItem}s; the list is unmodifiable (read-only)
     */
    @Nonnull
    List<AbstractWorklistItem> getOfferedWorklistItems(@Nonnull AbstractResource<?> resource);

    /**
     * Gets the allocated worklist items.
     * 
     * @param resource
     *            the resource the {@link Worklist} items shall be searched for
     * @return a list of all allocated {@link AbstractWorklistItem}s; the list is unmodifiable (read-only)
     */
    @Nonnull
    List<AbstractWorklistItem> getAllocatedWorklistItems(@Nonnull AbstractResource<?> resource);

    /**
     * Gets the claimed worklist items.
     * 
     * @param resource
     *            the resource the {@link Worklist} items shall be searched for
     * @return a list of all claimed {@link AbstractWorklistItem}s; the list is unmodifiable (read-only)
     */
    @Nonnull
    List<AbstractWorklistItem> getExecutingWorklistItems(@Nonnull AbstractResource<?> resource);

    /**
     * Gets the worklist items for the ressource with the id.
     * 
     * @param id
     *            the UUID of the resource
     * @return the worklist items for the resource
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    List<AbstractWorklistItem> getWorklistItems(@Nonnull UUID id)
    throws ResourceNotAvailableException;

    /**
     * Gets all offered worklist items for the ressource with the id.
     * 
     * @param id
     *            the UUID of the resource
     * @return the worklist items for the resource
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    List<AbstractWorklistItem> getOfferedWorklistItems(@Nonnull UUID id)
    throws ResourceNotAvailableException;

    /**
     * Gets all allocated worklist items for the ressource with the id.
     * 
     * @param id
     *            the UUID of the resource
     * @return the worklist items for the resource
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    List<AbstractWorklistItem> getAllocatedWorklistItems(@Nonnull UUID id)
    throws ResourceNotAvailableException;

    /**
     * Gets all executing worklist items for the ressource with the id.
     * 
     * @param id
     *            the UUID of the resource
     * @return the worklist items for the resource
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    List<AbstractWorklistItem> getExecutingWorklistItems(@Nonnull UUID id)
    throws ResourceNotAvailableException;

    /**
     * Resolves all worklist items belonging to the given resources.
     * 
     * @param resources
     *            - the resources the worklist items shall be searched for
     * @return a map where the key is a {@link AbstractResource} and the value is a list of {@link AbstractWorklistItem}
     */
    @Nonnull
    Map<AbstractResource<?>, List<AbstractWorklistItem>> 
    getWorklistItems(@Nonnull Set<? extends AbstractResource<?>> resources);

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
     * @param oldResource
     *            the old resource that executed the abortion
     * @param newResource
     *            the resource that shall get the item that is aborted (if it is not known and the pattern shall
     *            evaluate a resource, leave <code>null</code> and ensure, that the pattern finds a resource)
     * @param pattern
     *            the pattern to be executed on abortion
     */
    void abortWorklistItemBy(@Nonnull AbstractWorklistItem worklistItem,
                             @Nonnull AbstractResource<?> oldResource,
                             @Nonnull DetourPattern pattern,
                             AbstractResource<?> newResource);
    
    /**
     * Aborts a {@link AbstractWorklistItem}.
     * 
     * @param worklistItem
     *            - {@link AbstractWorklistItem} that is aborted
     * @param oldResource
     *            the old resource that executed the abortion
     */
    void abortWorklistItemBy(AbstractWorklistItem worklistItem, AbstractResource<?> oldResource);

    /**
     * Execute the given detour pattern and route the item to a resource.
     * 
     * @param detourPattern
     *            the detour pattern to be executed
     * @param worklistItem
     *            the worklist item to re-distribute
     * @param oldResource
     *            the old resource that triggered the execution of a pattern
     * @param newResource
     *            the new resource that shall get the item afterwards (if not known and the pattern evaluates that,
     *            leave <code>null</code>)
     */
    void executeDetourPattern(@Nonnull DetourPattern detourPattern,
                              @Nonnull AbstractWorklistItem worklistItem,
                              AbstractResource<?> oldResource,
                              AbstractResource<?> newResource);

    /**
     * Returns a {@link AbstractWorklistItem} by id.
     * 
     * @param resource
     *            the {@link AbstractResource}, to which the {@link AbstractWorklistItem} belongs
     * @param worklistItemId
     *            the {@link AbstractWorklistItem}'s id
     * @return the {@link AbstractWorklistItem}
     * @throws InvalidWorkItemException
     *             thrown if the work item is not found
     */
    @Nullable
    AbstractWorklistItem getWorklistItem(@Nonnull AbstractResource<?> resource, @Nonnull UUID worklistItemId)
    throws InvalidWorkItemException;
}
