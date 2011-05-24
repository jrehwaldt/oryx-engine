package org.jodaengine.process.activation.pattern;

import org.jodaengine.eventmanagement.subscription.ProcessStartEvent;
import org.jodaengine.process.activation.ProcessDefinitionActivationPatternContext;

/**
 * This registers all necessary StartEvent of a processDefinition at the EventSubscriptionManager.
 */
public class RegisterAllStartEventPattern extends AbstractProcessDefinitionActivationPattern{

    @Override
    protected void activateProcessDefinitionIntern(ProcessDefinitionActivationPatternContext patternContext) {

        for (ProcessStartEvent event : patternContext.getProcessDefinition().getStartTriggers().keySet()) {
            patternContext.getCorrelationService().registerStartEvent(event);
        }
    }
}
