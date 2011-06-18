package org.jodaengine.eventmanagement.processevent.incoming.intermediate.triggering;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.eventmanagement.processevent.incoming.AbstractIncomingProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.TriggeringBehavior;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IncomingIntermediateProcessEvent;
import org.jodaengine.eventmanagement.subscription.IncomingProcessEvent;
import org.jodaengine.process.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class groups several events together to a logical unit. If the {@link IncomingProcessEvent} is connected to
 * another {@link IncomingProcessEvent} than a {@link AbstractProcessEventGroup} can be used to specify that
 * connection.
 */
public abstract class AbstractProcessEventGroup implements TriggeringBehavior {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected Token token;
    private List<AbstractIncomingProcessEvent> groupedIncomingProcessEvents;

    /**
     * Default Constructor.
     * 
     * @param token
     */
    protected AbstractProcessEventGroup(Token token) {

        this.token = token;
    }

    /**
     * Adding {@link IncomingIntermediateProcessEvent}s to this {@link TriggeringBehavior}.
     * 
     * @param processIntermediateEvent
     *            - the {@link IncomingIntermediateProcessEvent} that should be added to this group
     * @return this {@link AbstractProcessEventGroup}
     */
    public AbstractProcessEventGroup addIncomingProcessEventToGroup(AbstractIncomingProcessEvent processIntermediateEvent) {

        getIntermediateEvents().add(processIntermediateEvent);
        processIntermediateEvent.setTriggeringBehaviour(this);

        return this;
    }

    /**
     * Getter for Lazy initialized {@link AbstractProcessEventGroup#groupedIncomingProcessEvents}.
     * 
     * @return a {@link List} of {@link AbstractIncomingProcessEvent}s
     */
    protected List<AbstractIncomingProcessEvent> getIntermediateEvents() {

        if (groupedIncomingProcessEvents == null) {
            groupedIncomingProcessEvents = new ArrayList<AbstractIncomingProcessEvent>();
        }
        return groupedIncomingProcessEvents;
    }
}
