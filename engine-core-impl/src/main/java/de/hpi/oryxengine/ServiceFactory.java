package de.hpi.oryxengine;

import java.util.HashMap;

import de.hpi.oryxengine.allocation.TaskDistribution;
import de.hpi.oryxengine.allocation.TaskAllocation;
import de.hpi.oryxengine.correlation.CorrelationManagerImpl;
import de.hpi.oryxengine.deploy.Deployer;
import de.hpi.oryxengine.deploy.DeployerImpl;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.repository.ProcessRepository;
import de.hpi.oryxengine.repository.ProcessRepositoryImpl;

/**
 * 
 */
public class ServiceFactory {

    protected static WorklistManager worklistManager;

    protected static IdentityService identityService;

    protected static Deployer deployer;

    protected static ProcessRepository repo;

    protected static HashMap<Navigator, CorrelationManagerImpl> correlationManagers = 
        new HashMap<Navigator, CorrelationManagerImpl>();

    /**
     * Gets the worklist manager instance.
     * 
     * @return the worklist manager instance
     */
    protected synchronized static WorklistManager getWorklistManagerInstance() {

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
    public static TaskAllocation getWorklistQueue() {

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

    public synchronized static IdentityService getIdentityService() {

        if (identityService == null) {
            identityService = new IdentityServiceImpl();
        }

        return identityService;
    }

    /**
     * Deplyoment service.
     * 
     * @return the deployer service.
     */
    public synchronized static Deployer getDeplyomentService() {

        if (deployer == null) {
            deployer = new DeployerImpl();
        }
        return deployer;
    }

    /**
     * Gets the repository service.
     * 
     * @return the repository service
     */
    public synchronized static ProcessRepository getRepositoryService() {

        if (repo == null) {
            repo = new ProcessRepositoryImpl();
        }
        return repo;
    }

    /**
     * Gets the correlation service for the supplied navigator. As we do not necessarily have only one navigator, we
     * need a CorrelationManager for each of them.
     * 
     * @param nav
     *            the nav
     * @return the correlation service
     */
    public synchronized static CorrelationManagerImpl getCorrelationService(Navigator nav) {

        CorrelationManagerImpl correlation = correlationManagers.get(nav);
        if (correlation == null) {
            correlation = new CorrelationManagerImpl(nav);
            correlationManagers.put(nav, new CorrelationManagerImpl(nav));
        }
        return correlation;
    }
}
