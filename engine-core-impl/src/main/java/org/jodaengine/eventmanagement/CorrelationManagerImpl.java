package org.jodaengine.eventmanagement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.registration.EventCondition;
import org.jodaengine.eventmanagement.registration.StartEvent;
import org.jodaengine.exception.AdapterSchedulingException;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.navigator.Navigator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class CorrelationManagerImpl responsible for correlating Events to entities that subscribed to those Events.
 */
public class CorrelationManagerImpl implements CorrelationManager, EventRegistrar {

    /** The logger. */
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private List<StartEvent> startEvents;
    private Navigator navigator;
    
    /**
     * Instantiates a new correlation manager impl.
     *
     * @param navigator the navigator needed to correlate events that trigger the start of a new ProcessInstance.
     */
    public CorrelationManagerImpl(Navigator navigator) {
        this.navigator = navigator;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void correlate(@Nonnull AdapterEvent event)  {
    
        logger.debug("Correlating {}", event);
        try {
            startEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Maybe later used, to get the token from an UUID: intermediateEvent(event);
    }


    /**
     * Triggered if an {@link AdapterEvent} is identified as start trigger.
     *
     * @param e the event fired by a certain adapter
     * @throws DefinitionNotFoundException thrown if starting this instance fails
     * @throws IllegalAccessException reflection exception
     * @throws InvocationTargetException reflection exception
     */
    private void startEvent(@Nonnull AdapterEvent e)
    throws DefinitionNotFoundException, IllegalAccessException, InvocationTargetException {
    
        for (StartEvent event : startEvents) {
            boolean triggerEvent = true;
    
            // don't correlate events of different types.
            if (e.getAdapterType() != event.getEventConfiguration().getEventType()) {
                continue;
            }
    
            // don't correlate events that were assigned to different configurations.
            if (e.getAdapterConfiguration() != event.getEventConfiguration()) {
                continue;
            }
    
            for (EventCondition condition : event.getConditions()) {
                Method method = condition.getMethod();
                Object returnValue = method.invoke(e);
                if (!returnValue.equals(condition.getExpectedValue())) {
                    triggerEvent = false;
                    break;
                }
            }
            if (triggerEvent) {
                this.navigator.startProcessInstance(event.getDefinitionID(), event);
                logger.info("Starting process {}", this.navigator);
            }
        }
    }

    @Override
    public void registerStartEvent(@Nonnull StartEvent event) {
    
        logger.debug("Registering start event {}", event);
        try {
            registerAdapaterForEvent(event);
            this.startEvents.add(event);
        } catch (AdapterSchedulingException e) {
            e.printStackTrace();
        }
    }

}
