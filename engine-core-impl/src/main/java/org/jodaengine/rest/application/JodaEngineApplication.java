package org.jodaengine.rest.application;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Application;
import javax.ws.rs.ext.ExceptionMapper;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.bootstrap.Service;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.rest.api.IdentityWebService;
import org.jodaengine.rest.api.NavigatorWebService;
import org.jodaengine.rest.api.RepositoryWebService;
import org.jodaengine.rest.api.WorklistWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;


/**
 * This starts the {@link JodaEngine} from within
 * a web container and registers resources as well as {@link javax.ws.rs.ext.Provider}, which
 * will correctly be started.
 */
public class JodaEngineApplication extends Application {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    protected JodaEngineServices engineServices;
    protected Set<Class<?>> classes;
    protected Set<Object> singletons;
    
    /**
     * Starts the {@link JodaEngine} and registers {@link javax.ws.rs.ext.Provider}s
     * and REST-{@link org.jodaengine.bootstrap.Service}s.
     */
    public JodaEngineApplication() {
        
        //
        // start the engine and it's services
        //
        logger.info("Start the web application");
        engineServices = JodaEngine.start();
        classes = new HashSet<Class<?>>();
        singletons = new HashSet<Object>();
        
        //
        // add the exception providers
        //
        logger.info("Add all exception mapper");
        classes.addAll(getExceptionMappers());
        
        //
        // add the core resources
        //
        logger.info("Add core web services");
        singletons.add(new WorklistWebService(engineServices));
        singletons.add(new NavigatorWebService(engineServices));
        singletons.add(new RepositoryWebService(engineServices));
        singletons.add(new IdentityWebService(engineServices));
        
        //
        // add extension services
        //
        logger.info("Add extension web services");
        ExtensionService extensionService = this.engineServices.getExtensionService();
        List<Service> extensionWebServices = extensionService.getExtensionWebServiceSingletons();
        singletons.addAll(extensionWebServices);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
    
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
    
    /**
     * Returns a list of {@link ExceptionMapper}s.
     * 
     * @return exception mappers
     */
    @SuppressWarnings("unchecked")
    private Set<Class<? extends ExceptionMapper<?>>> getExceptionMappers() {
        
        Set<Class<? extends ExceptionMapper<?>>> mappers = new HashSet<Class<? extends ExceptionMapper<?>>>();
        
        //
        // Rather than adding those manually we scan all classes for the provider annotation
        // http://stackoverflow.com/questions/259140/scanning-java-annotations-at-runtime
        //
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        
        scanner.addIncludeFilter(new AnnotationTypeFilter(javax.ws.rs.ext.Provider.class));
        scanner.addIncludeFilter(new AssignableTypeFilter(ExceptionMapper.class));
        
        for (BeanDefinition bd : scanner.findCandidateComponents(JodaEngine.BASE_PACKAGE)) {
            try {
                mappers.add((Class<ExceptionMapper<?>>) Class.forName(bd.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                logger.warn("An ExceptionMapper could not be loaded from the class path.", e);
            }
        }
        
        return mappers;
    }
}
