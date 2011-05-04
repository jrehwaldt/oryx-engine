package de.hpi.oryxengine;

import javax.annotation.Nonnull;

import org.codehaus.jackson.map.ObjectMapper;

import de.hpi.oryxengine.allocation.TaskAllocation;
import de.hpi.oryxengine.allocation.TaskDistribution;
import de.hpi.oryxengine.bootstrap.JodaEngineAppContext;
import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.navigator.Navigator;

/**
 * General service factory, which provides singleton instances for our system.
 */
public final class ServiceFactory {

    /**
     * Gets the {@link WorklistManager} instance.
     * 
     * @return the worklist manager instance
     */
    protected static @Nonnull WorklistManager getWorklistManagerInstance() {

        return (WorklistManager) JodaEngineAppContext.getBean("worklistService");
    }

    /**
     * Gets the {@link WorklistService}.
     * 
     * @return the worklist service
     */
    public static @Nonnull WorklistService getWorklistService() {

        return getWorklistManagerInstance();
    }

    /**
     * Gets the {@link TaskAllocation} Interface for operating on work lists.
     * 
     * @return the worklist queue
     */
    public static @Nonnull TaskAllocation getWorklistQueue() {

        return getWorklistManagerInstance();
    }

    /**
     * Gets the {@link TaskDistribution}.
     * 
     * @return the task distribution
     */
    public static @Nonnull TaskDistribution getTaskDistribution() {

        return getWorklistManagerInstance();
    }

    /**
     * Gets the {@link IdentityService}.
     * 
     * @return the identity service
     */
    public static @Nonnull IdentityService getIdentityService() {

        return (IdentityService) JodaEngineAppContext.getBean("identityService");
    }

    /**
     * Gets the {@link Navigator}.
     * 
     * @return the navigator service
     */
    public static @Nonnull Navigator getNavigatorService() {

        return (Navigator) JodaEngineAppContext.getBean("navigatorService");
    }

    /**
     * Gets the {@link RepositoryService}.
     * 
     * @return the repository service
     */
    public static @Nonnull RepositoryService getRepositoryService() {

        return (RepositoryService) JodaEngineAppContext.getBean("repositoryService");
    }

    /**
     * Gets the {@link CorrelationManager} for the supplied navigator. As we do not necessarily have only one navigator,
     * we need a CorrelationManager for each of them.
     * 
     * @return the correlation service
     */
    public static @Nonnull CorrelationManager getCorrelationService() {

        return (CorrelationManager) JodaEngineAppContext.getBean("correlationService");
    }
    
    /**
     * Provides the object mapper.
     * 
     * @return the object mapper
     */
    public static @Nonnull ObjectMapper getJsonMapper() {
        return (ObjectMapper) JodaEngineAppContext.getBean("jsonMapper");
    }

    /**
     * Hidden Constructor.
     */
    private ServiceFactory() { }
}
