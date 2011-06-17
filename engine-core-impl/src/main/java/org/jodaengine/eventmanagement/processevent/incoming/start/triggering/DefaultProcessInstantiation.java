package org.jodaengine.eventmanagement.processevent.incoming.start.triggering;

import org.jodaengine.eventmanagement.processevent.incoming.IncomingStartProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.TriggeringBehaviour;
import org.jodaengine.eventmanagement.processevent.incoming.start.BaseIncomingStartProcessEvent;
import org.jodaengine.eventmanagement.subscription.IncomingProcessEvent;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.navigator.Navigator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the default {@link TriggeringBehaviour} for an {@link BaseIncomingStartProcessEvent}. It calls the
 * {@link Navigator} in order to start the specified process.
 */
public class DefaultProcessInstantiation implements TriggeringBehaviour {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void trigger(IncomingProcessEvent processEvent) {

        if (!(processEvent instanceof BaseIncomingStartProcessEvent)) {
            String errorMessage = "The triggeringBehavior of the processEvent '" + processEvent
                + "'is supposed to be attached only to startProcessEvent.";
            logger.error(errorMessage);
            throw new JodaEngineRuntimeException(errorMessage);
        }

        doingProcessInstantiation((BaseIncomingStartProcessEvent) processEvent);
    }

    /**
     * This method encapsulates the process instantiation.
     * 
     * @param startProcessEvent
     *            - the {@link IncomingStartProcessEvent} that triggers the process instantiation
     */
    private void doingProcessInstantiation(BaseIncomingStartProcessEvent startProcessEvent) {

        logger.info("Starting a new processInstance for event {}", startProcessEvent);
        try {

            startProcessEvent.getNavigator().startProcessInstance(startProcessEvent.getDefinitionID(),
                startProcessEvent);

        } catch (DefinitionNotFoundException e) {
            String errorMessage = "The processDefinition belonging to the id '" + startProcessEvent.getDefinitionID()
                + "' could not be found.";
            logger.error(errorMessage, e);
            throw new JodaEngineRuntimeException(errorMessage, e);
        } catch (NullPointerException e) {
            String errorMessage = "trying to start a process instance by calling the navigator "
                + "without having injected a navigator";
            logger.error(errorMessage, e);
            throw new JodaEngineRuntimeException(errorMessage, e);
        }
        logger.info("ProcessInstance created for event {}", startProcessEvent);
    }
}
