package org.jodaengine.eventmanagement.processevent.incoming;

import org.jodaengine.eventmanagement.subscription.IntermediateProcessEvent;


/**
 * This class encapsulate the behavior that should be done in case a {@link IntermediateProcessEvent} is triggered.
 */
public interface TriggeringBehaviour {

    /**
     * If an {@link IntermediateProcessEvent} is triggered than this method is
     * called.
     * 
     * @param processEvent
     *            - the {@link IntermediateProcessEvent} that was triggered
     */
    void trigger(IntermediateProcessEvent processEvent);
}
