package org.jodaengine.ext.service;

import javax.ws.rs.Path;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.bootstrap.Service;

/**
 * This {@link TestingWebService} is the counterpart to {@link TestingWebExtensionService}
 * and test the proper creation of specified web services.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-20
 */
@Path("/" + TestingWebExtensionService.DEMO_EXTENSION_SERVICE_NAME)
public class TestingWebService implements Service {
    
    private JodaEngineServices services;
    private TestingWebExtensionService testing;
    
    /**
     * Constrcuts a {@link TestingWebService}.
     * 
     * @param services the joda services
     * @param testing the parent testing service
     */
    public TestingWebService(JodaEngineServices services,
                      TestingWebExtensionService testing) {
        this.services = services;
        this.testing = testing;
    }
    
    @Override
    public void start(JodaEngineServices services) {
        this.testing.start(services);
    }
    
    @Override
    public void stop() {
        this.testing.stop();
    }
    
    @Override
    public boolean isRunning() {
        return  this.testing.isRunning();
    }
    
    /**
     * Returns the joda services.
     * 
     * @return the joda services
     */
    JodaEngineServices getServices() {
        return services;
    }
    
    /**
     * Returns the {@link TestingWebExtensionService}.
     * 
     * @return the testing service
     */
    TestingWebExtensionService getTesting() {
        return testing;
    }
}
