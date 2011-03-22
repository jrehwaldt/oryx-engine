package de.hpi.oryxengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.Resource;
import de.hpi.oryxengine.worklist.Pattern;
import de.hpi.oryxengine.worklist.Task;
import de.hpi.oryxengine.worklist.TaskDistribution;
import de.hpi.oryxengine.worklist.WorklistItem;
import de.hpi.oryxengine.worklist.WorklistItemImpl;
import de.hpi.oryxengine.worklist.WorklistItemState;
import de.hpi.oryxengine.worklist.WorklistQueue;

/**
 * The implementation of the WorklistManager.
 */
public class WorklistManager implements WorklistService, TaskDistribution, WorklistQueue {

    /** The worklist manager. */
//    protected static WorklistManager worklistManager;

    private Map<Resource<?>, List<WorklistItem>> lazyWorklistTable;

    

    

    @Override
    public void addWorklistItem(WorklistItem worklistItem, Resource<?> resourceToFillIn) {

        List<WorklistItem> worklistForResources = getWorklistTable().get(resourceToFillIn);
        if (worklistForResources == null) {

            worklistForResources = new ArrayList<WorklistItem>();
            worklistForResources.add(worklistItem);
            getWorklistTable().put(resourceToFillIn, worklistForResources);

        } else {

            worklistForResources.add(worklistItem);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWorklistItem(WorklistItem worklistItem, List<Resource<?>> resourcesToFillIn) {

        for (Resource<?> resource : resourcesToFillIn) {
            addWorklistItem(worklistItem, resource);
        }
    }

    /**
     * {@inheritDoc}
     */
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
     * @throws DalmatinaException 
     */
    @Override
    public void claimWorklistItem(WorklistItem worklistItem) throws DalmatinaException {

        WorklistItemImpl worklistItemImpl = extractWorklistItemImplFrom(worklistItem);
        worklistItemImpl.setStatus(WorklistItemState.ALLOCATED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void abortWorklistItem(WorklistItem worklistItem) {

        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void completeWorklistItem(WorklistItem worklistItem) throws DalmatinaException {
        
        WorklistItemImpl worklistItemImpl = extractWorklistItemImplFrom(worklistItem);
        worklistItemImpl.setStatus(WorklistItemState.COMPLETED);
        
        for (Resource<?> resource : worklistItemImpl.getAssignedResources()) {
            getWorklistItems(resource).remove(worklistItemImpl);
        }
        
        worklistItemImpl.getCorrespondingToken().resume();
    }

    @Override
    public List<WorklistItem> getWorklistItems(@Nonnull Resource<?> resource) {

        List<WorklistItem> worklistForResources = getWorklistTable().get(resource);
        if (worklistForResources == null) {
            worklistForResources = Collections.emptyList();
            return worklistForResources;
        }
        return worklistForResources;
    }

    private Map<Resource<?>, List<WorklistItem>> getWorklistTable() {

        if (lazyWorklistTable == null) {
            lazyWorklistTable = new HashMap<Resource<?>, List<WorklistItem>>();
        }
        return lazyWorklistTable;
    }

    @Override
    public void beginWorklistItem(WorklistItem worklistItem) throws DalmatinaException {

        WorklistItemImpl worklistItemImpl = extractWorklistItemImplFrom(worklistItem);
        worklistItemImpl.setStatus(WorklistItemState.EXECUTING);
    }

    /**
     * Translates a WorklistItem into a corresponding WorklistItemImpl object.
     * 
     * @param worklistItem
     *            - a {@link WorklistItem} object
     * @return roleImpl - the casted {@link WorklistItemImpl} object
     * @throws DalmatinaException
     *             - an {@link DalmatinaException}
     */
    private WorklistItemImpl extractWorklistItemImplFrom(WorklistItem worklistItem)
    throws DalmatinaException {

        if (worklistItem == null) {
            throw new DalmatinaException("The WorklistItem parameter is null.");
        }
        WorklistItemImpl worklistItemImpl = (WorklistItemImpl) worklistItem;

        return worklistItemImpl;
    }

}
