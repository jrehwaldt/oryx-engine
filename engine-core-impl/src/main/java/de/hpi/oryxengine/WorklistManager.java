package de.hpi.oryxengine;

import java.util.List;
import java.util.Map;

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

    private static WorklistManager worklistManager;
    
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
    public void addWortlistItem(WorklistItem worklistItem, Resource<?> resourceToFillIn) {

        // TODO Auto-generated method stub
        
    }

    @Override
    public void addWorklistItem(WorklistItem worklistItem, List<Resource<?>> resourcesToFillIn) {

        // TODO Auto-generated method stub
        
    }

    @Override
    public void distribute(Task task, Token token) {
        Pattern pushPattern = task.getAllocationStrategies().getPushPattern();
        
        pushPattern.execute(task, token, (WorklistQueue) this);
        
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

        // TODO Auto-generated method stub
        
    }

}
