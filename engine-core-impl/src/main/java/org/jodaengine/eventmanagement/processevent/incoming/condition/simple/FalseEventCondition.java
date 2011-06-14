package org.jodaengine.eventmanagement.processevent.incoming.condition.simple;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;

/**
 * A Condition that never holds. It always returns false. 
 */
public class FalseEventCondition implements EventCondition {

    @Override
    public boolean evaluate(AdapterEvent adapterEvent) {

        return false;
    }
}
