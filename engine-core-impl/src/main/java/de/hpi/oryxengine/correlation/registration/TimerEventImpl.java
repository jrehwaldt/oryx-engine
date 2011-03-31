package de.hpi.oryxengine.correlation.registration;

import java.util.List;

import de.hpi.oryxengine.correlation.adapter.AdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.AdapterType;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Class TimerEventImpl.
 */
public class TimerEventImpl implements IntermediateEvent {

    private Token token;
    private AdapterConfiguration config;

    /**
     * Instantiates a new timer event impl.
     *
     * @param config the config for the event, consists out of options for scheduling and for the adaptor.
     * @param token the process token
     */
    public TimerEventImpl(AdapterConfiguration config,
                          Token token) {

        this.token = token;
        this.config = config;
    }

    @Override
    public Token getToken() {

        return token;
    }

    @Override
    public AdapterType getAdapterType() {

        return this.config.getAdapterType();
    }

    @Override
    public AdapterConfiguration getAdapterConfiguration() {

        return config;
    }

    @Override
    public List<EventCondition> getConditions() {
        return null;
    }

}
