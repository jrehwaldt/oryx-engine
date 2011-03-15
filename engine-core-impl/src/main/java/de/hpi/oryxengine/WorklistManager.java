package de.hpi.oryxengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

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

    protected static WorklistManager worklistManager;
    
    private Map<Resource<?>, List<WorklistItem>> lazyWorklistTable;
    
    private static WorklistManager getWorklistManagerInstance() {
        
        if (worklistManager == null) {
            worklistManager = new WorklistManager();
        }
        
        return worklistManager;
    }
    
    
    public static WorklistService getWorklistService() {
        return getWorklistManagerInstance();
    }

    public static WorklistQueue getWorklistQueue() {
        return getWorklistManagerInstance();
    }
    
    public static TaskDistribution getTaskDistribution() {
        return getWorklistManagerInstance();
    }
    
    @Override
    public List<WorklistItem> getWorkListItems(Resource<?> resource) {

        // TODO Auto-generated method stub
        return null;
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

    @Override
    public Map<Resource<?>, List<WorklistItem>> getWorklistItems(List<Resource<?>> resources) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void claimWorklistItem(WorklistItem worklistItem) {

        // TODO Auto-generated method stub
        
    }

    @Override
    public void abortWorklistItem(WorklistItem worklistItem) {

        // TODO Auto-generated method stub
        
    }

    @Override
    public void completeWorklistItem(WorklistItem worklistItem) {

        if (worklistItem)
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
    public void beginWorklistItem(WorklistItem worklistItem) {

        WorklistItemImpl worklistItemImpl = (WorklistItemImpl) worklistItem;
        worklistItemImpl.setStatus(WorklistItemState.EXECUTING);
    }    
    
}
