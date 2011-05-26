package org.jodaengine.eventmanagement.subscription;

import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.condition.simple.TrueEventCondition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;



/**
 * The Class TimerEventImpl.
 */
public class TimerEventImpl extends AbstractProcessEvent implements ProcessIntermediateEvent {

    private Token token;
    private Node node;

    /**
     * Instantiates a new timer event impl.
     *
     * @param config the config for the event, consists out of options for scheduling and for the adaptor.
     * @param token the process token
     */
    public TimerEventImpl(AdapterConfiguration config,
                          Token token) {

        super(EventTypes.Timer, config, new TrueEventCondition());
        this.token = token;
        this.node = token.getCurrentNode();
    }
    
    @Override
    public Token getToken() {

        return token;
    }

    @Override
    public void trigger() {

        // Resuming the token with myself
        token.resume(this);
    }

    @Override
    public Node getFireringNode() {

        return node;
    }

    @Override
    public void setFireringNode(Node node) {

        this.node = node;
    }
}
