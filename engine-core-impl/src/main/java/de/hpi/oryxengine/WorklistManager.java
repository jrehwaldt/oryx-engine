package de.hpi.oryxengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.exception.OryxEngineException;
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
    protected static WorklistManager worklistManager;

    private Map<Resource<?>, List<WorklistItem>> lazyWorklistTable;

    /**
     * Gets the worklist manager instance.
     * 
     * @return the worklist manager instance
     */
    private static WorklistManager getWorklistManagerInstance() {

        if (worklistManager == null) {
            worklistManager = new WorklistManager();
        }

        return worklistManager;
    }

    /**
     * Gets the worklist service.
     * 
     * @return the worklist service
     */
    public static WorklistService getWorklistService() {

        return getWorklistManagerInstance();
    }

    /**
     * Gets the worklist queue.
     * 
     * @return the worklist queue
     */
    public static WorklistQueue getWorklistQueue() {

        return getWorklistManagerInstance();
    }

    /**
     * Gets the task distribution.
     * 
     * @return the task distribution
     */
    public static TaskDistribution getTaskDistribution() {

        return getWorklistManagerInstance();
    }

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
     */
    @Override
    public void claimWorklistItem(WorklistItem worklistItem) {

        // TODO Auto-generated method stub

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
     * @throws OryxEngineException 
     */
    @Override
    public void completeWorklistItem(WorklistItem worklistItem) throws OryxEngineException {
        
        WorklistItemImpl worklistItemImpl = extractWorklistItemImplFrom(worklistItem);
        worklistItemImpl.setStatus(WorklistItemState.COMPLETED);
        
        for (Resource<?> resource : worklistItemImpl.getAssignedResources()) {
            getWorklistItems(resource).remove(worklistItemImpl);
        }
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
    public void beginWorklistItem(WorklistItem worklistItem) throws OryxEngineException {

        WorklistItemImpl worklistItemImpl = extractWorklistItemImplFrom(worklistItem);
        worklistItemImpl.setStatus(WorklistItemState.EXECUTING);
    }

    /**
     * Translates a WorklistItem into a corresponding WorklistItemImpl object.
     * 
     * @param role
     *            - a {@link WorklistItem} object
     * @return roleImpl - the casted {@link WorklistItemImpl} object
     * @throws OryxEngineException
     *             - an {@link OryxEngineException}
     */
    private WorklistItemImpl extractWorklistItemImplFrom(WorklistItem worklistItem)
    throws OryxEngineException {

        if (worklistItem == null) {
            throw new OryxEngineException("The WorklistItem parameter is null.");
        }
        WorklistItemImpl worklistItemImpl = (WorklistItemImpl) worklistItem;

        return worklistItemImpl;
    }

}
