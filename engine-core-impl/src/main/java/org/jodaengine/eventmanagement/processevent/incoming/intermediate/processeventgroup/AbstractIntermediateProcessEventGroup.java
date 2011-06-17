package org.jodaengine.eventmanagement.processevent.incoming.intermediate.processeventgroup;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.eventmanagement.processevent.incoming.TriggeringBehaviour;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IncomingIntermediateProcessEvent;
import org.jodaengine.eventmanagement.subscription.IncomingProcessEvent;
import org.jodaengine.process.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class groups several events together to a logical unit. If the {@link IncomingProcessEvent} is connected to
 * another {@link IncomingProcessEvent} than a {@link AbstractIntermediateProcessEventGroup} can be used to specify that
 * connection.
 */
public abstract class AbstractIntermediateProcessEventGroup implements TriggeringBehaviour {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected Token token;
    private List<IncomingIntermediateProcessEvent> intermediateEvents;
    protected boolean called = false;

    /**
     * Default Constructor.
     * 
     * @param token
     */
    protected AbstractIntermediateProcessEventGroup(Token token) {

        this.token = token;
    }

    /**
     * Adding {@link IncomingIntermediateProcessEvent}s to this {@link TriggeringBehaviour}.
     * 
     * @param processIntermediateEvent
     *            - the {@link IncomingIntermediateProcessEvent} that should be added to this group
     */
    public void add(IncomingIntermediateProcessEvent processIntermediateEvent) {

        getIntermediateEvents().add(processIntermediateEvent);
    }

    @Override
    public synchronized void trigger(IncomingProcessEvent processEvent) {

        // If it was already called then then leave right now
        if (called) {
            return;
        }

        triggerIntern((IncomingIntermediateProcessEvent) processEvent);
    }

    /**
     * If an {@link IncomingProcessEvent} that belongs to that {@link TriggeringBehaviour} is triggered than this method
     * is called.
     * 
     * @param processIntermediateEvent
     *            - the {@link IncomingIntermediateProcessEvent} that was triggered
     */
    protected abstract void triggerIntern(IncomingIntermediateProcessEvent processIntermediateEvent);

    /**
     * Getter for Lazy initialized {@link AbstractIntermediateProcessEventGroup#intermediateEvents}.
     * 
     * @return a {@link List} of {@link IncomingIntermediateProcessEvent}s
     */
    protected List<IncomingIntermediateProcessEvent> getIntermediateEvents() {

        if (intermediateEvents == null) {
            intermediateEvents = new ArrayList<IncomingIntermediateProcessEvent>();
        }
        return intermediateEvents;
    }
}
