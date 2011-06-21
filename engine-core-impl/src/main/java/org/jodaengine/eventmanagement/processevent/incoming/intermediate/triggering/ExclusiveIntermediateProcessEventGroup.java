package org.jodaengine.eventmanagement.processevent.incoming.intermediate.triggering;

import org.jodaengine.ServiceFactory;
import org.jodaengine.eventmanagement.EventSubscriptionManagement;
import org.jodaengine.eventmanagement.processevent.incoming.AbstractIncomingProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.TriggeringBehavior;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IncomingIntermediateProcessEvent;
import org.jodaengine.eventmanagement.subscription.IncomingProcessEvent;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.token.Token;

/**
 * This represents a {@link TriggeringBehavior} that a soon a {@link IncomingProcessEvent} of this group is triggered this
 * group will unregister all other {@link IncomingProcessEvent} that are in this group.
 */
public class ExclusiveIntermediateProcessEventGroup extends AbstractThreadSafeIntermediateProcessEventGroup {

    /**
     * Default Constructor this {@link ExclusiveIntermediateProcessEventGroup}.
     * 
     * @param token
     *            - the {@link Token} that created this {@link ExclusiveIntermediateProcessEventGroup}
     */
    public ExclusiveIntermediateProcessEventGroup(Token token) {

        super(token);
    }

    @Override
    public void triggerIntern(IncomingIntermediateProcessEvent processIntermediateEvent) {

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
     * Unsubscribes all {@link IncomingIntermediateProcessEvent} that are left.
     */
    // TODO @Gerardo muss geändert werden keine ServiceFactory mehr
    private void unsubscribingOtherEvent() {

        // Unsubscribing the other registered events; doing it as early as possible
        EventSubscriptionManagement eventManager = ServiceFactory.getEventManagerService();
        for (AbstractIncomingProcessEvent registeredProcessEvent : getIntermediateEvents()) {
            eventManager.unsubscribeFromIncomingIntermediateEvent((IncomingIntermediateProcessEvent) registeredProcessEvent);
        }
    }
}
