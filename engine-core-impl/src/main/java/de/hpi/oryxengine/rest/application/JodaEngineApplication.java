package de.hpi.oryxengine.rest.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import de.hpi.oryxengine.JodaEngineServices;
import de.hpi.oryxengine.bootstrap.JodaEngine;
import de.hpi.oryxengine.rest.api.IdentityWebService;
import de.hpi.oryxengine.rest.api.NavigatorWebService;
import de.hpi.oryxengine.rest.api.RepositoryWebService;
import de.hpi.oryxengine.rest.api.WorklistWebService;
import de.hpi.oryxengine.rest.exception.BadRequestMapper;
import de.hpi.oryxengine.rest.exception.DefinitionNotFoundMapper;
import de.hpi.oryxengine.rest.exception.IllegalArgumentMapper;
import de.hpi.oryxengine.rest.exception.InvalidWorkItemMapper;
import de.hpi.oryxengine.rest.exception.ResourceNotAvailableMapper;

/**
 * The Class JodaEngineApplication registers resources and providers and configures them correctly.
 */
public class JodaEngineApplication extends Application {
    
    protected JodaEngineServices engineServices;
    protected Set<Class<?>> classes;
    protected Set<Object> singletons;
    
    /**
     * Starts the Joda Engine and registers Providers and Resources.
     */
    public JodaEngineApplication() {
        engineServices = JodaEngine.start();
        classes = new HashSet<Class<?>>();
        singletons = new HashSet<Object>();
        
        // add the exception providers
        classes.add(BadRequestMapper.class);
        classes.add(DefinitionNotFoundMapper.class);
        classes.add(IllegalArgumentMapper.class);
        classes.add(InvalidWorkItemMapper.class);
        classes.add(ResourceNotAvailableMapper.class);
        
        // add the resources
        singletons.add(new WorklistWebService(engineServices));
        singletons.add(new NavigatorWebService(engineServices));
        singletons.add(new RepositoryWebService(engineServices));
        singletons.add(new IdentityWebService(engineServices));
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
    
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
