package org.jodaengine.process.activation.pattern;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.eventmanagement.processevent.incoming.intermediate.ProcessStartEvent;
import org.jodaengine.process.activation.ProcessDefinitionActivationPatternContext;

/**
 * This registers all necessary StartEvent of a processDefinition at the EventSubscriptionManager.
 */
public class RegisterAllStartEventPattern extends AbstractProcessDefinitionDeActivationPattern {

    private List<ProcessStartEvent> registeredStartEvents;

    @Override
    protected void activateProcessDefinitionIntern(ProcessDefinitionActivationPatternContext patternContext) {

        if (registeredStartEvents != null) {
            // Then it means that startEvents already were registered, so we do not need to do nothing
            return;
        }

        for (ProcessStartEvent startEvent : patternContext.getProcessDefinition().getStartTriggers().keySet()) {
            patternContext.getCorrelationService().registerStartEvent(startEvent);
            getRegisteredStartEvents().add(startEvent);
        }
    }

    @Override
    protected void deactivateProcessDefinitionIntern(ProcessDefinitionActivationPatternContext patternContext) {

        // Copying the List of registered startEvents because it is modified in the loop
        ArrayList<ProcessStartEvent> currentRegisteredStartEvents = new ArrayList<ProcessStartEvent>();
        currentRegisteredStartEvents.addAll(getRegisteredStartEvents());

        for (ProcessStartEvent startEvent : currentRegisteredStartEvents) {
            patternContext.getCorrelationService().unsubscribeFromStartEvent(startEvent);
            getRegisteredStartEvents().remove(startEvent);
        }
    }

    /**
     * Getter for the registeredStartEvents.
     * 
     * @return a {@link List} of {@link ProcessStartEvent}s
     */
    public List<ProcessStartEvent> getRegisteredStartEvents() {

        if (registeredStartEvents == null) {
            this.registeredStartEvents = new ArrayList<ProcessStartEvent>();
        }

        return registeredStartEvents;
    }
}
