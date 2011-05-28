package org.jodaengine.node.activity.bpmn;

import org.jodaengine.ServiceFactory;
import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.eventmanagement.subscription.ProcessEvent;
import org.jodaengine.eventmanagement.subscription.ProcessIntermediateEvent;
import org.jodaengine.eventmanagement.subscription.processevent.intermediate.ProcessIntermediateManualTriggeringEvent;
import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.token.Token;

/**
 * This {@link BpmnManualTriggeringIntermediateEventActivity} is an activity that registers a {@link ProcessEvent} that
 * should be triggered manually.
 */
public class BpmnManualTriggeringIntermediateEventActivity extends AbstractActivity implements
BpmnEventBasedGatewayEvent {

    @Override
    public ProcessIntermediateEvent createProcessIntermediateEvent(Token token) {

        return new ProcessIntermediateManualTriggeringEvent(token);
    }

    @Override
    protected void executeIntern(Token token) {

        EventSubscriptionManager eventManager = ServiceFactory.getCorrelationService();

        ProcessIntermediateEvent processEvent = createProcessIntermediateEvent(token);

        eventManager.registerIntermediateEvent(processEvent);

        token.suspend();
    }
}
