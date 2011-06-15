package org.jodaengine;

import java.util.List;

import javax.annotation.Nonnull;

import org.jodaengine.bootstrap.Service;
import org.jodaengine.eventmanagement.EventManagerService;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.resource.IdentityService;
import org.jodaengine.resource.worklist.WorklistService;

/**
 * General Interface for all Services that the {@link JodaEngine} provides.
 * <p>
 * Currently there are the following services available:
 * <ul>
 * <li>{@link RepositoryService}</li>
 * <li>{@link Navigator}</li>
 * <li>{@link IdentityService}</li>
 * <li>{@link WorklistService}</li>
 * <li>{@link ExtensionService}</li>
 * </ul>
 * </p>
 */
public interface JodaEngineServices {

    /**
     * Gets the {@link WorklistService}.
     * 
     * @return the {@link WorklistService worklistService}
     */
    @Nonnull WorklistService getWorklistService();

    /**
     * Gets the {@link IdentityService}.
     * 
     * @return the {@link IdentityService identityService}
     */
    @Nonnull IdentityService getIdentityService();

    /**
     * Gets the {@link Navigator}.
     * 
     * @return the {@link Navigator navigatorService}
     */
    @Nonnull Navigator getNavigatorService();

    /**
     * Gets the {@link RepositoryService}.
     * 
     * @return the {@link RepositoryService repositoryService}
     */
    @Nonnull RepositoryService getRepositoryService();

    /**
     * Gets the {@link ExtensionService}.
     * 
     * @return the {@link ExtensionService}
     */
    @Nonnull ExtensionService getExtensionService();
    
    /**
     * Gets the {@link EventManagerService}.
     *
     * @return the {@link EventManagerService}
     */
    @Nonnull EventManagerService getEventManagerService();
    
    /**
     * Gets a list of all core {@link Service}s.
     * 
     * @return the core services
     */
    @Nonnull List<Service> getCoreServices();
    
    /**
     * Stops the specified service and shuts it's components down.
     */
    void stop();
}
