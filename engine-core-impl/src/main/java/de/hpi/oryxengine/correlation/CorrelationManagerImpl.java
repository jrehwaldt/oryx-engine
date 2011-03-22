package de.hpi.oryxengine.correlation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.correlation.adapter.AdapterType;
import de.hpi.oryxengine.correlation.adapter.AdapterTypes;
import de.hpi.oryxengine.correlation.adapter.InboundAdapter;
import de.hpi.oryxengine.correlation.adapter.InboundPullAdapter;
import de.hpi.oryxengine.correlation.adapter.error.ErrorAdapter;
import de.hpi.oryxengine.correlation.adapter.error.ErrorAdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.mail.InboundImapMailAdapterImpl;
import de.hpi.oryxengine.correlation.adapter.mail.MailAdapterConfiguration;
import de.hpi.oryxengine.correlation.registration.EventCondition;
import de.hpi.oryxengine.correlation.registration.IntermediateEvent;
import de.hpi.oryxengine.correlation.registration.ProcessEvent;
import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.correlation.timing.TimingManagerImpl;
import de.hpi.oryxengine.exception.AdapterSchedulingException;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.EngineInitializationFailedException;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.repository.ProcessRepositoryImpl;

/**
 * A concrete implementation of our engines Event Manager.
 */
public class CorrelationManagerImpl implements CorrelationManager, EventRegistrar {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Navigator navigator;
    private TimingManagerImpl timer;
    private ErrorAdapter errorHandler;
    private Map<AdapterType, InboundAdapter> inboundAdapter;

    private List<StartEvent> startEvents;
    private List<IntermediateEvent> intermediateEvents;

    /**
     * Default constructor.
     * 
     * @param navigator
     *            the navigator
     */
    public CorrelationManagerImpl(@Nonnull Navigator navigator) {

        this.navigator = navigator;
        this.inboundAdapter = new HashMap<AdapterType, InboundAdapter>();
        this.startEvents = new ArrayList<StartEvent>();
        this.intermediateEvents = new ArrayList<IntermediateEvent>();
        this.errorHandler = new ErrorAdapter(this, new ErrorAdapterConfiguration());

        try {
            this.timer = new TimingManagerImpl(this.errorHandler);
        } catch (SchedulerException se) {
            logger.error("Initializing the scheduler failed. EventManager not available.", se);
            throw new EngineInitializationFailedException("Creating a timer manager failed.", se);
        }
    }

    /**
     * This method starts the correlation manager and its dependent services.
     */
    public void start() {

        this.registerAdapter(this.errorHandler);
    }

    @Override
    public void correlate(@Nonnull AdapterEvent event) {

        logger.debug("Correlating {}", event);
        try {
            startEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        intermediateEvent(event);
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
     * @param event the event
     * @throws AdapterSchedulingException the adapter scheduling exception
     */
    private void createAdapaterForEvent(ProcessEvent event)
    throws AdapterSchedulingException {

        if (event.getAdapterType() == AdapterTypes.Mail) {
            InboundImapMailAdapterImpl adapter = new InboundImapMailAdapterImpl(this,
                (MailAdapterConfiguration) event.getAdapterConfiguration());
            this.timer.registerPullAdapter(adapter);
            logger.debug("Registered mail adapter {}", adapter);
        }
    }

    @Override
    public void registerIntermediateEvent(@Nonnull IntermediateEvent event) {

        logger.debug("Registering intermediate event {}", event);
        this.intermediateEvents.add(event);
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

        this.inboundAdapter.put(adapter.getAdapterType(), adapter);
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
     * Triggered if an event is identified as intermediate trigger.
     * 
     * @param e
     *            the event fired by a certain {@link InboundAdapter}
     */
    private void intermediateEvent(@Nonnull AdapterEvent e) {

        // TODO user story ready to be solved
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
            if (e.getAdapterType() != event.getAdapterType()) {
                continue;
            }
            for (EventCondition condition : event.getConditions()) {
                Method method = condition.getMethod();
                Object returnValue = method.invoke(e);
                if (!returnValue.equals(condition.getExpectedValue())) {
                    triggerEvent = false;
                }
            }
            if (triggerEvent) {
                this.navigator.startProcessInstance(event.getDefinitionID());
                System.out.println("starting process" + this.navigator);
            }
        }

        // don't generate a random UUID here, as it has to be one that a definition exists in the repository for.
        // this.navigator.startProcessInstance(ProcessRepositoryImpl.SIMPLE_PROCESS_ID);
    }
}
