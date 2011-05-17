package org.jodaengine.eventmanagement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javassist.expr.NewArray;

import javax.annotation.Nonnull;


import org.jodaengine.bootstrap.Service;
import org.jodaengine.eventmanagement.adapter.AdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.InboundAdapter;
import org.jodaengine.eventmanagement.adapter.TimedConfiguration;
import org.jodaengine.eventmanagement.adapter.error.ErrorAdapter;
import org.jodaengine.eventmanagement.adapter.error.ErrorAdapterConfiguration;
import org.jodaengine.eventmanagement.registration.EventCondition;
import org.jodaengine.eventmanagement.registration.IntermediateEvent;
import org.jodaengine.eventmanagement.registration.StartEvent;
import org.jodaengine.eventmanagement.timing.TimingManagerImpl;
import org.jodaengine.exception.AdapterSchedulingException;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.EngineInitializationFailedException;
import org.jodaengine.navigator.Navigator;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A concrete implementation of our engines Event Manager.
 */

// TODO: make Facade
public class EventManagerImpl implements EventManager, Service {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Navigator navigator;

    private List<StartEvent> startEvents;
    
    private AdapterRegistrar adapterRegistrar;

    /**
     * Default constructor.
     * 
     * @param navigator
     *            the navigator
     */
    public EventManagerImpl(@Nonnull Navigator navigator) {

        this.navigator = navigator;
        this. adapterRegistrar = new AdapterRegistration();
        
        this.startEvents = new ArrayList<StartEvent>();
    }

    /**
     * This method starts the correlation manager and its dependent services.
     */
    public void start() {

        logger.info("Starting the correlation manager");
    }

    @Override
    public void stop() {

        logger.info("Stopping the correlation manager");
    }

    @Override
    public void correlate(@Nonnull AdapterEvent event) {

        logger.debug("Correlating {}", event);
        try {
            startEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Maybe later used, to get the token from an UUID: intermediateEvent(event);
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

    @Override
    // TODO this are no intermediate events... at least not all of them work like that (this here is PULL)
    public String registerIntermediateEvent(@Nonnull IntermediateEvent event) {

        String jobCompleteName = null;
        logger.debug("Registering intermediate event {}", event);
        try {
            jobCompleteName = this.timer.registerNonRecurringJob((TimedConfiguration) event.getEventConfiguration(),
                event.getToken());
        } catch (AdapterSchedulingException e) {
            e.printStackTrace();
        }

        return jobCompleteName;
    }

    /**
     * Triggered if an {@link AdapterEvent} is identified as start trigger.
     * 
     * @param e
     *            the event fired by a certain adapter
     * @throws InvocationTargetException
     *             reflection exception
     * @throws IllegalAccessException
     *             reflection exception
     * @throws DefinitionNotFoundException
     *             thrown if starting this instance fails
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

    /**
     * Gets the timer.
     * 
     * @return the timer
     */
    @Override
    public TimingManagerImpl getTimer() {

        return timer;
    }
}
