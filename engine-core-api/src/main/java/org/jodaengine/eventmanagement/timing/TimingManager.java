package org.jodaengine.eventmanagement.timing;

import javax.annotation.Nonnull;
import javax.xml.stream.events.StartDocument;

import org.jodaengine.eventmanagement.adapter.incoming.IncomingPullAdapter;
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
    void registerJobForIncomingPullAdapter(@Nonnull IncomingPullAdapter inboundPulladapter)
    throws AdapterSchedulingException;

    /**
 * Unregister the given job.
 *
 * @param inboundPulladapter the inbound pulladapter
 * @throws AdapterSchedulingException the adapter scheduling exception
 */
    void unregisterJobForIncomingPullAdapter(@Nonnull IncomingPullAdapter inboundPulladapter)
    throws AdapterSchedulingException;
    
    void start();
    
    void stop();
}
