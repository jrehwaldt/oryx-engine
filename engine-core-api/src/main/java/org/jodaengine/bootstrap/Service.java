package org.jodaengine.bootstrap;

import javax.annotation.Nonnull;

import org.jodaengine.JodaEngineServices;

/**
 * This interface represents a service of the JodaEngine. Each service can be started and stopped.
 */
public interface Service {

    /**
     * Starts the service.
     * 
     * Not all {@link Service}s hold by {@link JodaEngineServices} may already be started when this method is called.
     * 
     * @param services the service bootstrap
     */
    void start(@Nonnull JodaEngineServices services);

    /**
     * Stops the service.
     */
    void stop();
    
    /**
     * Returns whether this service is running.
     * 
     * @return running?
     */
    boolean isRunning();
}
