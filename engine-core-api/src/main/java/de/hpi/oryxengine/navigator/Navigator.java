package de.hpi.oryxengine.navigator;

import java.util.UUID;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.process.token.Token;

// TODO: Auto-generated Javadoc
/**
 * The Interface NavigatorInterface.
 */
public interface Navigator {

    /**
     * Start a new process instance.
     * 
     * @param processID
     *            the id of the process that is to be instantiated
     * @throws Exception thrown if process with given id does not exist in repo
     */
    // TODO This should return the id of the created process instance. Unfortunately, we do not have a process instance
    // class yet, so we cannot do this.
    @Nonnull
    void startProcessInstance(@Nonnull UUID processID) throws Exception;

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
    void addThread();

    /**
     * Gets the current instance state.
     * 
     * @param instanceID
     *            the processinstance id
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
    
    
    /**
     * Adds a token to that is to be worked on.
     *
     * @param t the t
     */
    void addWorkToken(Token t);
    
    /**
     * Adds a token that is in suspended state.
     *
     * @param t the t
     */
    void addSuspendToken(Token t);
}
