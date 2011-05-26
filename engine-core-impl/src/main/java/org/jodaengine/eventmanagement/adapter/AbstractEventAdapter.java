package org.jodaengine.eventmanagement.adapter;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract super adapter defining helper functions and a general structure.
 * 
 * @author Jan Rehwaldt
 * @param <Configuration>
 *            the adapter's configuration
 */
public abstract class AbstractEventAdapter<Configuration extends AdapterConfiguration> implements EventAdapter {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final Configuration configuration;
    
    private static final int MAGIC_HASH = -57;

    /**
     * Default constructor.
     * 
     * @param configuration
     *            the adapter's configuration
     */
    public AbstractEventAdapter(@Nonnull Configuration configuration) {

        this.configuration = configuration;

        logger.info("Initializing {} with config: {}", getClass().getSimpleName(), this.configuration);
    }

    @Override
    public final EventType getAdapterType() {

        return this.configuration.getEventType();
    }

    @Override
    public final @Nonnull
    Configuration getConfiguration() {

        return this.configuration;
    }

    @Override
    public int hashCode() {

        // derived from the configurations since it determines what the adapter is like
        return MAGIC_HASH * this.getConfiguration().hashCode();
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof EventAdapter) {
            EventAdapter adapter = (EventAdapter) o;
            // there should be no 2 adapters with the same configuration, so if their configuration is the same, so are
            // they
            return this.configuration.equals(adapter.getConfiguration());
        } else {
            return false;
        }
    }
}
