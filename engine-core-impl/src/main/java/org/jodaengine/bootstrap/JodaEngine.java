package org.jodaengine.bootstrap;

import java.util.Iterator;
import java.util.Map;

import org.jodaengine.IdentityService;
import org.jodaengine.JodaEngineServices;
import org.jodaengine.RepositoryService;
import org.jodaengine.ServiceFactory;
import org.jodaengine.WorklistService;
import org.jodaengine.navigator.Navigator;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The {@link JodaEngine} class is responsible for the initialization of the whole application. Therefore we use the
 * "Inversion of Control" pattern provided by the Spring.net Framework.
 */
public class JodaEngine implements JodaEngineServices {

    public static final String DEFAULT_SPRING_CONFIG_FILE = "jodaengine.cfg.xml";

    protected static JodaEngine jodaEngineSingelton;

    /**
     * Starts the engine using the default dependency injection file (jodaengine.cfg.xml). In case the
     * {@link JodaEngine} already has started, the old {@link JodaEngine} is returned.
     * 
     * @return the {@link JodaEngine}; in case the {@link JodaEngine} already has
     *         started, the old {@link JodaEngine} is returned
     */
    public static JodaEngine start() {

        return startWithConfig(DEFAULT_SPRING_CONFIG_FILE);
    }

    /**
     * Starts the engine using a specific dependency injection file.
     * 
     * @param configurationFile
     *            - file where the dependencies are defined
     * @return the {@link JodaEngine} singleton
     */
    public static JodaEngine startWithConfig(String configurationFile) {

        if (jodaEngineSingelton != null) {
            return jodaEngineSingelton;
        }

        // Initialize ApplicationContext
        initializeApplicationContext(configurationFile);

        // Extracting all Service Beans
        Map<String, Service> serviceTable = JodaEngineAppContext.getAppContext().getBeansOfType(Service.class);

        if (serviceTable != null) {

            Iterator<Service> serviceIterator = serviceTable.values().iterator();
            while (serviceIterator.hasNext()) {

                Service service = serviceIterator.next();
                service.start();
            }
        }

        jodaEngineSingelton = new JodaEngine();
        return jodaEngineSingelton;
    }

    /**
     * Initializes the {@link ApplicationContext} of the {@link JodaEngine}.
     * 
     * @param configurationFile
     *            - file where the dependencies are defined
     */
    private static void initializeApplicationContext(String configurationFile) {

        new ClassPathXmlApplicationContext(configurationFile);
    }

    /**
     * Stops the {@link JodaEngine}.
     */
    @Override
    public void shutdown() {

        // Extracting all Service Beans
        Map<String, Service> serviceTable = JodaEngineAppContext.getAppContext().getBeansOfType(Service.class);

        if (serviceTable != null) {

            Iterator<Service> serviceIterator = serviceTable.values().iterator();
            while (serviceIterator.hasNext()) {

                Service service = serviceIterator.next();
                service.stop();
            }
        }
        
        jodaEngineSingelton = null;
    }

    @Override
    public WorklistService getWorklistService() {

        return ServiceFactory.getWorklistService();
    }

    @Override
    public IdentityService getIdentityService() {

        return ServiceFactory.getIdentityService();
    }

    @Override
    public Navigator getNavigatorService() {

        return ServiceFactory.getNavigatorService();
    }

    @Override
    public RepositoryService getRepositoryService() {

        return ServiceFactory.getRepositoryService();
    }
}
