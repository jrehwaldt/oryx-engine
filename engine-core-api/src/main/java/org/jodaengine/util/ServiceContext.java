package org.jodaengine.util;

import org.jodaengine.IdentityService;
import org.jodaengine.RepositoryServiceInside;
import org.jodaengine.eventmanagement.EventCorrelator;
import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.navigator.NavigatorInside;
import org.jodaengine.resource.allocation.TaskAllocation;

/**
 * This context provides basic functionality. It holds all relevant services that our system provides.
 */
public interface ServiceContext extends Attributable {

    /**
     * Gets the {@link RepositoryServiceInside repositoryService}.
     * 
     * @return the {@link RepositoryServiceInside}
     */
    RepositoryServiceInside getRepositiory();

    /**
     * Gets the {@link IdentityService identityService}.
     * 
     * @return the {@link IdentityService}
     */
    IdentityService getIdentityService();
    
    /**
     * Gets the {@link WorklistService}.
     * 
     * @return the {@link WorklistService}
     */
    TaskAllocation getWorklistService();

    /**
     * Gets the {@link EventCorrelator correlationService}.
     * 
     * @return the {@link EventCorrelator}
     */
    EventSubscriptionManager getCorrelationService();

    /**
     * Gets the {@link NavigatorInside navigatorService}.
     * 
     * @return the {@link NavigatorInside}
     */
    NavigatorInside getNavigatorService();

    /**
     * Gets the {@link ExtensionService extensionService}.
     * 
     * @return the {@link ExtensionService}
     */
    ExtensionService getExtensionService();
}
