package de.hpi.oryxengine.correlation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.adapter.PullingInboundAdapter;
import de.hpi.oryxengine.correlation.adapter.mail.InboundImapMailAdapterImpl;
import de.hpi.oryxengine.correlation.adapter.mail.MailAdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.mail.MailType;
import de.hpi.oryxengine.correlation.timing.TimingManagerImpl;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.repository.ProcessRepositoryImpl;

/**
 * A concrete implementation of our engines Event Manager.
 */
public class CorrelationManagerImpl implements CorrelationManager, EventRegistrar {

    private Navigator navigator;

    private Map<EventType, PullingInboundAdapter> inboundAdapter;

    private List<EventType> startEvents;
    private List<EventType> intermediateEvents;
    private PullingInboundAdapter adapter;

    private TimingManagerImpl timer;

    /**
     * Default constructor.
     * 
     * @param navigator
     *            the navigator
     */
    public CorrelationManagerImpl(@Nonnull Navigator navigator) {

        this.navigator = navigator;
        this.inboundAdapter = new HashMap<EventType, PullingInboundAdapter>();
        this.startEvents = new ArrayList<EventType>();
        this.intermediateEvents = new ArrayList<EventType>();

        this.timer = new TimingManagerImpl(this);
        Thread t = new Thread(this.timer);
        this.timer.setThread(t);
        t.start();

        this.adapter = initializeAdapter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void correlate(@Nonnull AdapterEvent event) {

        System.out.println("correlating...");
        if (this.startEvents.contains(event.getEventType())) {
            try {
                startEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (this.intermediateEvents.contains(event.getEventType())) {
            intermediateEvent(event);
        }
    }

    @Override
    public void registerCorrelationEvent() {

        this.startEvents.add(this.adapter.getEventType());
    }

    PullingInboundAdapter initializeAdapter() {

        MailAdapterConfiguration config = new MailAdapterConfiguration(MailType.IMAP, "oryxengine", "dalmatina!",
            "imap.gmail.com", MailType.IMAP.getPort(true), true);

        final PullingInboundAdapter mailAdapter = new InboundImapMailAdapterImpl(this, config);
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

        System.out.println("starting process" + this.navigator);

        // don't generate a random UUID here, as it has to be one that a definition exists in the repository for.
        this.navigator.startProcessInstance(ProcessRepositoryImpl.SIMPLE_PROCESS_ID);
    }

    public Collection<PullingInboundAdapter> getPullingAdapters() {

        return this.inboundAdapter.values();
    }
}
