package de.hpi.oryxengine;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.allocation.TaskAllocation;
import de.hpi.oryxengine.allocation.TaskDistribution;
import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.correlation.CorrelationManagerImpl;
import de.hpi.oryxengine.deploy.Deployer;
import de.hpi.oryxengine.navigator.Navigator;

/**
 * General service factory, which provides singleton instances for our system.
 */
public final class ServiceFactory {
    
    protected static @Nonnull WorklistManager getWorklistManagerInstance() {

        return (WorklistManager) OryxEngineAppContext.getBean("worklistService");
    }

    public static @Nonnull WorklistService getWorklistService() {

        return getWorklistManagerInstance();
    }

    public static @Nonnull TaskAllocation getWorklistQueue() {

        return getWorklistManagerInstance();
    }

    public static @Nonnull TaskDistribution getTaskDistribution() {

        return getWorklistManagerInstance();
    }

    public static @Nonnull IdentityService getIdentityService() {

        return (IdentityService) OryxEngineAppContext.getBean("identityService");
    }

    public static @Nonnull Navigator getNavigatorService() {

        return (Navigator) OryxEngineAppContext.getBean("navigatorService");
    }

    public static @Nonnull Deployer getDeplyomentService() {

        return (Deployer) OryxEngineAppContext.getBean("deployerService");
    }

    public static @Nonnull RepositoryService getRepositoryService() {

        return (RepositoryService) OryxEngineAppContext.getBean("repositoryService");
    }

    /**
     * Gets the correlation service for the supplied navigator. As we do not necessarily have only one navigator, we
     * need a CorrelationManager for each of them.
     * 
     * @return the correlation service
     */
    public static @Nonnull CorrelationManager getCorrelationService() {
        
        return  (CorrelationManager) OryxEngineAppContext.getBean("correlationService");
    }
    
    /**
     * Hidden Constructor.
     */
    private ServiceFactory() { }
}
