package de.hpi.oryxengine.navigator;

import java.util.UUID;

import javax.annotation.Nonnull;

/**
 * The Interface NavigatorInterface.
 */
public interface Navigator {

    /**
     * Start a new process instance.
     * 
     * @param processID
     *            the id of the process that is to be instantiated
     * @return the id of the newly created process instance
     */
    @Nonnull UUID startProcessInstance(@Nonnull UUID processID);

    /**
     * Stop process instance.
     * 
     * @param instanceID
     *            the id of the instance that is to be stopped
     */
    void stopProcessInstance(@Nonnull UUID instanceID);
    
    /**
     * Increase speed.
     */
    void increaseSpeed();

    /**
     * Gets the current instance state.
     *
     * @param instanceID the processinstance id
     * @return the current instance state
     */
    String getCurrentInstanceState(UUID instanceID);
    
    /**
     * Starts the navigator, which is than ready to schedule processes.
     */
    void start();
    
    /**
     * Stops the navigator. No processes will be scheduled afterwards.
     */
    void stop();
}
