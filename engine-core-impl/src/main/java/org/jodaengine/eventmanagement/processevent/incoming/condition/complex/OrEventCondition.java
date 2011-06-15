package org.jodaengine.eventmanagement.processevent.incoming.condition.complex;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;

/**
 * This {@link EventCondition} is able to manage multiple {@link EventCondition}s.
 * 
 * <p>
 * It evaluates all the conditions assigned to this {@link OrEventCondition}. Only one condition needs to be hold in
 * order to return 'true'.
 * </p>
 */
public class OrEventCondition implements EventCondition {

    private List<EventCondition> eventConditons;

    /**
     * Default Instantiation.
     * <p>
     * Can be used if the {@link EventCondition}s should be added iteratively.
     * </p>
     */
    public OrEventCondition() {

        this.eventConditons = new ArrayList<EventCondition>();
    }

    /**
     * Default Instantiation.
     * 
     * @param eventConditons
     *            - a list of {@link EventCondition}s that only one of them needs to hold in order that the
     *            {@link OrEventCondition} holds
     */
    public OrEventCondition(List<EventCondition> eventConditons) {

        this.eventConditons = eventConditons;
    }

    /**
     * Add another {@link EventCondition}.
     * 
     * @param eventCondition
     *            - the {@link EventCondition} that should be added
     * @return this {@link OrEventCondition}
     */
    public OrEventCondition addEventCondition(EventCondition eventCondition) {

        eventConditons.add(eventCondition);
        return this;
    }

    /**
     * Evaluates the conditions assigned to this {@link OrEventCondition}. Only one condition needs to be hold in order
     * to
     * return 'true'.
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean evaluate(AdapterEvent adapterEvent) {

        boolean returnBoolean = false;

        for (EventCondition theEventCondition : eventConditons) {

            returnBoolean = returnBoolean || theEventCondition.evaluate(adapterEvent);

            // If it is true, then you can leave right now
            if (returnBoolean) {
                break;
            }
        }
        return returnBoolean;
    }
}
