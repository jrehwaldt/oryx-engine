package de.hpi.oryxengine.correlation.registration;

import java.util.List;

import de.hpi.oryxengine.correlation.adapter.TimedConfiguration;
import de.hpi.oryxengine.process.token.Token;

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
