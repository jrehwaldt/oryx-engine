package org.jodaengine.eventmanagement.processevent.incoming;

import org.jodaengine.eventmanagement.subscription.IncomingProcessEvent;


/**
 * This class encapsulate the behavior that should be done in case a {@link IncomingProcessEvent} is triggered.
 */
public interface TriggeringBehavior {

    /**
     * If an {@link IncomingProcessEvent} is triggered than this method is
     * called.
     * 
     * @param processEvent
     *            - the {@link IncomingProcessEvent} that was triggered
     */
    void trigger(IncomingProcessEvent processEvent);
}
