package org.jodaengine.ext.service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.bootstrap.Service;
import org.jodaengine.ext.Extension;
import org.jodaengine.util.AndTypeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

/**
 * This service provides an interface for managing and deploying extensions.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-19
 */
public class ExtensionServiceImpl implements ExtensionService {
    
    public static final String BASE_PACKAGE = "org.jodaengine";
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private JodaEngineServices coreServices;
    private List<Class<?>> extensions;
    
    private Map<String, Service> extensionServices;
    
    private boolean running = false;
    
    @Override
    public synchronized void start(JodaEngineServices services) {
        
        //
        // skip method if the service is already running
        //
        if (this.running) {
            return;
        }
        
        this.coreServices = services;
        this.extensions = new ArrayList<Class<?>>();
        this.extensionServices = new HashMap<String, Service>();
        
        this.extensionServices.put("core-navigator", this.coreServices.getNavigatorService());
        this.extensionServices.put("core-repository", this.coreServices.getRepositoryService());
        this.extensionServices.put("core-worklist", this.coreServices.getWorklistService());
        this.extensionServices.put("core-identity", this.coreServices.getIdentityService());
        
        //
        // do not add this service, because it will cause infinite recursion on stop()
        //
//        this.extensionServices.put("core-extension", this.coreServices.getExtensionService());
        
        startExtensionServices();
        
        this.running = true;
    }

    @Override
    public synchronized void stop() {
        
        //
        // skip method if the service is already stopped
        //
        if (!this.running) {
            return;
        }
        
        this.running = false;
        
        //
        // stop all loaded services
        //
        for (Service service: this.extensionServices.values()) {
            service.stop();
        }
        
        this.extensions = null;
        this.extensionServices = null;
        this.coreServices = null;
    }
    
    @Override
    public <IExtension> boolean isExtensionAvailable(Class<IExtension> extension) {
        
        return this.extensions.contains(extension);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <IExtension> List<IExtension> getExtensions(Class<IExtension> extension) {
        
        List<IExtension> instances = new ArrayList<IExtension>();
        
        //
        // create an instance of each requested extension
        //
        createInstance : for (Class<IExtension> ext: getExtensionClasses(extension)) {
            
            //
            // get a public constructor
            //
            Constructor<IExtension>[] constructors = (Constructor<IExtension>[]) ext.getConstructors();
            for (Constructor<IExtension> constructor: constructors) {
                Type[] parameterTypes = constructor.getGenericParameterTypes();
                Object[] parameters = new Object[parameterTypes.length];
                
                //
                // set parameter for certain services, if defined in constructor
                //
                setParameters : for (int i = 0; i < parameterTypes.length; i++) {
                    Type paramaterType = parameterTypes[i];
                    
                    if (paramaterType instanceof Class<?>) {
                        Class<?> parameterClass = (Class<?>) paramaterType;
                        
                        //
                        // inject the JodaEngineServices
                        //
                        if (JodaEngineServices.class.isAssignableFrom(parameterClass)) {
                            parameters[i] = this.coreServices;
                            continue;
                        }
                        
                        //
                        // inject the ExtensionService
                        //
                        if (ExtensionService.class.isAssignableFrom(parameterClass)) {
                            parameters[i] = this;
                            continue;
                        }
                        
                        //
                        // inject other available services
                        //
                        if (Service.class.isAssignableFrom(parameterClass)) {
                            
                            for (Service service: this.extensionServices.values()) {
                                if (service.getClass().isAssignableFrom(parameterClass)) {
                                    parameters[i] = service;
                                    continue setParameters;
                                }
                            }
                        }
                        
                        //
                        // unknown parameters are provided as null
                        //
                        parameters[i] = null;
                    }
                }
                
                //
                // create an instance with the injected parameters
                //
                try {
                    instances.add(constructor.newInstance(parameters));
                    continue createInstance;
                } catch (Exception e) {
                    logger.error("Extension could not be instantiated.", e);
                }
            }
        }
        
        return instances;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <IExtensionService extends Service> IExtensionService getExtensionService(Class<IExtensionService> extension,
                                                                                     String name)
    throws ExtensionNotAvailableException {
        
        IExtensionService service = (IExtensionService) this.extensionServices.get(name);
        
        if (service == null) {
            throw new ExtensionNotAvailableException();
        }
        
        return service;
    }

    //=================================================================
    //=================== Private methods =============================
    //=================================================================
    
    /**
     * Starts all available extension services.
     */
    private void startExtensionServices() {
        List<Class<Service>> serviceClasses = getExtensionClasses(Service.class);
        
        for (Class<Service> serviceClass: serviceClasses) {
            String name = serviceClass.getAnnotation(Extension.class).value();
            
            if (this.extensionServices.containsKey(name)) {
                logger.error("Multiple extension services with name '{}' found. Services only partially loaded.", name);
                continue;
            }
            
            try {
                //
                // start the service and keep a reference
                //
                try {
                    Constructor<Service> serviceConstructor = serviceClass.getConstructor(JodaEngineServices.class);
                    Service serviceInstance = serviceConstructor.newInstance(this.coreServices);
                    this.extensionServices.put(name, serviceInstance);
                    continue;
                } catch (NoSuchMethodException nsme) {
                    //
                    // No public constructor with JodaEngineServices as parameter found.
                    // Assume empty public constructor.
                    //
                    Service serviceInstance = serviceClass.newInstance();
                    this.extensionServices.put(name, serviceInstance);
                }
                
            } catch (Exception e) {
                logger.error("Could not instantiate extension service.", e);
            }
        }
        
        //
        // start the created services
        //
        for (Service service: this.extensionServices.values()) {
            service.start(this.coreServices);
        }
    }
    
    /**
     * Provides any extension class that is assignable to the desired interface.
     * 
     * @param <IExtension> the extension point's interface
     * @param extension the extension point's interface
     * @return the classes matching the extension point's interface
     */
    @SuppressWarnings("unchecked")
    private <IExtension> List<Class<IExtension>> getExtensionClasses(Class<IExtension> extension) {
        
        List<Class<IExtension>> classes = new ArrayList<Class<IExtension>>();
        
        //
        // find class candidates
        //
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        
        //
        // our candidate needs to be annotated with @Extension and of the desired type
        //
        TypeFilter filter = new AndTypeFilter(
            new AnnotationTypeFilter(Extension.class),
            new AssignableTypeFilter(extension));
        
        scanner.addIncludeFilter(filter);
        
        for (BeanDefinition bd : scanner.findCandidateComponents(BASE_PACKAGE)) {
            try {
                classes.add((Class<IExtension>) Class.forName(bd.getBeanClassName()));
            } catch (ClassNotFoundException cnfe) {
                logger.error("Extension class could not be found", cnfe);
            }
        }
        
        return classes;
    }
}
