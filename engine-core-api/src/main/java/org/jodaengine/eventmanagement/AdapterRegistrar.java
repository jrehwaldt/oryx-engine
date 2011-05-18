package org.jodaengine.eventmanagement;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.adapter.InboundAdapter;
import org.jodaengine.eventmanagement.adapter.InboundPullAdapter;
import org.jodaengine.eventmanagement.timing.TimingManager;
import org.jodaengine.exception.AdapterSchedulingException;

/**
 * This interface provides methods for registering events to the {@link EventManager}.
 */
public interface AdapterRegistrar {

    /**
     * A call to this method registers the corresponding adapter.
     * 
     * @param adapter
     *            the {@link InboundAdapter} to register
     * @param <Adapter>
     *            the adapter's type, generally {@link InboundAdapter} or {@link InboundPullAdapter}
     * @return the registered {@link InboundAdapter}
     */
    @Nonnull
    <Adapter extends InboundAdapter> Adapter registerAdapter(@Nonnull Adapter adapter);

    /**
     * A call to this method registers the corresponding adapter.
     * 
     * @param adapter
     *            the {@link InboundAdapter} to register
     * @return the registered {@link InboundAdapter}
     * @exception AdapterSchedulingException
     *                thrown if scheduling the adapter failed
     */
    @Nonnull
    InboundPullAdapter registerPullAdapter(@Nonnull InboundPullAdapter adapter)
    throws AdapterSchedulingException;
    
    /**
     * Gets the timer.
     * 
     * @return the timer
     */
    TimingManager getTimer();
}
