package org.jodaengine.eventmanagement;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.adapter.CorrelationAdapter;
import org.jodaengine.eventmanagement.adapter.InboundAdapter;
import org.jodaengine.eventmanagement.adapter.InboundPullAdapter;
import org.jodaengine.eventmanagement.registration.ProcessEvent;
import org.jodaengine.eventmanagement.timing.TimingManager;

/**
 * This interface provides methods for registering events to the {@link EventManager}.
 */
public interface AdapterRegistrar {

    /**
     * A call to this method registers the corresponding adapter.
     * 
     * @param adapter
     *            the {@link CorrelationAdapter} to register
     * @return the registered {@link CorrelationAdapter}
     */
    @Nonnull
    CorrelationAdapter registerAdapter(@Nonnull CorrelationAdapter adapter);
    
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
     *            the {@link InboundAdapter} to register
     * @return the registered {@link InboundAdapter}
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
     * Provides information about the assigned {@link CorrelationManager}.
     * 
     * @return a {@link CorrelationManager} that is able to correlate {@link ProcessEvent}s with {@link AdapterEvent}s
     */
    CorrelationManager getEventCorrelation();
}
