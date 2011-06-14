package org.jodaengine.eventmanagement.processevent.incoming;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.adapter.EventType;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.AbstractProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.condition.simple.TrueEventCondition;
import org.jodaengine.eventmanagement.subscription.IntermediateProcessEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;

/**
 * The Class ProcessEventImpl. Have a look at {@link IntermediateProcessEvent}.
 */
public abstract class AbstractIncomingProcessEvent extends AbstractProcessEvent implements IntermediateProcessEvent {

    private EventCondition condition;

    /**
     * Instantiates a new {@link IntermediateProcessEvent}.
     * 
     * @param type
     *            the type
     * @param config
     *            the config
     * @param condition
     *            the conditions
     */
    protected AbstractIncomingProcessEvent(EventType type, AdapterConfiguration config, EventCondition condition) {

        this.config = config;
        this.condition = condition;
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
}
