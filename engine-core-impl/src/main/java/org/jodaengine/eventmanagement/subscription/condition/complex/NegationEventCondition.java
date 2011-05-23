package org.jodaengine.eventmanagement.subscription.condition.complex;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;

/**
 * This EventCondition negates the result of another {@link EventCondition}.
 */
public class NegationEventCondition implements EventCondition {

    private EventCondition eventConditionToNegate;

    /**
     * Default Constructor.
     * 
     * @param eventConditionToNegate
     *            - the {@link EventCondition} that should be negated
     */
    public NegationEventCondition(EventCondition eventConditionToNegate) {

        this.eventConditionToNegate = eventConditionToNegate;
    }

    @Override
    public boolean evaluate(AdapterEvent adapterEvent) {

        return !eventConditionToNegate.evaluate(adapterEvent);
    }
}
