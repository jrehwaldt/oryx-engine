package org.jodaengine.eventmanagement.subscription;

import org.jodaengine.eventmanagement.processevent.ProcessEvent;
import org.jodaengine.eventmanagement.subscription.condition.AdapterEventComparable;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;

/**
 * The Interface ProcessEvent. All process events have an assigned adapter with a given configuration that may produce
 * incoming events that correlate against the process event.
 * 
 * Could extend Attributable!!!
 */
public interface IntermediateProcessEvent extends ProcessEvent, AdapterEventComparable {

    /**
     * Gets the conditions.
     * 
     * @return the conditions
     */
    EventCondition getCondition();

    /**
     * In case an {@link AdapterEvent} matches to this {@link IntermediateProcessEvent}, this means that an event
     * occurred for this {@link IntermediateProcessEvent} and that it can be triggered.
     */
    void trigger();
}
