package org.jodaengine.ext.service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

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
 * This {@link Service} provides an interface for managing {@link Extension}s.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-19
 */
public class ExtensionServiceImpl implements ExtensionService {
    
    public static final String BASE_PACKAGE = "org.jodaengine";
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private JodaEngineServices coreServices;
    private Map<String, Service> extensionServices;
    
    private Map<Class<?>, ExtensionList<?>> extensions;
    
    private boolean running = false;
    
    @Override
    public synchronized void start(JodaEngineServices services) {
        
        //
        // skip method if the service is already running
        //
        if (this.running) {
            return;
        }
        
        logger.info("Starting the ExtensionService");
        
        this.coreServices = services;
        this.extensions = new HashMap<Class<?>, ExtensionList<?>>();
        this.extensionServices = new HashMap<String, Service>();
        
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
        
        logger.info("Stopping the ExtensionService");
        
        this.running = false;
        
        //
        // stop all loaded extension services
        //
        for (Service service: this.extensionServices.values()) {
            service.stop();
        }
        
        this.extensions = null;
        this.extensionServices = null;
        this.coreServices = null;
    }
    
    @Override
    public boolean isRunning() {
        return this.running;
    }
    
    @Override
    public <IExtension> boolean isExtensionAvailable(Class<IExtension> extension) {
        
        return this.extensions.containsKey(extension);
    }

    @Override
    public synchronized <IExtension> List<IExtension> getExtensions(Class<IExtension> extension) {
        
        List<IExtension> instances = new ArrayList<IExtension>();
        
        //
        // create an instance of each requested extension
        //
        for (Class<IExtension> ext: getExtensionClasses(extension)) {
            
            //
            // create an instance with the injected parameters
            //
            try {
                instances.add(createExtensionInstance(ext));
                continue;
            } catch (ExtensionNotAvailableException e) {
                logger.error("No such extension available. None of constructors could be instantiated.");
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

    @Override
    public <IExtension> void rebuildExtensionDatabase(Class<IExtension> extension) {
        
        //
        // remove the list of extensions
        // it will automatically rebuild upon call of ExtensionService#getExtensions(...)
        //
        this.extensions.remove(extension);
    }
    
    @Override
    public <IExtension> List<Class<IExtension>> getExtensionTypes(Class<IExtension> extension) {
        List<Class<IExtension>> classes = new ArrayList<Class<IExtension>>();
        classes.addAll(getExtensionClasses(extension).getExtensionTypes());
        return classes;
    }

    //=================================================================
    //=================== Private methods =============================
    //=================================================================
    
    /**
     * Starts all available extension services.
     */
    private void startExtensionServices() {
        
        logger.info("Starting all found extensions services");
        
        ExtensionList<Service> serviceClasses = getExtensionClasses(Service.class);
        
        for (Class<Service> serviceClass: serviceClasses) {
            String name = serviceClass.getAnnotation(Extension.class).value();
            
            if (this.extensionServices.containsKey(name)) {
                logger.error("Multiple extension services with name '{}' found. Services only partially loaded.", name);
                continue;
            }
            
            //
            // create the service and keep a reference
            //
            try {
                Service service = createExtensionInstance(serviceClass);
                this.extensionServices.put(name, service);
                
                logger.info("Successfully registered extension service {} [{}]", name, service);
            } catch (Exception e) {
                logger.error("Could not instantiate extension service " + name + ".", e);
            }
        }
        
        //
        // start the created services
        //
        for (Service service: this.extensionServices.values()) {
            logger.info("Starting registered extension service {}", service);
            service.start(this.coreServices);
        }
    }
    
    /**
     * Provides any {@link Extension} class that is assignable to the desired interface.
     * 
     * @param <IExtension> the extension point's interface
     * @param extension the extension point's interface
     * @return the classes matching the extension point's interface
     */
    @SuppressWarnings("unchecked")
    private @Nonnull <IExtension> ExtensionList<IExtension> getExtensionClasses(@Nonnull Class<IExtension> extension) {
        
        synchronized (extension) {
            //
            // get the cached extension type list
            //
            if (this.extensions.containsKey(extension)) {
                return (ExtensionList<IExtension>) this.extensions.get(extension);
            }
            
            ExtensionList<IExtension> classes = new ExtensionList<IExtension>();
            
            //
            // class path scanner
            //
            ClassPathScanningCandidateComponentProvider scanner
                = new ClassPathScanningCandidateComponentProvider(false);
            
            //
            // our candidate needs to be annotated with @Extension and of the desired type
            //
            TypeFilter filter = new AndTypeFilter(
                new AnnotationTypeFilter(Extension.class),
                new AssignableTypeFilter(extension));
            
            scanner.addIncludeFilter(filter);
            
            //
            // search for the specified beans within base package scope
            //
            for (BeanDefinition bd : scanner.findCandidateComponents(BASE_PACKAGE)) {
                try {
                    Class<IExtension> extensionClass = (Class<IExtension>) Class.forName(bd.getBeanClassName());
                    classes.addExtensionType(extensionClass);
                    logger.info("Found extension type {}", extensionClass);
                } catch (ClassNotFoundException cnfe) {
                    //
                    // this is seriously unlikely to happen... we found it in the class path just a second ago
                    //
                    logger.error("Extension class could not be found", cnfe);
                }
            }
            
            //
            // remember the list of extensions
            //
            this.extensions.put(extension, classes);
            
            return classes;
        }
    }
    
    /**
     * This method constructs an instance of the required {@link Extension}'s interface
     * providing it with any kind of {@link Service} specified in the constructor.
     * 
     * @param <IExtension> the extension's interface
     * @param extension the extension's interface
     * @return an instance of the extension
     * @throws ExtensionNotAvailableException in case the extension could not be instantiated
     */
    @SuppressWarnings("unchecked")
    private @Nonnull <IExtension> IExtension createExtensionInstance(@Nonnull Class<IExtension> extension)
    throws ExtensionNotAvailableException {
        
        //
        // get a public constructor
        //
        Constructor<IExtension>[] constructors = (Constructor<IExtension>[]) extension.getConstructors();
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
                    
                    if (Service.class.isAssignableFrom(parameterClass)) {
                        
                        //
                        // inject core services
                        //
                        for (Service service: this.coreServices.getCoreServices()) {
                            if (parameterClass.isAssignableFrom(service.getClass())) {
                                parameters[i] = service;
                                continue setParameters;
                            }
                        }
                        
                        //
                        // inject available extension services
                        //
                        for (Service service: this.extensionServices.values()) {
                            if (parameterClass.isAssignableFrom(service.getClass())) {
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
                return constructor.newInstance(parameters);
            } catch (Exception e) {
                logger.warn("Extension could not be instantiated.", e);
            }
        }
        
        throw new ExtensionNotAvailableException();
    }
}
