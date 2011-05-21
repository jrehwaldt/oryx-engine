package org.jodaengine.eventmanagement.subscription.condition;

import org.jodaengine.eventmanagement.AdapterEvent;

/**
 * A Condition that never holds. It always returns false. 
 */
public class FalseEventCondition implements EventCondition {

    @Override
    public boolean evaluate(AdapterEvent adapterEvent) {

        return false;
    }
}
