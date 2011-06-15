package org.jodaengine.util;

import java.util.HashMap;
import java.util.Map;

import org.jodaengine.RepositoryServiceInside;
import org.jodaengine.ServiceFactory;
import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.navigator.NavigatorInside;
import org.jodaengine.resource.IdentityService;
import org.jodaengine.resource.worklist.WorklistServiceIntern;


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
    public EventSubscriptionManager getCorrelationService() {
        return ServiceFactory.getCorrelationService();
    }

    @Override
    public NavigatorInside getNavigatorService() {
        return (NavigatorInside) ServiceFactory.getNavigatorService();
    }

    @Override
    public ExtensionService getExtensionService() {
        return (ExtensionService) ServiceFactory.getExtensionService();
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

    @Override
    public WorklistServiceIntern getWorklistService() {

        return ServiceFactory.getInteralWorklistService();
    }
}
