package org.jodaengine.eventmanagement.subscription;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.processevent.incoming.StartProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IncomingIntermediateProcessEvent;

/**
 * This interface provides methods in order to unsubscribe from events.
 */
public interface EventUnsubscription {

    /**
     * Entry point for unsubscribing from an event.
     * 
     * @param startEvent
     *            - the {@link StartProcessEvent startEvent} that is not needed anymore
     */
    void unsubscribeFromStartEvent(@Nonnull StartProcessEvent startEvent);

    //
    /**
     * Entry point for unsubscribing from an event.
     * 
     * @param intermediateEvent
     *            - the {@link IncomingIntermediateProcessEvent intermediateEvent} that is not needed anymore
     */
    void unsubscribeFromIncomingIntermediateEvent(@Nonnull IncomingIntermediateProcessEvent intermediateEvent);
}
