package de.hpi.oryxengine;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.hpi.oryxengine.correlation.CorrelationManagerImpl;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.navigator.NavigatorImpl;

/**
 * Bootstrapper for starting the Dalmatina Engine.
 * 
 * The Bootstrapper reads a configuration file and enables the dependency injection.
 * 
 */
public class OryxEngine {

    // Dependency injection??
    
    public void start() {
        
        ApplicationContext ctx = new ClassPathXmlApplicationContext("oryxengine.xml");
        
        ctx.getBean("");
        
//        appCon.
        
//        Navigator navigator = new NavigatorImpl();
//        navigator.start();
//        
//        CorrelationManagerImpl correlationManager = new CorrelationManagerImpl(navigator);
//        correlationManager.start();
    }
    
    public void shutdown() {
        
    }
}
