package de.hpi.oryxengine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.allocation.Pattern;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.allocation.TaskAllocation;
import de.hpi.oryxengine.allocation.TaskDistribution;
import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.worklist.WorklistItem;

/**
 * The implementation of the WorklistManager.
 */
public class WorklistManager implements WorklistService, TaskDistribution, TaskAllocation, Service {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void start() {

        logger.info("Starting the correlation manager");
    }

    @Override
    public void stop() {

        logger.info("Stopping the correlation manager");
    }

    @Override
    public void addWorklistItem(WorklistItem worklistItem, AbstractResource<?> resourceToFillIn) {

        resourceToFillIn.getWorklist().addWorklistItem(worklistItem);
    }

    @Override
    public void addWorklistItem(WorklistItem worklistItem, Set<AbstractResource<?>> resourcesToFillIn) {

        AbstractResource<?>[] resourcesToFillInArray = (AbstractResource<?>[]) resourcesToFillIn
        .toArray(new AbstractResource<?>[resourcesToFillIn.size()]);

        for (int i = 0; i < resourcesToFillInArray.length; i++) {
            AbstractResource<?> resourceToFillIn = resourcesToFillInArray[i];
            addWorklistItem(worklistItem, resourceToFillIn);
        }
    }

    @Override
    public void distribute(Task task, Token token) {

        Pattern pushPattern = task.getAllocationStrategies().getPushPattern();

        pushPattern.execute(task, token, this);
    }

    @Override
    public @Nullable
    WorklistItem getWorklistItem(@Nonnull AbstractResource<?> resource, @Nonnull UUID worklistItemId) {

        for (final WorklistItem item : resource.getWorklist()) {
            if (worklistItemId.equals(item.getID())) {
                return item;
            }
        }

        return null;
    }

    @Override
    public Map<AbstractResource<?>, List<WorklistItem>> getWorklistItems(List<AbstractResource<?>> resources) {

        Map<AbstractResource<?>, List<WorklistItem>> result = new HashMap<AbstractResource<?>, List<WorklistItem>>();

        for (AbstractResource<?> r : resources) {
            result.put(r, getWorklistItems(r));
        }

        return result;
    }

    @Override
    public void claimWorklistItemBy(WorklistItem worklistItem, AbstractResource<?> resource) {

        Set<AbstractResource<?>> resourcesToNotify = new HashSet<AbstractResource<?>>();
        resourcesToNotify.add(resource);
        resourcesToNotify.addAll(worklistItem.getAssignedResources());
        for (AbstractResource<?> resourceToNotify : resourcesToNotify) {
            resourceToNotify.getWorklist().itemIsAllocatedBy(worklistItem, resource);
        }
    }

    @Override
    public void abortWorklistItemBy(WorklistItem worklistItem, AbstractResource<?> resource) {

    }

    @Override
    public void completeWorklistItemBy(WorklistItem worklistItem, AbstractResource<?> resource) {

        resource.getWorklist().itemIsCompleted(worklistItem);

        try {

            worklistItem.getCorrespondingToken().resume();

        } catch (DalmatinaException e) {

            // TODO Logger message
            throw new DalmatinaRuntimeException(e.getMessage());
        }
    }

    @Override
    public List<WorklistItem> getWorklistItems(@Nonnull AbstractResource<?> resource) {

        return resource.getWorklist().getWorklistItems();
    }

    @Override
    public void beginWorklistItemBy(WorklistItem worklistItem, AbstractResource<?> resource) {

        resource.getWorklist().itemIsStarted(worklistItem);
    }
}
