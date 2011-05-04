package de.hpi.oryxengine.bootstrap;

import java.util.Iterator;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.hpi.oryxengine.Service;

/**
 * The {@link JodaEngine} class is responsible for the initialization of the whole applicaiton. Therefore we use the
 * "Inversion of Control" pattern provided by the Spring.net Framework.
 */
public final class JodaEngine {

    public static final String DEFAULT_SPRING_CONFIG_FILE = "jodaengine.cfg.xml";

    /**
     * Starts the engine using the default dependency injection file (oryxengine.cfg.xml).
     */
    public static void start() {

        startWithConfig(DEFAULT_SPRING_CONFIG_FILE);
    }

    /**
     * Starts the engine using a specific dependency injection file.
     * 
     * @param configurationFile
     *            - file where the dependencies are defined
     */
    public static void startWithConfig(String configurationFile) {

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
    public static void shutdown() {

     // Extracting all Service Beans
        Map<String, Service> serviceTable = JodaEngineAppContext.getAppContext().getBeansOfType(Service.class);

        if (serviceTable != null) {

            Iterator<Service> serviceIterator = serviceTable.values().iterator();
            while (serviceIterator.hasNext()) {

                Service service = serviceIterator.next();
                service.stop();
            }
        }
    }

    /**
     * Hidden Constructor.
     */
    private JodaEngine() { }
}
