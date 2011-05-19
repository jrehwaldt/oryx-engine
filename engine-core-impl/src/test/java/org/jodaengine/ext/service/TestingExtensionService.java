package org.jodaengine.ext.service;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.bootstrap.Service;
import org.jodaengine.ext.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An empty extension {@link Service} for testing purpose.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-19
 */
@Extension(TestingExtensionService.DEMO_EXTENSION_SERVICE_NAME)
public class TestingExtensionService implements Service {
    
    public final static String DEMO_EXTENSION_SERVICE_NAME = "testing";
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private boolean started = false;
    private boolean stopped = false;
    private JodaEngineServices services; 
    
    @Override
    public void start(JodaEngineServices services) {
        logger.info("Start Testing Service");
        this.services = services;
        this.started = true;
    }

    @Override
    public void stop() {
        logger.info("Stop Testing Service");
        this.services = null;
        this.stopped = true;
    }
    
    @Override
    public boolean isRunning() {
        return this.started && !this.stopped;
    }
    
    /**
     * Return is started.
     * 
     * @return started?
     */
    public boolean isStarted() {
        return started;
    }
    
    /**
     * Return is stopped.
     * 
     * @return stopped?
     */
    public boolean isStopped() {
        return stopped;
    }

    /**
     * Return the initialization provided services.
     * 
     * @return provided services
     */
    public JodaEngineServices getServices() {
    
        return services;
    }
    
}
