package org.jodaengine.eventmanagement.registration;

import java.util.List;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.adapter.AdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.EventType;

/**
 * The Class ProcessEventImpl. Have a look at {@link ProcessEvent}.
 */
public class ProcessEventImpl implements ProcessEvent {
    
    private AdapterConfiguration config;
    private List<EventCondition> conditions;

    /**
     * Instantiates a new process event impl.
     * 
     * @param type
     *            the type
     * @param config
     *            the config
     * @param conditions
     *            the conditions
     */
    protected ProcessEventImpl(EventType type, AdapterConfiguration config, List<EventCondition> conditions) {

        this.config = config;
        this.conditions = conditions;
    }

    @Override
    public AdapterConfiguration getAdapterConfiguration() {

        return config;
    }

    @Override
    public List<EventCondition> getConditions() {

        return conditions;
    }

    @Override
    public boolean evaluate(AdapterEvent adapterEvent) {

        if (getConditions().isEmpty()) {
            return true;
        }
        
        return evaluateConditions(adapterEvent);
    }

    /**
     * Evaluates the conditions assigned to this {@link ProcessEvent}. All conditions needs to be hold in order to return 'true'. 
     * 
     * @param adapterEvent
     *            - the {@link AdapterEvent} that is evaluated against this object
     * @return true or false as result of the evaluation
     */
    private boolean evaluateConditions(AdapterEvent adapterEvent) {

        boolean returnBoolean = true;
        
        for (EventCondition theEventCondition : getConditions()) {
        
            returnBoolean = returnBoolean && theEventCondition.evaluate(adapterEvent); 
        }
        return false;
    }

    @Override
    public void trigger() {

        // TODO Auto-generated method stub
        
    }

}
