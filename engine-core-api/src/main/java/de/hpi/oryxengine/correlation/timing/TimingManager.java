package de.hpi.oryxengine.correlation.timing;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.adapter.InboundPullAdapter;
import de.hpi.oryxengine.correlation.adapter.TimedConfiguration;
import de.hpi.oryxengine.exception.AdapterSchedulingException;
import de.hpi.oryxengine.process.token.Token;

/**
 * This class is responsible for providing timing support.
 */
public interface TimingManager {

    /**
     * Registers a new pull adapter.
     * 
     * @param adapter the adapter
     * @throws AdapterSchedulingException thrown if scheduling fails
     */
    void registerPullAdapter(@Nonnull InboundPullAdapter adapter)
    throws AdapterSchedulingException;
    
    /**
     * Registers a non recurring job. This can be used for intermediate timers.
     *
     * @param configuration the configuration of the event
     * @param token the process token to continue the process afterwards.
     * @throws AdapterSchedulingException the adapter scheduling exception
     * @return the name of the job
     */
    String registerNonRecurringJob(@Nonnull TimedConfiguration configuration, Token token)
    throws AdapterSchedulingException;
    
    /**
     * Unregister the given job.
     *
     * @param the name of the job, in the form jobName.groupName
     */
    void unregisterJob(String jobCompleteName);
    
    /**
     * Count the scheduled jobGroups.
     *
     * @return the number of scheduled groups
     */
    int countScheduledJobGroups();
}
