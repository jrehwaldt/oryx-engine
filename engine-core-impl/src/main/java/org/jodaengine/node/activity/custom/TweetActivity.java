package org.jodaengine.node.activity.custom;

import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.eventmanagement.adapter.twitter.OutgoingTwitterSingleAccountTweetAdapterConfiguration;
import org.jodaengine.eventmanagement.processevent.ProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IncomingIntermediateProcessEvent;
import org.jodaengine.eventmanagement.processevent.outgoing.OutgoingTweetEvent;
import org.jodaengine.node.activity.AbstractActivity;
import org.jodaengine.process.token.AbstractToken;
import org.jodaengine.process.token.Token;

/**
 * An activity used to tweet something.. for now the ProcessInstanceID
 */
public class TweetActivity extends AbstractActivity {

    public static final String PATH_TO_TWITTERCONFIG = "src/main/resources/twitter/twitter.properties";
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeIntern(AbstractToken token) {

        EventSubscriptionManager eventManager = token.getCorrelationService();

        IncomingIntermediateProcessEvent processEvent = createOutgoingIntermediateProcessEvent();

        eventManager.registerIncomingIntermediateEvent(processEvent);

        token.suspend();

    }
    
    /**
     * Creates the TwitterEcent.
     *
     * @return the process event
     */
    private ProcessEvent createOutgoingIntermediateProcessEvent() {
        return new OutgoingTweetEvent(new OutgoingTwitterSingleAccountTweetAdapterConfiguration(PATH_TO_TWITTERCONFIG));
    }

}
