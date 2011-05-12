package de.hpi.oryxengine.process.instantiation;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.RepositoryServiceInside;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.navigator.NavigatorInside;
import de.hpi.oryxengine.util.ServiceContext;

public class ServiceContextImpl implements ServiceContext {

    @Override
    public RepositoryServiceInside getRepositiory() {
    
        return (RepositoryServiceInside) ServiceFactory.getRepositoryService();
    }

    @Override
    public IdentityService getIdentityService() {
    
        return ServiceFactory.getIdentityService();
    }

    @Override
    public CorrelationManager getCorrelationService() {
    
        return ServiceFactory.getCorrelationService();
    }

    @Override
    public NavigatorInside getNavigatorService() {
    
        return (NavigatorInside) ServiceFactory.getNavigatorService();
    }

}
