package org.jodaengine.eventmanagement.processevent.incoming.condition.complex;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;

/**
 * This {@link EventCondition} is able to manage multiple {@link EventCondition}s.
 * 
 * <p>
 * It evaluates all the conditions assigned to this {@link AndEventCondition}. All conditions need to be hold in order
 * to return 'true'.
 * </p>
 */
public class AndEventCondition extends AbstractMultipleEventCondition {

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
     * Evaluates the conditions assigned to this {@link AndEventCondition}. All conditions need to be hold in order to
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
