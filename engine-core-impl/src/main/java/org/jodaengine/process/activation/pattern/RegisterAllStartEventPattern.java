package org.jodaengine.process.activation.pattern;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.eventmanagement.processevent.incoming.StartProcessEvent;
import org.jodaengine.process.activation.ProcessDefinitionActivationPatternContext;

/**
 * This registers all necessary StartEvent of a processDefinition at the EventSubscriptionManager.
 */
public class RegisterAllStartEventPattern extends AbstractProcessDefinitionDeActivationPattern {

    private List<StartProcessEvent> registeredStartEvents;

    @Override
    protected void activateProcessDefinitionIntern(ProcessDefinitionActivationPatternContext patternContext) {

        if (registeredStartEvents != null) {
            // Then it means that startEvents already were registered, so we do not need to do nothing
            return;
        }

        for (StartProcessEvent startEvent : patternContext.getProcessDefinition().getStartTriggers().keySet()) {
            patternContext.getEventManagerService().registerStartEvent(startEvent);
            getRegisteredStartEvents().add(startEvent);
        }
    }

    @Override
    protected void deactivateProcessDefinitionIntern(ProcessDefinitionActivationPatternContext patternContext) {

        // Copying the List of registered startEvents because it is modified in the loop
        ArrayList<StartProcessEvent> currentRegisteredStartEvents = new ArrayList<StartProcessEvent>();
        currentRegisteredStartEvents.addAll(getRegisteredStartEvents());

        for (StartProcessEvent startEvent : currentRegisteredStartEvents) {
            patternContext.getEventManagerService().unsubscribeFromStartEvent(startEvent);
            getRegisteredStartEvents().remove(startEvent);
        }
    }

    /**
     * Getter for the registeredStartEvents.
     * 
     * @return a {@link List} of {@link StartProcessEvent}s
     */
    public List<StartProcessEvent> getRegisteredStartEvents() {

        if (registeredStartEvents == null) {
            this.registeredStartEvents = new ArrayList<StartProcessEvent>();
        }

        return registeredStartEvents;
    }
}
