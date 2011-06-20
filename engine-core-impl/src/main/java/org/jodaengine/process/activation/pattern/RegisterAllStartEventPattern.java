package org.jodaengine.process.activation.pattern;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.eventmanagement.processevent.incoming.IncomingStartProcessEvent;
import org.jodaengine.process.activation.ProcessDefinitionActivationPatternContext;

/**
 * This registers all necessary StartEvent of a processDefinition at the EventSubscriptionManager.
 */
public class RegisterAllStartEventPattern extends AbstractProcessDefinitionDeActivationPattern {

    private List<IncomingStartProcessEvent> registeredStartEvents;

    @Override
    protected void activateProcessDefinitionIntern(ProcessDefinitionActivationPatternContext patternContext) {

        if (registeredStartEvents != null) {
            // Then it means that startEvents already were registered, so we do not need to do nothing
            return;
        }

        for (IncomingStartProcessEvent startEvent : patternContext.getProcessDefinition().getStartTriggers().keySet()) {
            patternContext.getEventManagerService().subscribeToStartEvent(startEvent);
            getRegisteredStartEvents().add(startEvent);
        }
    }

    @Override
    protected void deactivateProcessDefinitionIntern(ProcessDefinitionActivationPatternContext patternContext) {

        // Copying the List of registered startEvents because it is modified in the loop
        ArrayList<IncomingStartProcessEvent> currentRegisteredStartEvents = new ArrayList<IncomingStartProcessEvent>();
        currentRegisteredStartEvents.addAll(getRegisteredStartEvents());

        for (IncomingStartProcessEvent startEvent : currentRegisteredStartEvents) {
            patternContext.getEventManagerService().unsubscribeFromStartEvent(startEvent);
            getRegisteredStartEvents().remove(startEvent);
        }
    }

    /**
     * Getter for the registeredStartEvents.
     * 
     * @return a {@link List} of {@link IncomingStartProcessEvent}s
     */
    public List<IncomingStartProcessEvent> getRegisteredStartEvents() {

        if (registeredStartEvents == null) {
            this.registeredStartEvents = new ArrayList<IncomingStartProcessEvent>();
        }

        return registeredStartEvents;
    }
}
