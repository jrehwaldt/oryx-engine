package org.jodaengine.eventmanagement.processevent.incoming.start;

import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.incoming.AbstractIncomingProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.StartProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.TriggeringBehaviour;
import org.jodaengine.eventmanagement.processevent.incoming.start.triggering.DefaultProcessInstantiation;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.definition.ProcessDefinitionID;

/**
 * The Class StartEventImpl. Have a look at {@link StartProcessEvent}.
 */
public class IncomingProcessStartEvent extends AbstractIncomingProcessEvent implements StartProcessEvent {

    private ProcessDefinitionID definitionID;
    private Navigator navigator;

    /**
     * Instantiates a new start event impl.
     * 
     * @param config
     *            the config
     * @param condition
     *            the conditions
     * @param definitionID
     *            the def
     */
    public IncomingProcessStartEvent(AdapterConfiguration config,
                                     EventCondition condition,
                                     ProcessDefinitionID definitionID) {

        super(config, condition, new DefaultProcessInstantiation());
        this.definitionID = definitionID;
        // we do not yet have one
        this.navigator = null;
    }

    @Override
    public ProcessDefinitionID getDefinitionID() {

        return definitionID;
    }

    @Override
    public String toString() {

        return "Start event of the processdefinitionId: " + definitionID.toString();
    }

    @Override
    public void injectNavigatorService(Navigator navigator) {

        this.navigator = navigator;
    }

    /**
     * Returns the injected {@link Navigator} so that it can be accessed by the {@link TriggeringBehaviour}.
     * 
     * @return the {@link Navigator}
     */
    public Navigator getNavigator() {

        return navigator;

    }

}
