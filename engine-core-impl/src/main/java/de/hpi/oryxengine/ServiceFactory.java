package de.hpi.oryxengine;

import de.hpi.oryxengine.worklist.TaskDistribution;
import de.hpi.oryxengine.worklist.WorklistQueue;

/**
 * 
 */
public class ServiceFactory {

    protected static WorklistManager worklistManager;
    
    protected static IdentityService identityService;
    
    /**
     * Gets the worklist manager instance.
     * 
     * @return the worklist manager instance
     */
    protected static WorklistManager getWorklistManagerInstance() {

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
    
    public static IdentityService getIdentityService() {

        if (identityService == null) {
            identityService = new IdentityServiceImpl();
        }

        return identityService;
    }
}
