package org.jodaengine.eventmanagement.registration;

import java.util.List;

import org.jodaengine.eventmanagement.adapter.AdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.EventType;
import org.jodaengine.process.definition.ProcessDefinitionID;

/**
 * The Class StartEventImpl. Have a look at {@link StartEvent}.
 */
public class StartEventImpl extends ProcessEventImpl implements StartEvent {

    private ProcessDefinitionID definitionID;

    /**
     * Instantiates a new start event impl.
     * 
     * @param type
     *            the type
     * @param config
     *            the config
     * @param conditions
     *            the conditions
     * @param definitionID
     *            the def
     */
    public StartEventImpl(EventType type,
                          AdapterConfiguration config,
                          List<EventCondition> conditions,
                          ProcessDefinitionID definitionID) {

        super(type, config, conditions);
        this.definitionID = definitionID;
    }
    
    @Override
    public ProcessDefinitionID getDefinitionID() {

        return definitionID;
    }

}
