package org.jodaengine.eventmanagement.timing;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.adapter.InboundPullAdapter;
import org.jodaengine.exception.AdapterSchedulingException;

/**
 * This class is responsible for providing timing support.
 */
public interface TimingManager {

    /**
     * Registers a new pull adapter.
     * 
     * @param inboundPulladapter
     *            the adapter
     * @throws AdapterSchedulingException
     *             thrown if scheduling fails
     */
    void registerJobForInboundPullAdapter(@Nonnull InboundPullAdapter inboundPulladapter)
    throws AdapterSchedulingException;

//    /**
//     * Registers a non recurring job. This can be used for intermediate timers.
//     * 
//     * @param configuration
//     *            the configuration of the event
//     * @param token
//     *            the process token to continue the process afterwards.
//     * @throws AdapterSchedulingException
//     *             the adapter scheduling exception
//     * @return the name of the job
//     */
//    String registerNonRecurringJob(@Nonnull TimerConfiguration configuration, Token token)
//    throws AdapterSchedulingException;

    /**
     * Unregister the given job.
     * 
     */
    void unregisterJobForInboundPullAdapter(@Nonnull InboundPullAdapter inboundPulladapter)
    throws AdapterSchedulingException;
}
