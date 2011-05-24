package org.jodaengine.eventmanagement.subscription;

import org.jodaengine.ServiceFactory;
import org.jodaengine.eventmanagement.adapter.EventType;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.navigator.NavigatorInside;
import org.jodaengine.process.definition.ProcessDefinitionID;


/**
 * The Class StartEventImpl. Have a look at {@link ProcessStartEvent}.
 */
public class StartEventImpl extends AbstractProcessEvent implements ProcessStartEvent {

    private ProcessDefinitionID definitionID;

    /**
     * Instantiates a new start event impl.
     * 
     * @param type
     *            the type
     * @param config
     *            the config
     * @param condition
     *            the conditions
     * @param definitionID
     *            the def
     */
    public StartEventImpl(EventType type,
                          AdapterConfiguration config,
                          EventCondition condition,
                          ProcessDefinitionID definitionID) {

        super(type, config, condition);
        this.definitionID = definitionID;
    }

    @Override
    public ProcessDefinitionID getDefinitionID() {

        return definitionID;
    }

    @Override
    public void trigger() {

//        logger.info("Starting a new processInstance for event {}", this);
        NavigatorInside navigator = (NavigatorInside) ServiceFactory.getNavigatorService();
        try {
            
            navigator.startProcessInstance(getDefinitionID(), this);
            
        } catch (DefinitionNotFoundException e) {
            String errorMessage = "The processDefinition belonging to the id '" + getDefinitionID()
                + "' could not be found.";
            logger.error(errorMessage, e);
            throw new JodaEngineRuntimeException(errorMessage, e);
        }
        logger.info("ProcessInstance created for event {}", this);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Start event of the processdefinitionId: " + definitionID.toString();
    }

}
