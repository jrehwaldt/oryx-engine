package org.jodaengine.eventmanagement.subscription;

import javax.annotation.Nonnull;

/**
 * This interface provides methods in order to unsubscribe from events.
 */
public interface EventUnsubscription {

    /**
     * Entry point for unsubscribing from an event.
     * 
     * @param startEvent
     *            - the {@link ProcessStartEvent startEvent} that is not needed anymore
     */
    void unsubscribeFromStartEvent(@Nonnull ProcessStartEvent startEvent);

    //
    /**
     * Entry point for unsubscribing from an event.
     * 
     * @param intermediateEvent
     *            - the {@link ProcessIntermediateEvent intermediateEvent} that is not needed anymore
     */
    void unsubscribeFromIntermediateEvent(@Nonnull ProcessIntermediateEvent intermediateEvent);
}
