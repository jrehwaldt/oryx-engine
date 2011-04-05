package de.hpi.oryxengine;

import java.util.Iterator;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The {@link OryxEngine} class is responsible for the initialization of the whole applicaiton. Therefore we use the
 * "Inversion of Control" pattern provided by the Spring.net Framework.
 */
public final class OryxEngine {

    public static final String DEFAULT_SPRING_CONFIG_FILE = "oryxengine.cfg.xml";

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
        Map<String, Service> serviceTable = OryxEngineAppContext.getAppContext().getBeansOfType(Service.class);

        if (serviceTable != null) {

            Iterator<Service> serviceIterator = serviceTable.values().iterator();
            while (serviceIterator.hasNext()) {

                Service service = serviceIterator.next();
                service.start();
            }
        }
    }

    /**
     * Initializes the {@link ApplicationContext} of the {@link OryxEngine}.
     * 
     * @param configurationFile
     *            - file where the dependencies are defined
     */
    private static void initializeApplicationContext(String configurationFile) {

        new ClassPathXmlApplicationContext(configurationFile);
    }

    /**
     * Stops the {@link OryxEngine}.
     */
    public static void shutdown() {

    }

    /**
     * Hidden Constructor.
     */
    private OryxEngine() { }
}
