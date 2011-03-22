package de.hpi.oryxengine.correlation;

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

import de.hpi.oryxengine.correlation.adapter.InboundPullAdapter;
import de.hpi.oryxengine.correlation.adapter.mail.InboundImapMailAdapterImpl;
import de.hpi.oryxengine.correlation.adapter.mail.MailAdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.mail.MailAdapterEvent;
import de.hpi.oryxengine.correlation.adapter.mail.MailType;
import de.hpi.oryxengine.correlation.registration.EventCondition;
import de.hpi.oryxengine.correlation.registration.IntermediateEvent;
import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.correlation.timing.TimingManagerImpl;
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

    private Map<EventType, InboundPullAdapter> inboundAdapter;

    private List<StartEvent> startEvents;
    private List<IntermediateEvent> intermediateEvents;
    private InboundPullAdapter adapter;

    /**
     * Default constructor.
     * 
     * @param navigator
     *            the navigator
     */
    public CorrelationManagerImpl(@Nonnull Navigator navigator) {

        this.navigator = navigator;
        this.inboundAdapter = new HashMap<EventType, InboundPullAdapter>();
        this.startEvents = new ArrayList<StartEvent>();
        this.intermediateEvents = new ArrayList<IntermediateEvent>();

        try {
            this.timer = new TimingManagerImpl(this);
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

        this.adapter = initializeAdapter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void correlate(@Nonnull AdapterEvent event) {

        System.out.println("correlating...");
        try {
            startEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        intermediateEvent(event);
    }

    @Override
    public void registerStartEvent(StartEvent event) {

        this.startEvents.add(event);
    }

    @Override
    public void registerIntermediateEvent(IntermediateEvent event) {

        this.intermediateEvents.add(event);
    }

    InboundPullAdapter initializeAdapter() {

        MailAdapterConfiguration config = new MailAdapterConfiguration(MailType.IMAP, "oryxengine", "dalmatina!",
            "imap.gmail.com", MailType.IMAP.getPort(true), true);

        final InboundPullAdapter mailAdapter = new InboundImapMailAdapterImpl(this, config);
        this.inboundAdapter.put(mailAdapter.getEventType(), mailAdapter);

        return mailAdapter;
    }

    /**
     * Triggered if an event is identified as intermediate trigger.
     * 
     * @param e
     *            the event fired by a certain adapter
     */
    private void intermediateEvent(@Nonnull AdapterEvent e) {

        // TODO user story ready to be solved
    }

    /**
     * Triggered if an event is identified as start trigger.
     * 
     * @param e
     *            the event fired by a certain adapter
     * @throws Exception
     *             thrown if starting this instance fails
     */
    private void startEvent(@Nonnull AdapterEvent e)
    throws Exception {

        for (StartEvent event : startEvents) {
            boolean triggerEvent = true;
            if (e.getEventType() != event.getEventType()) {
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
                // don't generate a random UUID here, as it has to be one that a definition exists in the repository
                // for.
                this.navigator.startProcessInstance(ProcessRepositoryImpl.SIMPLE_PROCESS_ID);
                System.out.println("starting process" + this.navigator);
            }
        }
    }

    public Collection<InboundPullAdapter> getPullingAdapters() {

        return this.inboundAdapter.values();
    }
}
