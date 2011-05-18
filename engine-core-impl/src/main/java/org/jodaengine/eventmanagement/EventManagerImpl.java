package org.jodaengine.eventmanagement;

import java.util.ArrayList;
import java.util.HashMap;

import javassist.expr.NewArray;

import javax.annotation.Nonnull;

import org.jodaengine.bootstrap.Service;
import org.jodaengine.eventmanagement.adapter.AdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.InboundAdapter;
import org.jodaengine.eventmanagement.adapter.InboundPullAdapter;
import org.jodaengine.eventmanagement.adapter.TimedConfiguration;
import org.jodaengine.eventmanagement.adapter.error.ErrorAdapter;
import org.jodaengine.eventmanagement.adapter.error.ErrorAdapterConfiguration;
import org.jodaengine.eventmanagement.registration.IntermediateEvent;
import org.jodaengine.eventmanagement.registration.StartEvent;
import org.jodaengine.eventmanagement.timing.TimingManager;
import org.jodaengine.exception.AdapterSchedulingException;
import org.jodaengine.exception.EngineInitializationFailedException;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.token.Token;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A concrete implementation of our engines Event Manager.
 */

// TODO: make Facade
public class EventManagerImpl implements EventManager, Service {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private AdapterRegistrar adapterRegistrar;

    private CorrelationManager correlationManager;

    /**
     * Default constructor.
     * 
     * @param navigator
     *            the navigator
     */
    public EventManagerImpl(@Nonnull Navigator navigator) {

        this.correlationManager = new CorrelationManagerImpl(navigator);
        this.adapterRegistrar = new AdapterRegistration(correlationManager);
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
    // TODO this are no intermediate events... at least not all of them work like that (this here is PULL)
    public String registerIntermediateEvent(@Nonnull IntermediateEvent event) {

        String jobCompleteName;
        logger.debug("Registering intermediate event {}", event);
        jobCompleteName = this.adapterRegistrar.registerIntermediateTimerEvent(event);
        return jobCompleteName;
    }
}
