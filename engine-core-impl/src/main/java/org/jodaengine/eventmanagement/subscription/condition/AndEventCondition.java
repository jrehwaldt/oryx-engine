package org.jodaengine.eventmanagement.subscription.condition;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.eventmanagement.AdapterEvent;

/**
 * This {@link EventCondition} is able to manage multiple {@link EventCondition}s.
 * 
 * <p>
 * It evaluates all the conditions assigned to this {@link AndEventCondition}. All conditions needs to be hold in order
 * to return 'true'.
 * </p>
 */
public class AndEventCondition implements EventCondition {

    private List<EventCondition> eventConditons;

    /**
     * Default Instantiation.
     * <p>
     * Can be used if the {@link EventCondition}s should be added iteratively.
     * </p>
     */
    public AndEventCondition() {

        this.eventConditons = new ArrayList<EventCondition>();
    }

    /**
     * Default Instantiation.
     * 
     * @param eventConditons
     *            - a list of {@link EventCondition}s that need to hold in order that the {@link AndEventCondition}
     *            holds
     */
    public AndEventCondition(List<EventCondition> eventConditons) {

        this.eventConditons = eventConditons;
    }

    /**
     * Add another {@link EventCondition}.
     *
     * @param eventCondition - the {@link EventCondition} that should be added
     * @return this {@link AndEventCondition}
     */
    public AndEventCondition addEventCondition(EventCondition eventCondition) {

        eventConditons.add(eventCondition);
        return this;
    }

    /**
     * Evaluates the conditions assigned to this {@link AndEventCondition}. All conditions needs to be hold in order to
     * return 'true'.
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean evaluate(AdapterEvent adapterEvent) {

        boolean returnBoolean = true;

        for (EventCondition theEventCondition : eventConditons) {

            returnBoolean = returnBoolean && theEventCondition.evaluate(adapterEvent);
            
            // If it is false, then you can leave right now
            if (!returnBoolean) {
                break;
            }
        }
        return returnBoolean;
    }
}
