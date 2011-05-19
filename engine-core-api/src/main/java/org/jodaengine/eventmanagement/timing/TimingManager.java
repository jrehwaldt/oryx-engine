package org.jodaengine.eventmanagement.timing;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.adapter.InboundPullAdapter;
import org.jodaengine.eventmanagement.adapter.TimerConfiguration;
import org.jodaengine.exception.AdapterSchedulingException;
import org.jodaengine.process.token.Token;

/**
 * This class is responsible for providing timing support.
 */
public interface TimingManager {

    /**
     * Registers a new pull adapter.
     * 
     * @param adapter
     *            the adapter
     * @throws AdapterSchedulingException
     *             thrown if scheduling fails
     */
    void registerJobForInboundPullAdapter(@Nonnull InboundPullAdapter adapter)
    throws AdapterSchedulingException;

    /**
     * Registers a non recurring job. This can be used for intermediate timers.
     * 
     * @param configuration
     *            the configuration of the event
     * @param token
     *            the process token to continue the process afterwards.
     * @throws AdapterSchedulingException
     *             the adapter scheduling exception
     * @return the name of the job
     */
    String registerNonRecurringJob(@Nonnull TimerConfiguration configuration, Token token)
    throws AdapterSchedulingException;

    /**
     * Unregister the given job.
     * 
     * @param jobCompleteName
     *            the name of the job from the scheduler
     */
    void unregisterJob(String jobCompleteName);

    /**
     * Count the scheduled jobGroups.
     * 
     * @return the number of scheduled groups
     */
    int countScheduledJobGroups();
}
