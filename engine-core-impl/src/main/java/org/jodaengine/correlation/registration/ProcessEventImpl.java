package org.jodaengine.correlation.registration;

import org.jodaengine.correlation.EventConfiguration;
import org.jodaengine.correlation.adapter.AdapterConfiguration;
import org.jodaengine.correlation.adapter.EventType;

import java.util.List;


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
    public EventConfiguration getEventConfiguration() {

        return config;
    }

    @Override
    public List<EventCondition> getConditions() {

        return conditions;
    }

}
