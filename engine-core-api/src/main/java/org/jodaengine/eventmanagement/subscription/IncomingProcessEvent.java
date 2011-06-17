package org.jodaengine.eventmanagement.subscription;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.processevent.ProcessEvent;
import org.jodaengine.eventmanagement.subscription.condition.AdapterEventComparable;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;

/**
 * The Interface IncomingProcessEvent. 
 * Those Events need to be triggered and have a condition under which they should get triggered.
 * 
 * Could extend Attributable!!!
 */
public interface IncomingProcessEvent extends ProcessEvent, AdapterEventComparable {

    /**
     * Gets the conditions.
     * 
     * @return the conditions
     */
    EventCondition getCondition();

    /**
     * In case an {@link AdapterEvent} matches to this {@link IncomingProcessEvent}, this means that an event
     * occurred for this {@link IncomingProcessEvent} and that it can be triggered.
     */
    void trigger();
}
