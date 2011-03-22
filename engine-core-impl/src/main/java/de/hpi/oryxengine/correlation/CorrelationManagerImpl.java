package de.hpi.oryxengine.correlation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.correlation.adapter.AdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.AdapterType;
import de.hpi.oryxengine.correlation.adapter.InboundPullAdapter;
import de.hpi.oryxengine.correlation.adapter.mail.InboundImapMailAdapterImpl;
import de.hpi.oryxengine.correlation.adapter.mail.MailAdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.mail.MailProtocol;
import de.hpi.oryxengine.correlation.timing.TimingManagerImpl;
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
    
    private Map<AdapterType, InboundPullAdapter> inboundAdapter;
    
    private List<AdapterType> startEvents;
    private List<AdapterType> intermediateEvents;
    private InboundPullAdapter adapter;

    /**
     * Default constructor.
     * 
     * @param navigator
     *            the navigator
     */
    public CorrelationManagerImpl(@Nonnull Navigator navigator) {
        this.navigator = navigator;
        this.inboundAdapter = new HashMap<AdapterType, InboundPullAdapter>();
        this.startEvents = new ArrayList<AdapterType>();
        this.intermediateEvents = new ArrayList<AdapterType>();
        
        try {
            this.timer = new TimingManagerImpl(this);
        } catch (SchedulerException se) {
            logger.error("Initializing the scheduler failed. EventManager not available.", se);
            throw new EngineInitializationFailedException("Creating a timer manager failed.", se);
        }
    }
    
    /**
     * This method starts the correlation manager and
     * its dependent services.
     */
    public void start() {
//        Thread t = new Thread(this.timer);
//        this.timer.setThread(t);
//        t.start();
        // TODO timer JAN
        
        this.adapter = initializeAdapter();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void correlate(@Nonnull AdapterEvent event) {
        
        AdapterConfiguration configuration = event.getAdapterConfiguration();
        
        System.out.println("correlating...");
        if (this.startEvents.contains(configuration.getAdapterType())) {
            try {
                startEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (this.intermediateEvents.contains(configuration.getAdapterType())) {
            intermediateEvent(event);
        }
    }

    @Override
    public void registerCorrelationEvent() {

        this.startEvents.add(this.adapter.getAdapterType());
    }

    InboundPullAdapter initializeAdapter() {

        MailAdapterConfiguration config = new MailAdapterConfiguration(MailProtocol.IMAP, "oryxengine", "dalmatina!",
            "imap.gmail.com", MailProtocol.IMAP.getPort(true), true);

        final InboundPullAdapter mailAdapter = new InboundImapMailAdapterImpl(this, config);
        this.inboundAdapter.put(mailAdapter.getAdapterType(), mailAdapter);

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

        System.out.println("starting process" + this.navigator);

        // don't generate a random UUID here, as it has to be one that a definition exists in the repository for.
        this.navigator.startProcessInstance(ProcessRepositoryImpl.SIMPLE_PROCESS_ID);
    }

    public Collection<InboundPullAdapter> getPullingAdapters() {

        return this.inboundAdapter.values();
    }
}
