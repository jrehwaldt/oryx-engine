package org.jodaengine.eventmanagement.subscription;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.ServiceFactory;
import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class groups several events together to a logical unit. If the {@link ProcessEvent} is connected to another
 * {@link ProcessEvent} than a {@link ProcessEventGroup} can be used to specify that connection
 * 
 */
// TODO @Gerardo Mach mal weiter commenting ...
public class ProcessEventGroup {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Token token;
    private List<ProcessIntermediateEvent> intermediateEvents;
    private boolean called = false;

    /**
     * Getter for Lazy initialized {@link ProcessEventGroup#intermediateEvents}.
     * 
     * @return a {@link List} of {@link ProcessIntermediateEvent}s
     */
    private List<ProcessIntermediateEvent> getIntermediateEvents() {

        if (intermediateEvents == null) {
            intermediateEvents = new ArrayList<ProcessIntermediateEvent>();
        }
        return intermediateEvents;
    }

    public ProcessEventGroup(Token token) {

        this.token = token;
    }

    public void add(ProcessIntermediateEvent processIntermediateEvent) {

        getIntermediateEvents().add(processIntermediateEvent);
    }

    public synchronized void trigger(ProcessIntermediateEvent processIntermediateEvent) {

        if (called) {
            return;
        }

        if (!getIntermediateEvents().remove(processIntermediateEvent)) {
            // Then it means the element that should be deleted was not in the list of registered Events
            String errorMessage = "The event-based Xor Gateway was resumed by an event that it has not registered.";
            logger.error(errorMessage);
            throw new JodaEngineRuntimeException(errorMessage);
        }

        unsubscribingOtherEvent();

        token.resume(processIntermediateEvent);
    }

    private void unsubscribingOtherEvent() {

        // Unsubscribing the other registered events; doing it as early as possible

        // TODO @Gerardo muss geändert werden keine ServiceFactory mehr - vielleicht im Token
        EventSubscriptionManager eventManager = ServiceFactory.getCorrelationService();
        for (ProcessIntermediateEvent registeredProcessEvent : getIntermediateEvents()) {
            eventManager.unsubscribeFromIntermediateEvent(registeredProcessEvent);
        }
    }
}
