package org.jodaengine.eventmanagement.processevent.incoming.condition.complex;

import java.util.List;

import org.jodaengine.eventmanagement.subscription.condition.EventCondition;

/**
 * An abstract superclass for conditions, that hold a list of other EventConditions.
 */
public abstract class AbstractMultipleEventCondition implements EventCondition {

    protected List<EventCondition> eventConditons;

    /**
     * Add another {@link EventCondition}.
     *
     * @param eventCondition - the {@link EventCondition} that should be added
     * @return this {@link AndEventCondition}
     */
    public AbstractMultipleEventCondition addEventCondition(EventCondition eventCondition) {
    
        eventConditons.add(eventCondition);
        return this;
    }
}
