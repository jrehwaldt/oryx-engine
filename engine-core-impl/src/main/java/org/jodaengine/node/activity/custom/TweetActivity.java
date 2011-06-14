package org.jodaengine.node.activity.custom;

import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.eventmanagement.processevent.ProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IncomingIntermediateProcessEvent;
import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.token.AbstractToken;
import org.jodaengine.process.token.Token;

/**
 * An activity used to tweet something.. for now the ProcessInstanceID
 */
public class TweetActivity extends AbstractActivity {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeIntern(AbstractToken token) {

        EventSubscriptionManager eventManager = token.getCorrelationService();

        IncomingIntermediateProcessEvent processEvent = createOutgoingIntermediateProcessEvent(token);

        eventManager.registerIncomingIntermediateEvent(processEvent);

        token.suspend();

    }
    
    private ProcessEvent createOutgoingIntermediateProcessEvent(Token token) {
        return null;
    }

}
