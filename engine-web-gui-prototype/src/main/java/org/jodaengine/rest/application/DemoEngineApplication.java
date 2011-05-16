package org.jodaengine.rest.application;

import org.jodaengine.rest.api.DemoWebService;

/**
 * The Class DemoEngineApplication extends the JodaEngineApplication, as it adds the DemoWebService to the supplied
 * resources.
 */
public class DemoEngineApplication extends JodaEngineApplication {
    
    /**
     * Demo engine constructor. This creates an application with our demo task as addition.
     */
    public DemoEngineApplication() {

        super();
        singletons.add(new DemoWebService(engineServices));
    }
}
