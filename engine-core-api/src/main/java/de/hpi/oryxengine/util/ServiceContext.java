package de.hpi.oryxengine.util;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.RepositoryServiceInside;
import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.navigator.NavigatorInside;

public interface ServiceContext {

    RepositoryServiceInside getRepositiory();

    IdentityService getIdentityService();

    CorrelationManager getCorrelationService();
    
    NavigatorInside getNavigatorService();
}
