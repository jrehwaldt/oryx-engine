package org.jodaengine.navigator;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.subscription.ProcessStartEvent;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.token.Token;


/**
 * The {@link Navigator} is the core routing component, which 'navigates' through the processes. 
 */
public interface Navigator {

    /**
     * Start a new process instance.
     * 
     * @param processID
     *            the id of the process that is to be instantiated
     * @param event
     *            the event that triggered the start
     * @throws DefinitionNotFoundException
     *             thrown if process with given id does not exist in repo
     * @return the started instance
     */
    @Nonnull
    AbstractProcessInstance startProcessInstance(@Nonnull UUID processID, ProcessStartEvent event)
    throws DefinitionNotFoundException;

    /**
     * Start the process instance.
     * 
     * @param processID
     *            the process id
     * @throws DefinitionNotFoundException
     *             the process definition was not found
     * @return the started instance
     */
    @Nonnull
    AbstractProcessInstance startProcessInstance(@Nonnull UUID processID)
    throws DefinitionNotFoundException;

    /**
     * Add another thread that works on the to-be-executed instances.
     */
    void addThread();

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
    List<AbstractProcessInstance> getRunningInstances();

    /**
     * Gets the instances that were processed by this navigator and have ended.
     * 
     * @return the ended instances
     */
    List<AbstractProcessInstance> getEndedInstances();

    /**
     * Cancel the given process instance. Stops all corresponding tokens as soon as possible and does some cleanup.
     *
     * @param instance the instance
     */
    void cancelProcessInstance(AbstractProcessInstance instance);
    
    /**
     * Signal that a formerly running process instance has ended.
     * 
     * @param instance
     *            the instance
     */
    void signalEndedProcessInstance(AbstractProcessInstance instance);

    /**
     * Checks if the navigator is idle, so to say if he still has work to do.
     * 
     * @return true, if the navigator is idle
     */
    boolean isIdle();

    /**
     * Delivers an overview of some statistics like running and finished instances and more.
     * 
     * @return the statistics
     */
    NavigatorStatistic getStatistics();
}
