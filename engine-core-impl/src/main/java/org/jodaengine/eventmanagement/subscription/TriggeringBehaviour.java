package org.jodaengine.eventmanagement.subscription;


/**
 * This class encapsulate the behavior that should be done in case a {@link ProcessEvent} is triggered.
 */
public interface TriggeringBehaviour {

    /**
     * If an {@link ProcessEvent} is triggered than this method is
     * called.
     * 
     * @param processEvent
     *            - the {@link ProcessEvent} that was triggered
     */
    void trigger(ProcessEvent processEvent);
}
