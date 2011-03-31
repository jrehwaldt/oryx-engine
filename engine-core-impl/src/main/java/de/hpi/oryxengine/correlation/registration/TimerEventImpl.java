package de.hpi.oryxengine.correlation.registration;

import java.util.List;
import java.util.UUID;

import de.hpi.oryxengine.correlation.adapter.AdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.AdapterType;
import de.hpi.oryxengine.correlation.adapter.PullAdapterConfiguration;
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
     * @param type the type
     * @param config the config
     * @param conditions the conditions
     * @param tokenId the token id
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

        // TODO Auto-generated method stub
        return null;
    }

}
