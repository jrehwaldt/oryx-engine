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

// TODO: Auto-generated Javadoc
/**
 * The implementation of the WorklistManager.
 */
public class WorklistManager implements WorklistService, TaskDistribution, WorklistQueue {

    /** The worklist manager. */
    private static WorklistManager worklistManager;
    
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<WorklistItem> getWorkListItems(Resource<?> resource) {

        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWortlistItem(WorklistItem worklistItem, Resource<?> resourceToFillIn) {

        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWorklistItem(WorklistItem worklistItem, List<Resource<?>> resourcesToFillIn) {

        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void distribute(Task task, Token token) {
        Pattern pushPattern = task.getAllocationStrategies().getPushPattern();
        
        pushPattern.execute(task, token, (WorklistQueue) this);
        
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
     */
    @Override
    public void completeWorklistItem(WorklistItem worklistItem) {

        // TODO Auto-generated method stub
        
    }

}
