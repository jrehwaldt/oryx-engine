package org.jodaengine.eventmanagement.subscription.condition;

import org.jodaengine.eventmanagement.AdapterEvent;

/**
 * A Condition that always holds. It always returns true. 
 */
public class TrueEventCondition implements EventCondition {

    @Override
    public boolean evaluate(AdapterEvent adapterEvent) {

        return true;
    }
}
