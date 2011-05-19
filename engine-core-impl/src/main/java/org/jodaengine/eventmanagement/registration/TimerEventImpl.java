package org.jodaengine.eventmanagement.registration;

import java.util.List;

import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.TimerConfiguration;
import org.jodaengine.process.token.Token;


/**
 * The Class TimerEventImpl.
 */
public class TimerEventImpl extends ProcessEventImpl implements ProcessIntermediateEvent {

    private Token token;
    private TimerConfiguration config;

    /**
     * Instantiates a new timer event impl.
     *
     * @param config the config for the event, consists out of options for scheduling and for the adaptor.
     * @param token the process token
     */
    public TimerEventImpl(TimerConfiguration config,
                          Token token) {

        // TODO @Gerardo mal schauen was da hin kommen kann
        super(EventTypes.Timer, config, null);
        this.token = token;
        this.config = config;
    }
    
    @Override
    public Token getToken() {

        return token;
    }

    @Override
    public List<EventCondition> getConditions() {
        return null;
    }

}
