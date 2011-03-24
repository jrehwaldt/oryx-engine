package de.hpi.oryxengine.correlation.registration;

import java.util.List;

import de.hpi.oryxengine.correlation.adapter.AdapterType;
import de.hpi.oryxengine.correlation.adapter.PullAdapterConfiguration;

/**
 * The Class ProcessEventImpl. Have a look at {@link ProcessEvent}.
 */
public class ProcessEventImpl implements ProcessEvent {

    /** The type. */
    private AdapterType type;

    /** The config. */
    private PullAdapterConfiguration config;

    /** The conditions. */
    private List<EventCondition> conditions;

    /**
     * Instantiates a new process event impl.
     * 
     * @param type
     *            the type
     * @param config
     *            the config
     * @param conditions
     *            the conditions
     */
    protected ProcessEventImpl(AdapterType type, PullAdapterConfiguration config, List<EventCondition> conditions) {

        this.type = type;
        this.config = config;
        this.conditions = conditions;
    }

    @Override
    public AdapterType getAdapterType() {

        return type;
    }

    @Override
    public PullAdapterConfiguration getAdapterConfiguration() {

        return config;
    }

    @Override
    public List<EventCondition> getConditions() {

        return conditions;
    }

}
