package org.jodaengine.eventmanagement;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.adapter.EventAdapter;
import org.jodaengine.eventmanagement.adapter.InboundAdapter;
import org.jodaengine.eventmanagement.adapter.InboundPullAdapter;
import org.jodaengine.eventmanagement.subscription.ProcessEvent;
import org.jodaengine.eventmanagement.timing.TimingManager;


/**
 * This interface provides methods for registering events to the {@link EventManager}.
 */
public interface AdapterManagement {

    /**
     * A call to this method registers the corresponding adapter.
     * 
     * @param adapter
     *            the {@link EventAdapter} to register
     * @return the registered {@link EventAdapter}
     */
    @Nonnull
    EventAdapter registerAdapter(@Nonnull EventAdapter adapter);
    
    /**
     * A call to this method registers the corresponding adapter.
     * 
     * @param adapter
     *            the {@link InboundAdapter} to register
     * @return the registered {@link InboundAdapter}
     */
    @Nonnull
    InboundAdapter registerInboundAdapter(@Nonnull InboundAdapter adapter);

    /**
     * A call to this method registers the corresponding adapter.
     * 
     * @param adapter
     *            the {@link InboundPullAdapter} to register
     * @return the registered {@link InboundPullAdapter}
     */
    @Nonnull
    InboundPullAdapter registerInboundPullAdapter(@Nonnull InboundPullAdapter adapter);

    /**
     * Gets the timer.
     * 
     * @return the timer
     */
    TimingManager getTimer();

    /**
     * Provides information about the assigned {@link EventCorrelator}.
     * 
     * @return a {@link EventCorrelator} that is able to correlate {@link ProcessEvent}s with {@link AdapterEvent}s
     */
    EventCorrelator getEventCorrelator();
}
