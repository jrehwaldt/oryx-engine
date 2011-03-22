package de.hpi.oryxengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.exception.OryxEngineException;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.Resource;
import de.hpi.oryxengine.util.WorklistTable;
import de.hpi.oryxengine.worklist.Pattern;
import de.hpi.oryxengine.worklist.ResourceViewResolver;
import de.hpi.oryxengine.worklist.Task;
import de.hpi.oryxengine.worklist.TaskDistribution;
import de.hpi.oryxengine.worklist.Worklist;
import de.hpi.oryxengine.worklist.WorklistItem;
import de.hpi.oryxengine.worklist.WorklistItemImpl;
import de.hpi.oryxengine.worklist.WorklistItemState;
import de.hpi.oryxengine.worklist.WorklistQueue;

/**
 * The implementation of the WorklistManager.
 */
public class WorklistManager implements WorklistService, TaskDistribution, WorklistQueue {

    private WorklistTable<Resource<?>, Worklist> lazyWorklistTable;

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

        resourceToFillIn.getWorklist().getWorklistItems().add(worklistItem);

    }

    @Override
    public void addWorklistItem(WorklistItem worklistItem, List<Resource<?>> resourcesToFillIn) {

        for (Resource<?> resource : resourcesToFillIn) {
            addWorklistItem(worklistItem, resource);
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

        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws OryxEngineException
     */
    @Override
    public void claimWorklistItemBy(WorklistItem worklistItem, Resource<?> resource) throws OryxEngineException {

        for (Resource<?> resource2 : worklistItem.getAssignedResources()) {
            resource2.getWorklist().itemIsAllocated(worklistItem);
        }
        
        resource.getWorklist().addWorklistItem(worklistItem);
        
        for (Resource<?> resourceAssignedToWorklist : worklistItemImpl.getAssignedResources()) {
            List<WorklistItem> worklistItemsAssignedResource = getWorklistTable().get(resourceAssignedToWorklist);
            if (worklistItemsAssignedResource.contains(worklistItemImpl)) {
                worklistItemsAssignedResource.remove(worklistItemImpl);
            }
        }
        
//        worklistItemImpl.get
//        getWorklistTable().get(resource)
        worklistItemImpl.setStatus(WorklistItemState.ALLOCATED);
    }

    @Override
    public void abortWorklistItemBy(WorklistItem worklistItem, Resource<?> resource) {

        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     * 
     * @throws Exception
     */
    @Override
    public void completeWorklistItemBy(WorklistItem worklistItem, Resource<?> resource)
    throws Exception {

        WorklistItemImpl worklistItemImpl = asWorklistItemImpl(worklistItem);
        worklistItemImpl.setStatus(WorklistItemState.COMPLETED);

        for (Resource<?> assignedResource : worklistItemImpl.getAssignedResources()) {
            getWorklistTable().get(assignedResource).remove(worklistItemImpl);
        }

        worklistItemImpl.getCorrespondingToken().resume();
    }

    @Override
    public List<WorklistItem> getWorklistItems(@Nonnull Resource<?> resource) {

        return getWorklistTable().get(resource).getWorklistItems();

        // List<Resource<?>> resourcesRelatedToResource = ResourceViewResolver.extractResourcesFor(resource);
        //
        // for (Resource<?> relatedResource : resourcesRelatedToResource) {
        //
        // List<WorklistItem> worklistItemsOfRelatedResource =
        // getWorklistTable().get(relatedResource).getWorklistItems();
        // worklistItemsForResource.addAll(worklistItemsOfRelatedResource);
        // }
        //
        // List<WorklistItem> worklistItemsOfResource = getWorklistTable().get(resource);
        // worklistItemsForResource.addAll(worklistItemsOfResource);
        //
        // return worklistItemsForResource;
    }

    private WorklistTable<Resource<?>, Worklist> getWorklistTable() {

        if (lazyWorklistTable == null) {
            lazyWorklistTable = new WorklistTable<Resource<?>, Worklist>();
        }
        return lazyWorklistTable;
    }

    @Override
    public void beginWorklistItemBy(WorklistItem worklistItem, Resource<?> resource)
    throws OryxEngineException {

        WorklistItemImpl worklistItemImpl = asWorklistItemImpl(worklistItem);
        worklistItemImpl.setStatus(WorklistItemState.EXECUTING);
    }
}
