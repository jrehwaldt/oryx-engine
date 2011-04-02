package de.hpi.oryxengine.navigator;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Interface NavigatorInterface.
 */
public interface Navigator {

    /**
     * Start a new process instance.
     *
     * @param processID the id of the process that is to be instantiated
     * @param event the event that triggered the start
     * @throws DefinitionNotFoundException thrown if process with given id does not exist in repo
     */
    // TODO This should return the id of the created process instance. Unfortunately, we do not have a process instance
    // class yet, so we cannot do this.
    @Nonnull
    void startProcessInstance(@Nonnull UUID processID, StartEvent event)
    throws DefinitionNotFoundException;

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
     * @param t
     *            the t
     */
    void addWorkToken(Token t);

    /**
     * Adds a token that is in suspended state.
     * 
     * @param t
     *            the t
     */
    void addSuspendToken(Token t);

    /**
     * Removes the suspend token.
     * 
     * @param t
     *            the t
     */
    void removeSuspendToken(Token t);

    /**
     * Gets all the instances that were ever started/executed by this navigator.
     * 
     * @return the instances
     */
    List<ProcessInstance> getRunningInstances();
    
    /**
     * Gets the instances that were processed by this navigator and have ended.
     *
     * @return the ended instances
     */
    List<ProcessInstance> getEndedInstances();
    
    /**
     * Signal that a formerly running process instance has ended.
     *
     * @param instance the instance
     */
    void signalEndedProcessInstance(ProcessInstance instance);
}
