package org.jodaengine.rest.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.rest.api.IdentityWebService;
import org.jodaengine.rest.api.NavigatorWebService;
import org.jodaengine.rest.api.RepositoryWebService;
import org.jodaengine.rest.api.WorklistWebService;
import org.jodaengine.rest.exception.BadRequestMapper;
import org.jodaengine.rest.exception.DefinitionNotFoundMapper;
import org.jodaengine.rest.exception.IllegalArgumentMapper;
import org.jodaengine.rest.exception.InvalidWorkItemMapper;
import org.jodaengine.rest.exception.ResourceNotAvailableMapper;


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
