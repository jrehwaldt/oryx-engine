package org.jodaengine.eventmanagement.subscription.condition.simple;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;

/**
 * A Condition that always holds. It always returns true. 
 */
public class TrueEventCondition implements EventCondition {

    @Override
    public boolean evaluate(AdapterEvent adapterEvent) {

        return true;
    }
}
