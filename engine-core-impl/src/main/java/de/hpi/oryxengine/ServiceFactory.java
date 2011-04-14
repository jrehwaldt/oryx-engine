package de.hpi.oryxengine;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.allocation.TaskAllocation;
import de.hpi.oryxengine.allocation.TaskDistribution;
import de.hpi.oryxengine.bootsstrap.OryxEngineAppContext;
import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.repository.Deployer;

/**
 * General service factory, which provides singleton instances for our system.
 */
public final class ServiceFactory {

    /**
     * Gets the {@link WorklistManager} instance.
     * 
     * @return the worklist manager instance
     */
    protected static @Nonnull
    WorklistManager getWorklistManagerInstance() {

        return (WorklistManager) OryxEngineAppContext.getBean("worklistService");
    }

    /**
     * Gets the {@link WorklistService}.
     * 
     * @return the worklist service
     */
    public static @Nonnull
    WorklistService getWorklistService() {

        return getWorklistManagerInstance();
    }

    /**
     * Gets the {@link TaskAllocation} Interface for operating on work lists.
     * 
     * @return the worklist queue
     */
    public static @Nonnull
    TaskAllocation getWorklistQueue() {

        return getWorklistManagerInstance();
    }

    /**
     * Gets the {@link TaskDistribution}.
     * 
     * @return the task distribution
     */
    public static @Nonnull
    TaskDistribution getTaskDistribution() {

        return getWorklistManagerInstance();
    }

    /**
     * Gets the {@link IdentityService}.
     * 
     * @return the identity service
     */
    public static @Nonnull
    IdentityService getIdentityService() {

        return (IdentityService) OryxEngineAppContext.getBean("identityService");
    }

    /**
     * Gets the {@link Navigator}.
     * 
     * @return the navigator service
     */
    public static @Nonnull
    Navigator getNavigatorService() {

        return (Navigator) OryxEngineAppContext.getBean("navigatorService");
    }

    /**
     * Gets the {@link Deployer}.
     * 
     * @return the deplyoment service
     */
    public static @Nonnull
    Deployer getDeplyomentService() {

        return (Deployer) OryxEngineAppContext.getBean("deployerService");
    }

    /**
     * Gets the {@link RepositoryService}.
     * 
     * @return the repository service
     */
    public static @Nonnull
    RepositoryService getRepositoryService() {

        return (RepositoryService) OryxEngineAppContext.getBean("repositoryService");
    }

    /**
     * Gets the {@link CorrelationManager} for the supplied navigator. As we do not necessarily have only one navigator,
     * we need a CorrelationManager for each of them.
     * 
     * @return the correlation service
     */
    public static @Nonnull
    CorrelationManager getCorrelationService() {

        return (CorrelationManager) OryxEngineAppContext.getBean("correlationService");
    }

    /**
     * Hidden Constructor.
     */
    private ServiceFactory() {

    }
}
