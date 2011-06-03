package org.jodaengine.node.activity.bpmn;

import org.jodaengine.ServiceFactory;
import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.eventmanagement.adapter.manual.ManualTriggeringAdapter;
import org.jodaengine.eventmanagement.subscription.ProcessEvent;
import org.jodaengine.eventmanagement.subscription.ProcessEventGroup;
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

    private String name;

    /**
     * Default Constructor.
     * 
     * @param name
     *            - the name of the {@link ManualTriggeringAdapter}
     */
    public BpmnManualTriggeringIntermediateEventActivity(String name) {

        this.name = name;
    }

    @Override
    protected void executeIntern(Token token) {

        EventSubscriptionManager eventManager = ServiceFactory.getCorrelationService();

        ProcessIntermediateEvent processEvent = createProcessIntermediateEvent(token);

        eventManager.registerIntermediateEvent(processEvent);

        token.suspend();
    }

    
    //
    // ==== Interface that represents that this event can also be used by other nodes like event-based Gateway ====
    //
    @Override
    public ProcessIntermediateEvent createProcessIntermediateEvent(Token token) {

        return new ProcessIntermediateManualTriggeringEvent(name, token);
    }

    @Override
    public ProcessIntermediateEvent createProcessIntermediateEventForEventGroup(Token token, ProcessEventGroup eventGroup) {

        return new ProcessIntermediateManualTriggeringEvent(name, token, eventGroup);
    }
}
