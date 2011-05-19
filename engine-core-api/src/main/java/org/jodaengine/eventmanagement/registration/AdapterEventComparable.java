package org.jodaengine.eventmanagement.registration;

import org.jodaengine.eventmanagement.AdapterEvent;

/**
 * Is able to compare an object with a given {@link AdapterEvent}.
 * 
 * Currently, such objects could be a {@link ProcessEvent} or an {@link EventCondition}.
 */
public interface AdapterEventComparable {

    /**
     * Evaluates if this object applies for the {@link AdapterEvent}.
     * 
     * @param adapterEvent
     *            - the {@link AdapterEvent} that is evaluated against this object
     * @return true or false as result of the evaluation
     */
    boolean evaluate(AdapterEvent adapterEvent);
}
