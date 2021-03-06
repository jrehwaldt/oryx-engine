package org.jodaengine.eventmanagement;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.adapter.EventAdapter;
import org.jodaengine.eventmanagement.adapter.incoming.IncomingAdapter;
import org.jodaengine.eventmanagement.adapter.incoming.IncomingPullAdapter;
import org.jodaengine.eventmanagement.timing.TimingManager;


/**
 * This interface provides methods for registering events to the {@link EventService}.
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
     *            the {@link IncomingAdapter} to register
     * @return the registered {@link IncomingAdapter}
     */
    @Nonnull
    IncomingAdapter registerIncomingAdapter(@Nonnull IncomingAdapter adapter);

    /**
     * A call to this method registers the corresponding adapter.
     * 
     * @param adapter
     *            the {@link IncomingPullAdapter} to register
     * @return the registered {@link IncomingPullAdapter}
     */
    @Nonnull
    IncomingPullAdapter registerIncomingPullAdapter(@Nonnull IncomingPullAdapter adapter);

    /**
     * Gets the timer.
     * 
     * @return the timer
     */
    TimingManager getTimer();

}
