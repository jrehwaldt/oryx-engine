package org.jodaengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jodaengine.bootstrap.Service;
import org.jodaengine.exception.InvalidWorkItemException;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.allocation.TaskAllocation;
import org.jodaengine.resource.allocation.pattern.detour.StatelessReallocationPattern;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.DetourPattern;
import org.jodaengine.resource.worklist.WorklistItemState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The implementation of the WorklistManager. It manages the worklists of all resources in the system.
 */
public class WorklistManager implements WorklistService, TaskAllocation, Service {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private IdentityService identityService;
    
    private boolean running = false;
    private DetourPattern defaultCancellationPattern;
    
    @Override
    public synchronized void start(JodaEngineServices services) {
        
        logger.info("Starting the worklist manager");
        identityService = services.getIdentityService();
        defaultCancellationPattern = new StatelessReallocationPattern();
        this.running = true;
    }

    @Override
    public synchronized void stop() {
        
        logger.info("Stopping the worklist manager");
        this.running = false;
    }
    
    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public void addWorklistItem(AbstractWorklistItem worklistItem, AbstractResource<?> resourceToFillIn) {

        // The worklistItem is added to the worklist of a certain resource
        resourceToFillIn.getWorklist().addWorklistItem(worklistItem);
    }

    @Override
    public void addWorklistItem(AbstractWorklistItem worklistItem, Set<AbstractResource<?>> resourcesToFillIn) {

        // Copying the set because it is modified during iteration. If it is not done there would be a
        // ConcurrentModificationException.
        AbstractResource<?>[] resourcesToFillInArray = (AbstractResource<?>[]) resourcesToFillIn
        .toArray(new AbstractResource<?>[resourcesToFillIn.size()]);

        for (int i = 0; i < resourcesToFillInArray.length; i++) {
            AbstractResource<?> resourceToFillIn = resourcesToFillInArray[i];
            addWorklistItem(worklistItem, resourceToFillIn);
        }
    }

    @Override
    public void removeWorklistItem(AbstractWorklistItem worklistItem, AbstractResource<?> resourceToRemoveFrom) {

        resourceToRemoveFrom.getWorklist().removeWorklistItem(worklistItem);

    }

    @Override
    public void removeWorklistItem(AbstractWorklistItem worklistItem, Set<AbstractResource<?>> resourcesToRemoveFrom) {

        // Copying the set because it is modified during iteration. If it is not done there would be a
        // ConcurrentModificationException.
        AbstractResource<?>[] resourcesToRemoveFromArray = (AbstractResource<?>[]) resourcesToRemoveFrom
        .toArray(new AbstractResource<?>[resourcesToRemoveFrom.size()]);

        for (int i = 0; i < resourcesToRemoveFromArray.length; i++) {
            AbstractResource<?> resourceToFillIn = resourcesToRemoveFromArray[i];
            removeWorklistItem(worklistItem, resourceToFillIn);
        }

    }

    @Override
    public @Nullable
    AbstractWorklistItem getWorklistItem(@Nonnull AbstractResource<?> resource, @Nonnull UUID worklistItemId)
    throws InvalidWorkItemException {

        for (final AbstractWorklistItem item : resource.getWorklist()) {
            if (worklistItemId.equals(item.getID())) {
                return item;
            }
        }

        // throw an exception, if the item was not found
        throw new InvalidWorkItemException(worklistItemId);
    }

    @Override
    public Map<AbstractResource<?>, List<AbstractWorklistItem>> getWorklistItems(
        Set<? extends AbstractResource<?>> resources) {

        Map<AbstractResource<?>, List<AbstractWorklistItem>> result = 
            new HashMap<AbstractResource<?>, List<AbstractWorklistItem>>();

        for (AbstractResource<?> r : resources) {
            result.put(r, getWorklistItems(r));
        }

        return result;
    }

    @Override
    public void claimWorklistItemBy(AbstractWorklistItem worklistItem, AbstractResource<?> resource) {

        synchronized (worklistItem) {
            if (!getWorklistItems(resource).contains(worklistItem)) {
                logger.debug("thou shalt not steal worklist items: {}", resource.getName());
                return;
            }
            // Defining which resources' worklists should be notified when the worklist item is claimed
            Set<AbstractResource<?>> resourcesToNotify = new HashSet<AbstractResource<?>>();
            resourcesToNotify.add(resource);
            resourcesToNotify.addAll(worklistItem.getAssignedResources());

            // Notifying the worklist of each resource. Each worklist implements another behavior
            for (AbstractResource<?> resourceToNotify : resourcesToNotify) {
                resourceToNotify.getWorklist().itemIsAllocatedBy(worklistItem, resource);
            }
        }

    }

    @Override
    public void abortWorklistItemBy(AbstractWorklistItem worklistItem, AbstractResource<?> resource) {

        defaultCancellationPattern.distribute(worklistItem, resource);
    }

    @Override
    public void completeWorklistItemBy(AbstractWorklistItem worklistItem, AbstractResource<?> resource) {

        // Others resources' worklists don't need to be notifyed because there is only one resource that has this
        // worklistItem
        resource.getWorklist().itemIsCompleted(worklistItem);

        // Resuming the token
        // TODO TobiM - vielleicht willst du da was anderes zurückgeben
        worklistItem.getCorrespondingToken().resume(null);

    }

    @Override
    public List<AbstractWorklistItem> getWorklistItems(@Nonnull AbstractResource<?> resource) {

        return Collections.unmodifiableList(resource.getWorklist().getWorklistItems());
    }

    @Override
    public List<AbstractWorklistItem> getOfferedWorklistItems(AbstractResource<?> resource) {

        List<AbstractWorklistItem> offeredItems = new ArrayList<AbstractWorklistItem>();
        List<AbstractWorklistItem> items = resource.getWorklist().getWorklistItems();
        synchronized (items) {
            for (AbstractWorklistItem item : resource.getWorklist().getWorklistItems()) {
                if (item.getStatus() == WorklistItemState.OFFERED) {
                    offeredItems.add(item);
                }
            }
        }

        return offeredItems;
    }

    @Override
    public List<AbstractWorklistItem> getAllocatedWorklistItems(AbstractResource<?> resource) {

        List<AbstractWorklistItem> allocatedItems = new ArrayList<AbstractWorklistItem>();
        List<AbstractWorklistItem> items = resource.getWorklist().getWorklistItems();
        synchronized (items) {
            for (AbstractWorklistItem item : resource.getWorklist().getWorklistItems()) {
                if (item.getStatus() == WorklistItemState.ALLOCATED) {
                    allocatedItems.add(item);
                }
            }
        }
        return allocatedItems;
    }

    @Override
    public List<AbstractWorklistItem> getExecutingWorklistItems(AbstractResource<?> resource) {

        List<AbstractWorklistItem> executingItems = new ArrayList<AbstractWorklistItem>();
        List<AbstractWorklistItem> items = resource.getWorklist().getWorklistItems();
        synchronized (items) {
            for (AbstractWorklistItem item : resource.getWorklist().getWorklistItems()) {
                if (item.getStatus() == WorklistItemState.EXECUTING) {
                    executingItems.add(item);
                }
            }
        }
        return executingItems;
    }

    @Override
    public void beginWorklistItemBy(AbstractWorklistItem worklistItem, AbstractResource<?> resource) {

        synchronized (worklistItem) {
            if (!getWorklistItems(resource).contains(worklistItem)) {
                logger.debug("thou shalt not beign worklist items that are not your own: {}", resource.getName());
                return;
            }
            claimWorklistItemBy(worklistItem, resource);
            resource.getWorklist().itemIsStarted(worklistItem);
        }

    }

    @Override
    public List<AbstractWorklistItem> getWorklistItems(UUID id)
    throws ResourceNotAvailableException {

        AbstractParticipant resource = identityService.getParticipant(id);
        return this.getWorklistItems(resource);
    }

    @Override
    public List<AbstractWorklistItem> getOfferedWorklistItems(UUID id)
    throws ResourceNotAvailableException {

        AbstractParticipant resource = identityService.getParticipant(id);
        return this.getOfferedWorklistItems(resource);
    }

    @Override
    public List<AbstractWorklistItem> getAllocatedWorklistItems(UUID id)
    throws ResourceNotAvailableException {

        AbstractParticipant resource = identityService.getParticipant(id);
        return this.getAllocatedWorklistItems(resource);
    }

    @Override
    public List<AbstractWorklistItem> getExecutingWorklistItems(UUID id)
    throws ResourceNotAvailableException {

        AbstractParticipant resource = identityService.getParticipant(id);
        return this.getExecutingWorklistItems(resource);
    }
    
    

    @Override
    public void removeWorklistItem(UUID worklistItemId) {

        AbstractWorklistItem worklistItem;

        // Looking in all participants for the worklistItem
        worklistItem = findWorklistItemInResources(worklistItemId,
            new HashSet<AbstractResource<?>>(identityService.getParticipants()));
        
        if (worklistItem == null) {
            // Looking in all positions for the worklistItem
            worklistItem = findWorklistItemInResources(worklistItemId,
                new HashSet<AbstractResource<?>>(identityService.getPositions()));
        
            if (worklistItem == null) {
                // Looking in all roles for the worklistItem
                worklistItem = findWorklistItemInResources(worklistItemId, new HashSet<AbstractResource<?>>(
                    identityService.getRoles()));
            
                if (worklistItem == null) {
                    // Looking in all organizationUnits for the worklistItem
                    worklistItem = findWorklistItemInResources(worklistItemId, new HashSet<AbstractResource<?>>(
                        identityService.getOrganizationUnits()));
                
                    if (worklistItem == null) {
                        String warnMessage = "No worklistItem with the id : '" + worklistItemId + "' could be found.";
                        logger.warn(warnMessage);
                        return;
                    }
                }
            }
        }
        removeWorklistItem(worklistItem, worklistItem.getAssignedResources());
    }

    /**
     * Finds the {@link AbstractWorklistItem worklistItem} in a set of {@link AbstractResource resources}.
     * 
     * @param worklistItemId
     *            - the id of the {@link AbstractWorklistItem worklistItem} to look for
     * @param resources
     *            - set of {@link AbstractResource resources} that should be analyzed
     * @return a {@link AbstractWorklistItem worklistItem} when it was found, otherwise null is returned
     */
    private AbstractWorklistItem findWorklistItemInResources(UUID worklistItemId, Set<AbstractResource<?>> resources) {

        for (AbstractResource<?> abstractResource : resources) {
            for (AbstractWorklistItem worklistItem : abstractResource.getWorklist()) {
                if (worklistItem.getID().equals(worklistItemId)) {
                    return worklistItem;
                }
            }
        }
        return null;
    }

    @Override
    public DetourPattern getCancellationPattern() {

        return defaultCancellationPattern;
    }

    @Override
    public void setCancellationPattern(DetourPattern cancellationPattern) {

        this.defaultCancellationPattern = cancellationPattern;
    }

   
}
