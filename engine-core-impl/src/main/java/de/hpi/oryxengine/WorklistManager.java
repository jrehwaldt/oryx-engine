package de.hpi.oryxengine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.Resource;
import de.hpi.oryxengine.worklist.Pattern;
import de.hpi.oryxengine.worklist.Task;
import de.hpi.oryxengine.worklist.TaskDistribution;
import de.hpi.oryxengine.worklist.WorklistItem;
import de.hpi.oryxengine.worklist.WorklistQueue;

/**
 * The implementation of the WorklistManager.
 */
public class WorklistManager implements WorklistService, TaskDistribution, WorklistQueue {

    @Override
    public void addWorklistItem(WorklistItem worklistItem, Resource<?> resourceToFillIn) {

        // List<WorklistItem> worklistForResources = getWorklistTable().get(resourceToFillIn);
        // if (worklistForResources == null) {
        //
        // worklistForResources = new ArrayList<WorklistItem>();
        // worklistForResources.add(worklistItem);
        // getWorklistTable().put(resourceToFillIn, worklistForResources);
        //
        // } else {
        //
        // worklistForResources.add(worklistItem);
        // }

        // getWorklistTable().addWorklistItemTo(resourceToFillIn, worklistItem);

        try {
            resourceToFillIn.getWorklist().addWorklistItem(worklistItem);
        } catch (DalmatinaException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addWorklistItem(WorklistItem worklistItem, Set<Resource<?>> resourcesToFillIn) {

        Resource<?>[] resourcesToFillInArray = (Resource<?>[]) resourcesToFillIn
        .toArray(new Resource<?>[resourcesToFillIn.size()]);

        for (int i = 0; i < resourcesToFillInArray.length; i++) {
            Resource<?> resourceToFillIn = resourcesToFillInArray[i];
            addWorklistItem(worklistItem, resourceToFillIn);
        }
    }

    @Override
    public void distribute(Task task, Token token) {

        Pattern pushPattern = task.getAllocationStrategies().getPushPattern();

        pushPattern.execute(task, token, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Resource<?>, List<WorklistItem>> getWorklistItems(List<Resource<?>> resources) {
        
        Map<Resource<?>, List<WorklistItem>> result = new HashMap<Resource<?>, List<WorklistItem>>();
        
        for (Resource<?> r: resources) {
            result.put(r, getWorklistItems(r));
        }
        
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws DalmatinaException
     */
    @Override
    public void claimWorklistItemBy(WorklistItem worklistItem, Resource<?> resource)
    throws DalmatinaException {

        Set<Resource<?>> resourcesToNotify = new HashSet<Resource<?>>();
        resourcesToNotify.add(resource);
        resourcesToNotify.addAll(worklistItem.getAssignedResources());
        for (Resource<?> resourceToNotify : resourcesToNotify) {
            resourceToNotify.getWorklist().itemIsAllocatedBy(worklistItem, resource);
        }

        // resource.getWorklist().addWorklistItem(worklistItem);
        // resource.getWorklist().itemIsAllocated(worklistItem);
    }

    @Override
    public void abortWorklistItemBy(WorklistItem worklistItem, Resource<?> resource) {


    }

    @Override
    public void completeWorklistItemBy(WorklistItem worklistItem, Resource<?> resource)
    throws DalmatinaException {

        resource.getWorklist().itemIsCompleted(worklistItem);

        worklistItem.getCorrespondingToken().resume();
    }

    @Override
    public List<WorklistItem> getWorklistItems(@Nonnull Resource<?> resource) {

        return resource.getWorklist().getWorklistItems();
    }

    @Override
    public void beginWorklistItemBy(WorklistItem worklistItem, Resource<?> resource)
    throws DalmatinaException {

        resource.getWorklist().itemIsStarted(worklistItem);
    }
}
