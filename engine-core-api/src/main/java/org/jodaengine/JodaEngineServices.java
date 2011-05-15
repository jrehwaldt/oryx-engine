package org.jodaengine;

import javax.annotation.Nonnull;

import org.jodaengine.navigator.Navigator;

/**
 * General Interface for all Services that the JodaEngine provides.
 * <p>
 * Currently there are the following Services available:
 * <ul>
 * <li>{@link RepositoryService}</li>
 * <li>{@link Navigator}</li>
 * <li>{@link IdentityService}</li>
 * <li>{@link WorklistService}</li>
 * </ul>
 * </p>
 */
public interface JodaEngineServices {

    /**
     * Gets the {@link WorklistService}.
     * 
     * @return the {@link WorklistService worklistService}
     */
    @Nonnull
    WorklistService getWorklistService();

    /**
     * Gets the {@link IdentityService}.
     * 
     * @return the {@link IdentityService identityService}
     */
    @Nonnull
    IdentityService getIdentityService();

    /**
     * Gets the {@link Navigator}.
     * 
     * @return the {@link Navigator navigatorService}
     */
    @Nonnull
    Navigator getNavigatorService();

    /**
     * Gets the {@link RepositoryService}.
     * 
     * @return the {@link RepositoryService repositoryService}
     */
    @Nonnull
    RepositoryService getRepositoryService();

    void shutdown();
}
