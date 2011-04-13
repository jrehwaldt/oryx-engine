package de.hpi.oryxengine.correlation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.Service;
import de.hpi.oryxengine.correlation.adapter.AdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.InboundAdapter;
import de.hpi.oryxengine.correlation.adapter.InboundPullAdapter;
import de.hpi.oryxengine.correlation.adapter.TimedConfiguration;
import de.hpi.oryxengine.correlation.adapter.error.ErrorAdapter;
import de.hpi.oryxengine.correlation.adapter.error.ErrorAdapterConfiguration;
import de.hpi.oryxengine.correlation.registration.EventCondition;
import de.hpi.oryxengine.correlation.registration.IntermediateEvent;
import de.hpi.oryxengine.correlation.registration.ProcessEvent;
import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.correlation.timing.TimingManagerImpl;
import de.hpi.oryxengine.exception.AdapterSchedulingException;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.EngineInitializationFailedException;
import de.hpi.oryxengine.navigator.Navigator;

/**
 * A concrete implementation of our engines Event Manager.
 */
public class CorrelationManagerImpl implements CorrelationManager, EventRegistrar, Service {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Navigator navigator;
    private TimingManagerImpl timer;

    private ErrorAdapter errorAdapter;
    // there may be multiple references to an Adapter configuration (i.e. a mail account)
    // and if these all use the same Configuration Object we can avoid duplicated adapters
    // TODO give a process a list of of AdapterConfiguration Objects in order for this to work
    private Map<AdapterConfiguration, InboundAdapter> inboundAdapter;

    private List<StartEvent> startEvents;

    /**
     * Default constructor.
     * 
     * @param navigator
     *            the navigator
     */
    public CorrelationManagerImpl(@Nonnull Navigator navigator) {

        this.navigator = navigator;
        this.inboundAdapter = new HashMap<AdapterConfiguration, InboundAdapter>();
        this.startEvents = new ArrayList<StartEvent>();
        this.errorAdapter = new ErrorAdapter(this, new ErrorAdapterConfiguration());

        try {
            this.timer = new TimingManagerImpl(this.errorAdapter);
        } catch (SchedulerException se) {
            logger.error("Initializing the scheduler failed. EventManager not available.", se);
            throw new EngineInitializationFailedException("Creating a timer manager failed.", se);
        }
    }

    /**
     * This method starts the correlation manager and its dependent services.
     */
    public void start() {

        logger.info("Starting the correlation manager");
        registerAdapter(this.errorAdapter);
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
            createAdapaterForEvent(event);
            this.startEvents.add(event);
        } catch (AdapterSchedulingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Creates the adapater for an event according to its event type.
     * 
     * @param event
     *            the event
     * @throws AdapterSchedulingException
     *             the adapter scheduling exception
     */
    private void createAdapaterForEvent(ProcessEvent event)
    throws AdapterSchedulingException {

        // TODO: Implement Comparable for Adapter configurations
        // check if an adapter with the given configuration already exists
        if (inboundAdapter.containsKey(event.getEventConfiguration())) {
            return;
        }
        // Maybe it is possible to do this better.
        AdapterConfiguration configuration = (AdapterConfiguration) event.getEventConfiguration();
        configuration.createAdapter(this);
            
    }

    @Override
    public String registerIntermediateEvent(@Nonnull IntermediateEvent event) {
        String jobCompleteName = null;
        logger.debug("Registering intermediate event {}", event);
        try {
            jobCompleteName = this.timer.registerNonRecurringJob(
                (TimedConfiguration) event.getEventConfiguration(),
                event.getToken());
        } catch (AdapterSchedulingException e) {
            e.printStackTrace();
        }
        
        return jobCompleteName;
    }

    /**
     * A call to this method registers the corresponding adapter.
     * 
     * @param adapter
     *            the {@link InboundAdapter} to register
     * @param <Adapter>
     *            the adapter's type, generally {@link InboundAdapter} or {@link InboundPullAdapter}
     * @return the registered {@link InboundAdapter}
     */
    private @Nonnull
    <Adapter extends InboundAdapter> Adapter registerAdapter(@Nonnull Adapter adapter) {

        this.inboundAdapter.put(adapter.getConfiguration(), adapter);
        return adapter;
    }

    /**
     * A call to this method registers the corresponding adapter.
     * 
     * @param adapter
     *            the {@link InboundAdapter} to register
     * @return the registered {@link InboundAdapter}
     * @throws AdapterSchedulingException
     *             thrown if the scheduling fails
     */
    private @Nonnull
    InboundPullAdapter registerPullAdapter(@Nonnull InboundPullAdapter adapter)
    throws AdapterSchedulingException {

        this.timer.registerPullAdapter(adapter);
        return registerAdapter(adapter);
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
                System.out.println("starting process" + this.navigator);
            }
        }
    }

    /**
     * Returns the error adapter.
     * 
     * @return the error adapter
     */
    public ErrorAdapter getErrorAdapter() {

        return this.errorAdapter;
    }

    /**
     * Returns the list of registered inbound adapters.
     * 
     * @return the registered inbound adapter
     */
    public Collection<InboundAdapter> getInboundAdapters() {

        return this.inboundAdapter.values();
    }
    
    @Override
    public TimingManagerImpl getTimer() {
        
        return timer;
    }
}
