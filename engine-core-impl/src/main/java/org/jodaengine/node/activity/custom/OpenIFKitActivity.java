package org.jodaengine.node.activity.custom;

import org.jodaengine.eventmanagement.EventSubscriptionManagement;
import org.jodaengine.eventmanagement.adapter.phidget.IncomingOpenIFKitAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.phidget.OpenIFKitAdapterEvent;
import org.jodaengine.eventmanagement.processevent.incoming.condition.simple.MethodInvokingEventCondition;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IncomingIntermediateProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IntermediateIncomingOpenIFKitProcessEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.token.AbstractToken;
import org.jodaengine.process.token.Token;

/**
 * The Class OpenIFKitActivity.
 */
public class OpenIFKitActivity extends AbstractActivity {

    private int channel;
    
    /**
     * Instantiates a new open if kit activity.
     *
     * @param channel the channel
     */
    public OpenIFKitActivity(int channel) {
        this.channel = channel;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeIntern(AbstractToken token) {

        EventSubscriptionManagement eventManager = token.getEventManagerService();
        IncomingIntermediateProcessEvent event = createProcessEvent(token);
        eventManager.subscribeToIncomingIntermediateEvent(event);
        token.suspend();
    }
    
    @Override
    public void resume(Token token, Object resumeObject) {

        OpenIFKitAdapterEvent event = (OpenIFKitAdapterEvent) resumeObject;
        logger.debug("LLLLLLLOOOOOOOOOOOLLLLLLLLLLLL: " + event.getValue());
        super.resume(token, resumeObject);

    }

    /**
     * Creates the process event.
     * 
     * @param token
     *            the token
     * @return the incoming intermediate process event
     */
    private IncomingIntermediateProcessEvent createProcessEvent(Token token) {

        EventCondition subjectCondition = new MethodInvokingEventCondition(OpenIFKitAdapterEvent.class, 
                                                                            "getChannel", 
                                                                            channel);
        return new IntermediateIncomingOpenIFKitProcessEvent(new IncomingOpenIFKitAdapterConfiguration(),
            subjectCondition, token);

    }

}
