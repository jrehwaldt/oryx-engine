package de.hpi.oryxengine;

import java.util.Iterator;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.hpi.oryxengine.correlation.CorrelationManagerImpl;
import de.hpi.oryxengine.navigator.Navigator;

/**
 * The {@link OryxEngine} class is responsible for the initialization of the whole applicaiton. Therefore we use the
 * "Inversion of Control" pattern provided by the Spring.net Framework.
 */
public class OryxEngine {

    public static final String DEFAULT_SPRING_CONFIG_FILE = "oryxengine.cfg.xml";

    public static void start() {

        startWithConfig(DEFAULT_SPRING_CONFIG_FILE);
    }

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

    private static void initializeApplicationContext(String configurationFile) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configurationFile);
    }

    public static void shutdown() {

    }
}
