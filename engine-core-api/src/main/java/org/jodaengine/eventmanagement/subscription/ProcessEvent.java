package org.jodaengine.eventmanagement.subscription;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.condition.AdapterEventComparable;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;

/**
 * The Interface ProcessEvent. All process events have an assigned adapter with a given configuration that may produce
 * incoming events that correlate against the process event.
 * 
 * Could extend Attributable!!!
 */
public interface ProcessEvent extends AdapterEventComparable {

    /**
     * Gets the adapter configuration.
     * 
     * @return the adapter configuration
     */

    AdapterConfiguration getAdapterConfiguration();

    /**
     * Gets the conditions.
     * 
     * @return the conditions
     */
    EventCondition getCondition();

    /**
     * In case an {@link AdapterEvent} matches to this {@link ProcessEvent}, this means that an event occurred for this
     * {@link ProcessEvent} and that it can be triggered.
     */
    void trigger();
}
