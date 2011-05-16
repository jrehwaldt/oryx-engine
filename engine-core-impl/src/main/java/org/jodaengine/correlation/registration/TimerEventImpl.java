package org.jodaengine.correlation.registration;

import org.jodaengine.correlation.adapter.TimedConfiguration;
import org.jodaengine.process.token.Token;

import java.util.List;


/**
 * The Class TimerEventImpl.
 */
public class TimerEventImpl implements IntermediateEvent {

    private Token token;
    private TimedConfiguration config;

    /**
     * Instantiates a new timer event impl.
     *
     * @param config the config for the event, consists out of options for scheduling and for the adaptor.
     * @param token the process token
     */
    public TimerEventImpl(TimedConfiguration config,
                          Token token) {

        this.token = token;
        this.config = config;
    }
    
    @Override
    public Token getToken() {

        return token;
    }

    @Override
    public TimedConfiguration getEventConfiguration() {

        return config;
    }

    @Override
    public List<EventCondition> getConditions() {
        return null;
    }

}
