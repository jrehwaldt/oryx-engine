package org.jodaengine.rest.application;

import org.jodaengine.rest.api.DemoWebService;

import de.hpi.oryxengine.rest.application.JodaEngineApplication;

/**
 * The Class DemoEngineApplication extends the JodaEngineApplication, as it adds the DemoWebService to the supplied
 * resources.
 */
public class DemoEngineApplication extends JodaEngineApplication {
    
    public DemoEngineApplication() {
        super();
        singletons.add(new DemoWebService(engineServices));
    }
}