package org.jodaengine.eventmanagement.registration;

import java.util.List;
import java.util.UUID;

import org.jodaengine.eventmanagement.adapter.AdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.EventType;

/**
 * The Class StartEventImpl. Have a look at {@link ProcessStartEvent}.
 */
public class StartEventImpl extends ProcessEventImpl implements ProcessStartEvent {

    private UUID definitionID;

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
                          UUID definitionID) {

        super(type, config, conditions);
        this.definitionID = definitionID;
    }
    
    @Override
    public UUID getDefinitionID() {

        return definitionID;
    }

}
