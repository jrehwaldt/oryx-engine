package de.hpi.oryxengine.rest.application;

import de.hpi.oryxengine.rest.api.DemoWebService;

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
