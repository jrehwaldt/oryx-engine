package org.jodaengine.eventmanagement;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.adapter.InboundAdapter;
import org.jodaengine.eventmanagement.adapter.InboundPullAdapter;
import org.jodaengine.eventmanagement.timing.TimingManager;

/**
 * This interface provides methods for registering events to the {@link EventManager}.
 */
public interface AdapterRegistrar {

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
}
