package org.jodaengine.eventmanagement.subscription.processeventgroup.intermediate;

import org.jodaengine.ServiceFactory;
import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.eventmanagement.subscription.ProcessEvent;
import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.eventmanagement.subscription.TriggeringBehaviour;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.token.Token;

/**
 * This represents a {@link TriggeringBehaviour} that a soon a {@link ProcessEvent} of this group is triggered this group
 * will unregister all other {@link ProcessEvent} that are in this group.
 */
public class ExclusiveProcessEventGroup extends AbstractProcessIntermediateEventGroup {

    /**
     * Default Constructor this {@link ExclusiveProcessEventGroup}.
     * 
     * @param token
     *            - the {@link Token} that created this {@link ExclusiveProcessEventGroup}
     */
    public ExclusiveProcessEventGroup(Token token) {

        super(token);
    }

    @Override
    public synchronized void triggerIntern(ProcessIntermediateEvent processIntermediateEvent) {

        if (!getIntermediateEvents().remove(processIntermediateEvent)) {

            // Then it means the element that should be deleted was not in the list of registered Events
            String errorMessage = "The processIntermediateEvent '" + processIntermediateEvent.toString()
                + "' does not belong to this group.";
            logger.error(errorMessage);
            throw new JodaEngineRuntimeException(errorMessage);
        }

        unsubscribingOtherEvent();

        token.resume(processIntermediateEvent);
    }

    /**
     * Unsubscribes all {@link ProcessIntermediateEvent} that are left.
     */
    // TODO @Gerardo muss geändert werden keine ServiceFactory mehr
    private void unsubscribingOtherEvent() {

        // Unsubscribing the other registered events; doing it as early as possible
        EventSubscriptionManager eventManager = ServiceFactory.getCorrelationService();
        for (ProcessIntermediateEvent registeredProcessEvent : getIntermediateEvents()) {
            eventManager.unsubscribeFromIntermediateEvent(registeredProcessEvent);
        }
    }
}
