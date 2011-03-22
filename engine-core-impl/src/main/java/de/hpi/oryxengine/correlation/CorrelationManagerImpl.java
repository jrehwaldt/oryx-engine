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
import de.hpi.oryxengine.correlation.adapter.InboundAdapter;
import de.hpi.oryxengine.correlation.adapter.InboundPullAdapter;
import de.hpi.oryxengine.correlation.adapter.error.ErrorAdapter;
import de.hpi.oryxengine.correlation.adapter.error.ErrorAdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.mail.InboundImapMailAdapterImpl;
import de.hpi.oryxengine.correlation.adapter.mail.MailAdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.mail.MailProtocol;
import de.hpi.oryxengine.correlation.registration.EventCondition;
import de.hpi.oryxengine.correlation.registration.IntermediateEvent;
import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.correlation.timing.TimingManagerImpl;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.EngineInitializationFailedException;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.repository.ProcessRepositoryImpl;

/**
 * A concrete implementation of our engines Event Manager.
 */
public class CorrelationManagerImpl
implements CorrelationManager, EventRegistrar {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Navigator navigator;
    private TimingManagerImpl timer;
    private ErrorAdapter errorHandler;
    private Map<AdapterType, InboundAdapter> inboundAdapter;
    private InboundPullAdapter adapter; // TODO remove, this is a hack

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

        // Thread t = new Thread(this.timer);
        // this.timer.setThread(t);
        // t.start();
        // TODO timer JAN
        
        this.registerAdapter(this.errorHandler);
        this.adapter = initializeAdapter();
    }

    /**
     * {@inheritDoc}
     */
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
        this.startEvents.add(event);
    }

    @Override
    public void registerIntermediateEvent(@Nonnull IntermediateEvent event) {
        this.intermediateEvents.add(event);
    }
    
    /**
     * A call to this method registers the corresponding adapter.
     * 
     * @param adapter the {@link InboundAdapter} to register
     * @param <Adapter> the adapter's type, generally {@link InboundAdapter} or {@link InboundPullAdapter}
     * @return the registered {@link InboundAdapter}
     */
    private @Nonnull <Adapter extends InboundAdapter> Adapter registerAdapter(@Nonnull Adapter adapter) {
        this.inboundAdapter.put(adapter.getAdapterType(), adapter);
        return adapter;
    }
    
    @Nonnull InboundPullAdapter initializeAdapter() {
        
        MailAdapterConfiguration config = new MailAdapterConfiguration(MailProtocol.IMAP, "oryxengine", "dalmatina!",
            "imap.gmail.com", MailProtocol.IMAP.getPort(true), true);

        final InboundPullAdapter mailAdapter = new InboundImapMailAdapterImpl(this, config);
        return registerAdapter(mailAdapter);
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
     * @throws InvocationTargetException reflection exception
     * @throws IllegalAccessException reflection exception
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
                this.navigator.startProcessInstance(ProcessRepositoryImpl.SIMPLE_PROCESS_ID);
                System.out.println("starting process" + this.navigator);
            }
        }

        // don't generate a random UUID here, as it has to be one that a definition exists in the repository for.
        // this.navigator.startProcessInstance(ProcessRepositoryImpl.SIMPLE_PROCESS_ID);
    }
}
