package org.jodaengine.eventmanagement.subscription;

import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.condition.TrueEventCondition;
import org.jodaengine.process.token.Token;



/**
 * The Class TimerEventImpl.
 */
public class TimerEventImpl extends AbstractProcessEvent implements ProcessIntermediateEvent {

    private Token token;

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
    }
    
    @Override
    public Token getToken() {

        return token;
    }

    @Override
    public void trigger() {

        token.resume();
    }
}
