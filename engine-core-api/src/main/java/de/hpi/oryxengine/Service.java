package de.hpi.oryxengine;

/**
 * This interface represents a service of the OryxEngine. Each service can be started and stopped. 
 */
public interface Service {

    /**
     * Starts the service.
     */
    void start();

    /**
     * Stops the service.
     */
    void stop();
}
