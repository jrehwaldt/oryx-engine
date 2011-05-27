package org.jodaengine.eventmanagement.subscription;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.adapter.EventType;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.eventmanagement.subscription.condition.simple.TrueEventCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ProcessEventImpl. Have a look at {@link ProcessEvent}.
 */
public abstract class AbstractProcessEvent implements ProcessEvent {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private AdapterConfiguration config;
    private EventCondition condition;

    /**
     * Instantiates a new {@link ProcessEvent}.
     * 
     * @param type
     *            the type
     * @param config
     *            the config
     * @param condition
     *            the conditions
     */
    protected AbstractProcessEvent(EventType type, AdapterConfiguration config, EventCondition condition) {

        this.config = config;
        this.condition = condition;
    }

    @Override
    public AdapterConfiguration getAdapterConfiguration() {

        return config;
    }

    @Override
    public EventCondition getCondition() {

        if (this.condition == null) {
            this.condition = new TrueEventCondition();
        }
        return condition;
    }

    @Override
    public boolean evaluate(AdapterEvent adapterEvent) {

        return getCondition().evaluate(adapterEvent);
    }

    @Override
    public abstract void trigger();

    @Override
    // TODO @EVENTTEAM: add beauty
    public String toString() {

        String resultString = "ProcessEvent '" + getAdapterConfiguration().toString() + "' for type "
            + getAdapterConfiguration().getEventType() + ".";
        return resultString;
    }
}
