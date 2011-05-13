package de.hpi.oryxengine.process.instantiation;

import java.util.HashMap;
import java.util.Map;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.RepositoryServiceInside;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.navigator.NavigatorInside;
import de.hpi.oryxengine.util.ServiceContext;

/**
 * Implementation of the {@link ServiceContext ServiceContext-Interface}.
 */
public class ServiceContextImpl implements ServiceContext {

    private Map<String, Object> attributeTable;
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
    @Override
    public Map<String, Object> getAttributes() {

        if (this.attributeTable == null) {
            this.attributeTable = new HashMap<String, Object>();
        }
            
        return this.attributeTable;
    }

    @Override
    public Object getAttribute(String attributeKey) {

        return getAttributes().get(attributeKey);
    }

    @Override
    public void setAttribute(String attributeKey, Object attributeValue) {

        getAttributes().put(attributeKey, attributeValue);
    }
}
